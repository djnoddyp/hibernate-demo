package demo;

import demo.entities.Bike;
import demo.entities.BikeName;
import demo.entities.BikeShop;
import demo.enums.Style;

import org.junit.Test;

import javax.persistence.RollbackException;

import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class SimpleTest extends BootstrapJPATest {

    final String query = "SELECT b FROM Bike b WHERE b.bikeShop = :bikeShop";
    final String query2 = "SELECT b.style FROM Bike b WHERE b.bikeName.model = :model";
    final String query3= "SELECT b.priceInDollars FROM Bike b WHERE b.bikeName.model = :model";
    

    @Test
    public void testSimplePersist() {
        // create entities
        BikeShop bikeShop = new BikeShop();
        bikeShop.setAddress("10 Mountain Road");
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        Bike bike2 = new Bike(new BikeName("GT", "Avalanche"), 21, Style.MOUNTAIN);
        Bike bike3 = new Bike(new BikeName("S-Works", "Comp"), 18, Style.ROAD);
        bike1.setBikeShop(bikeShop);
        bike2.setBikeShop(bikeShop);
        bike3.setBikeShop(bikeShop);
        Set<Bike> bikeSet = new HashSet<>();
        bikeSet.add(bike1);
        bikeSet.add(bike2);
        bikeSet.add(bike3);
        bikeShop.setBikes(bikeSet);

        // persist
        em.getTransaction().begin();
        em.persist(bikeShop);
        em.getTransaction().commit();

        assertSame(3, em.createQuery(query)
                .setParameter("bikeShop", bikeShop).getResultList().size());
    }

    @Test
    public void testSimpleEntityStateTransition() {
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        em.getTransaction().begin();
        em.persist(bike1);
        // Entity is now managed
        assertEquals(Style.MOUNTAIN, em.createQuery(query2)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
        
        // Update entity
        bike1.setStyle(Style.ROAD);
        em.getTransaction().commit();
        assertEquals(Style.ROAD, em.createQuery(query2)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
    }

    @Test
    public void testDetachAndReattachEntity() {
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        em.getTransaction().begin();
        em.persist(bike1);
        // Entity is now managed
        assertEquals(Style.MOUNTAIN, em.createQuery(query2)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
        
        // Clear the Persistence context (first level cache)
        em.clear();
        // Update (no longer managed) entity
        bike1.setStyle(Style.ROAD);
        // Assert that changes were not propagated
        assertNotEquals(Style.ROAD, em.createQuery(query2)
                .setParameter("model", "Trance").getResultStream().findFirst().get());
        
        // Re-attach entity
        em.merge(bike1);
        em.getTransaction().commit();
        assertEquals(Style.ROAD, em.createQuery(query2)
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
        assertEquals(price * 1.31, em.createQuery(query3)
                .setParameter("model", "Talon").getResultStream().findFirst().get());
    }
    
    @Test(expected = RollbackException.class)
    public void testUniqueConstraint() {
        em.getTransaction().begin();
        for (int i = 0; i < 2; i++) {
            em.persist(new Bike(new BikeName("Giant", "Talon"), 18, Style.MOUNTAIN));
        }
        em.getTransaction().commit();
    }
    
    
}
