package demo;

import demo.constants.QueryConstants;
import demo.entities.Bike;
import demo.entities.BikeName;
import demo.entities.BikeShop;
import demo.enums.Style;
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

    @Test
    public void testSimplePersist() {
        // create entities
        BikeShop bikeShop = new BikeShop();
        bikeShop.setAddress("10 Mountain Road");
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        Bike bike2 = new Bike(new BikeName("GT", "Avalanche"), 21, Style.MOUNTAIN);
        Bike bike3 = new Bike(new BikeName("S-Works", "Comp"), 10, Style.ROAD);
        bike1.setBikeShop(bikeShop);
        bike2.setBikeShop(bikeShop);
        bike3.setBikeShop(bikeShop);
        Set<Bike> bikeSet = new HashSet<>();
        bikeSet.add(bike1);
        bikeSet.add(bike2);
        bikeSet.add(bike3);
        bikeShop.setBikes(bikeSet);

        // A JTA EntityManager cannot use getTransaction()

        //em.getTransaction().begin();
        em.persist(bikeShop);
        //em.getTransaction().commit();

        assertSame(3, em.createQuery(QueryConstants.BIKE_BIKESHOP)
                .setParameter("bikeShop", bikeShop).getResultList().size());
    }

}
