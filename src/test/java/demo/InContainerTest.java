package demo;

import demo.constants.QueryConstants;
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
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Uses JTA container managed EntityManager
 */
@RunWith(Arquillian.class)
@Transactional
public class InContainerTest {

    // Inject a JTA container managed EntityManager
    @PersistenceContext(unitName = "ARQTEST")
    private EntityManager em;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackage(BikeShop.class.getPackage())
                .addClass(Style.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * Simple test to demonstrate the difference between an application-managed
     * EntityManager (a la SimpleTest.java) and a JTA one
     * Note: JTA cannot use entityManager#getTransaction since container is managing the transactions
     */
    @Test
    public void testSimplePersist() {
        BikeShop bikeShop = createBikeShop();
        // A JTA EntityManager cannot use getTransaction()
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
     * 3. See https://vladmihalcea.com/the-best-way-to-handle-the-lazyinitializationexception/
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
    public void testNoLazyInitializationException() {
        BikeShop bikeShop = createBikeShop();
        em.persist(bikeShop);
        em.flush();
        em.clear();
        BikeShop bs = em.createQuery(QueryConstants.BIKESHOP, BikeShop.class)
                .setParameter("id", bikeShop.getId()).getSingleResult();
        em.detach(bs);
        assertEquals(3, bs.getBikes().size());
    }
    
    
    
    
    
    BikeShop createBikeShop() {
        BikeShop bikeShop = new BikeShop();
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        Bike bike2 = new Bike(new BikeName("GT", "Avalanche"), 21, Style.MOUNTAIN);
        Bike bike3 = new Bike(new BikeName("S-Works", "Comp"), 10, Style.ROAD);
        bikeShop.addBike(bike1);
        bikeShop.addBike(bike2);
        bikeShop.addBike(bike3);
        for (int i = 0; i < 3; i++) {
            bikeShop.addEmployee(new Employee());
        }
        return bikeShop;
    }

}
