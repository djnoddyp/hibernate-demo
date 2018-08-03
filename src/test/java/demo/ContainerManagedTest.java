package demo;

import demo.constants.QueryConstants;
import demo.dtos.BikeShopDTO;
import demo.entities.Bike;
import demo.entities.BikeName;
import demo.entities.BikeShop;
import demo.entities.Employee;
import demo.enums.Style;
import org.hibernate.LazyInitializationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Uses JTA container managed EntityManager. See test-persistence.xml
 * for JPA configuration.
 */
@RunWith(Arquillian.class)
@Transactional
public class ContainerManagedTest {

    // Inject a JTA container managed EntityManager
    @PersistenceContext(unitName = "ARQTEST")
    private EntityManager em;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackage(BikeShop.class.getPackage())
                .addPackage("demo.dtos")
                .addPackage("demo.enums")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(war.toString(true));
        return war;
    }


    /**
     * Simple test to demonstrate the difference between an application-managed
     * EntityManager (a la ApplicationManagedTest.java) and a JTA one
     * Note: JTA cannot use entityManager#getTransaction since container is managing the transactions
     */
    @Test
    public void testSimplePersist() {
        BikeShop bikeShop = createBikeShop();
        // A JTA EntityManager cannot use getTransaction()!!
        //em.getTransaction().begin();
        em.persist(bikeShop);
        //em.getTransaction().commit();
        assertEquals(3, em.createQuery(QueryConstants.BIKE_BIKESHOP)
                .setParameter("bikeShop", bikeShop).getResultList().size());
    }

    /**
     * http://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#fetching-basics
     * 
     * Persistence Context API generates an extra JOIN on the Eagerly-loaded Bikes collection
     * JPQL generates an extra SELECT on the Eagerly-loaded Bikes collection
     */
    @Test
    public void testFetchStrategy() {
        BikeShop bikeShop = createBikeShop();
        em.persist(bikeShop);
        em.flush();
        em.clear();
        // fetch using Persistence Context API
        assertNotNull(em.find(BikeShop.class, bikeShop.getId()));
        em.clear();
        // fetch using JPQL
        BikeShop bs = em.createQuery(QueryConstants.BIKESHOP, BikeShop.class)
                .setParameter("id", bikeShop.getId()).getSingleResult();
        assertNotNull(bs);
    }

    /**
     * Lazily-loaded collection (Employees) cannot be accessed once the entity has
     * become detached, causing LazyInitializationException.
     */
    @Test(expected = LazyInitializationException.class)
    public void testLazyInitializationException() {
        BikeShop bikeShop = createBikeShop();
        em.persist(bikeShop);
        em.flush();
        em.clear();
        BikeShop bs = em.createQuery(QueryConstants.BIKESHOP, BikeShop.class)
                .setParameter("id", bikeShop.getId()).getSingleResult();
        em.detach(bs);
        assertEquals(3, bs.getEmployees().size());
    }

    /**
     * Some options to avoid the LazyInitializationException:
     * 
     * 1. Change to EAGER (bad)
     * 2. Use a JOIN FETCH to load the collection when it is needed (good)
     * 3. Others, see https://vladmihalcea.com/the-best-way-to-handle-the-lazyinitializationexception/
     */
    @Test
    public void testJoinFetchToTheRescue() {
        BikeShop bikeShop = createBikeShop();
        em.persist(bikeShop);
        em.flush();
        em.clear();
        BikeShop bs = em.createQuery(QueryConstants.BIKESHOP_JOIN_FETCH_EMPLOYEES, BikeShop.class)
                .setParameter("id", bikeShop.getId()).getSingleResult();
        em.detach(bs);
        assertEquals(3, bs.getEmployees().size());
    }

    /**
     * Eagerly-loaded collection (Bikes) CAN be accessed once the entity has
     * become detached.
     */
    @Test
    public void testEagerFetching() {
        BikeShop bikeShop = createBikeShop();
        em.persist(bikeShop);
        em.flush();
        em.clear();
        BikeShop bs = em.createQuery(QueryConstants.BIKESHOP, BikeShop.class)
                .setParameter("id", bikeShop.getId()).getSingleResult();
        em.detach(bs);
        assertEquals(3, bs.getBikes().size());
    }

    /**
     * Entity can be projected onto a DTO, thereby selecting only as many
     * columns as are needed to fulfil a business use case.
     */
    @Test
    public void testDtoProjectionToTheRescue() {
        BikeShop bikeShop = createBikeShop();
        bikeShop.setAddress("10 Ladas Drive");
        em.persist(bikeShop);
        BikeShopDTO dto = em.createQuery(QueryConstants.BIKESHOP_DTO, BikeShopDTO.class)
                .setParameter("id", bikeShop.getId()).getSingleResult();
        assertEquals("10 Ladas Drive", dto.getAddress());
    }

    /**
     * See test-persistence.xml, will batch the INSERT statements into batches
     * of max 10. See statistics in console- 5 JDBC batches should be executed.
     * 
     * Note: Only inserts/updates etc. into the same table will be batched,
     * see AbstractEntityPersister.java in Hibernate
     */
    @Test
    public void testBatching() {
        int entityCount = 50;
        for (int i = 0; i < entityCount; i++) {
            em.persist(new Employee());
        }
    }

    /**
     * Hibernate can execute native SQL e.g. a window function.
     * This one returns the following result:
     *
     *  bikeshop_id |  make   |   model   | style | price  |   sum
     * -------------+---------+-----------+-------+--------+---------
     *            1 | GT      | Avalanche |     2 | 499.29 |  499.29
     *            1 | Giant   | Trance    |     1 | 900.95 | 1400.24
     *            1 | S-Works | Comp      |     0 |   1200 | 2600.24
     *            8 | GT      | Avalanche |     2 | 499.29 |  499.29
     *            8 | Giant   | Trance    |     1 | 900.95 | 1400.24
     *            8 | S-Works | Comp      |     0 |   1200 | 2600.24
     */
    @Test
    public void testNativeQuery() {
        BikeShop bs = createBikeShop();
        em.persist(bs);
        BikeShop bs2 = createBikeShop();
        em.persist(bs2);
        em.flush();
        em.clear();
        List<Object[]> bikes = em.createNativeQuery(QueryConstants.BIKESHOP_NATIVE).getResultList();
        Object[] bike = bikes.get(2);
        assertEquals(Double.valueOf(2600.24), bike[5]);
    }
    
    
    
    BikeShop createBikeShop() {
        BikeShop bikeShop = new BikeShop();
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        Bike bike2 = new Bike(new BikeName("GT", "Avalanche"), 21, Style.HYBRID);
        Bike bike3 = new Bike(new BikeName("S-Works", "Comp"), 10, Style.ROAD);
        bike1.setPrice(900.95);
        bike2.setPrice(499.29);
        bike3.setPrice(1200.00);
        bikeShop.addBike(bike1);
        bikeShop.addBike(bike2);
        bikeShop.addBike(bike3);
        for (int i = 0; i < 3; i++) {
            bikeShop.addEmployee(new Employee());
        }
        return bikeShop;
    }

}
