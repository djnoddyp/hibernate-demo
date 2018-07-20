package demo;

import demo.constants.QueryConstants;
import demo.entities.Bike;
import demo.entities.BikeName;
import demo.entities.BikeShop;
import demo.enums.Style;

import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.RollbackException;

import static org.junit.Assert.*;

/**
 * Uses resource-local application managed EntityManager, for JTA container managed see InContainerTest.java
 */
public class SimpleTest extends BootstrapJPA {

    @Test
    public void testSimplePersist() {
        BikeShop bikeShop = new BikeShop();
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        Bike bike2 = new Bike(new BikeName("GT", "Avalanche"), 21, Style.HYBRID);
        Bike bike3 = new Bike(new BikeName("S-Works", "Comp"), 10, Style.ROAD);
        bikeShop.addBike(bike1);
        bikeShop.addBike(bike2);
        bikeShop.addBike(bike3);

        em.getTransaction().begin();
        em.persist(bikeShop);
        em.getTransaction().commit();

        assertEquals(3, em.createQuery(QueryConstants.BIKE_BIKESHOP)
                .setParameter("bikeShop", bikeShop).getResultList().size());
    }

    @Test
    public void testSimpleEntityStateTransition() {
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        em.getTransaction().begin();
        em.persist(bike1);
        // Entity is now managed
        assertEquals(Style.MOUNTAIN, em.createQuery(QueryConstants.BIKE_STYLE_MODEL)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
        
        // Update entity
        bike1.setStyle(Style.ROAD);
        em.getTransaction().commit();
        assertEquals(Style.ROAD, em.createQuery(QueryConstants.BIKE_STYLE_MODEL)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
    }

    @Test
    public void testDetachAndReattachEntity() {
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        em.getTransaction().begin();
        em.persist(bike1);
        // Entity is now managed
        assertEquals(Style.MOUNTAIN, em.createQuery(QueryConstants.BIKE_STYLE_MODEL)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
        
        // Clear the Persistence context (first level cache)
        em.clear();
        // Update (no longer managed) entity
        bike1.setStyle(Style.ROAD);
        // Assert that changes were not propagated
        assertNotEquals(Style.ROAD, em.createQuery(QueryConstants.BIKE_STYLE_MODEL)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
        
        // Re-attach entity
        em.merge(bike1);
        em.getTransaction().commit();
        assertEquals(Style.ROAD, em.createQuery(QueryConstants.BIKE_STYLE_MODEL)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
    }    
    
    @Test
    public void testHibernateFormula() {
        Bike bike1 = new Bike(new BikeName("Giant", "Talon"), 18, Style.MOUNTAIN);
        Double price = Double.valueOf(750.95d);
        bike1.setPrice(price);
        em.getTransaction().begin();
        em.persist(bike1);
        em.getTransaction().commit();
        assertEquals(price * 1.31, em.createQuery(QueryConstants.BIKE_PRICEINDOLLARS_MODEL)
                .setParameter("model", "Talon").getResultStream().findFirst().get());
    }
    
    @Test(expected = RollbackException.class)
    public void testUniqueConstraint() {
        em.getTransaction().begin();
        for (int i = 0; i < 2; i++) {
            em.persist(new BikeShop("10 Belfast Road"));
        }
        em.getTransaction().commit();
    }
    
    
}
