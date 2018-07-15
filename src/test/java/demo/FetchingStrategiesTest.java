package demo;


import demo.entities.Bike;
import demo.entities.BikeShop;
import demo.entities.Employee;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Uses resource-local application managed EntityManager, for JTA container managed see InContainerTest.java
 */
public class FetchingStrategiesTest extends BootstrapJPA {

    @Test
    public void testEagerFetching() {
        BikeShop bikeShop = new BikeShop();
        for (int i = 0; i < 10; i++) {
            Bike bike = new Bike();
            bike.setGears(i);
            bikeShop.addBike(bike);
        }
        em.getTransaction().begin();
        em.persist(bikeShop);
        em.getTransaction().commit();
        em.clear();
        BikeShop b = em.find(BikeShop.class, Long.valueOf(1L));
        em.detach(b);
        assertNotNull(b.getBikes());
    }

    @Test(expected = LazyInitializationException.class)
    public void testLazyFetching() {
        BikeShop bikeShop = new BikeShop();
        for (int i = 0; i < 10; i++) {
            Employee e = new Employee();
            e.setEmployeeNumber(i);
            bikeShop.addEmployee(e);
        }
        em.getTransaction().begin();
        em.persist(bikeShop);
        em.getTransaction().commit();
        em.clear();
        BikeShop b = em.find(BikeShop.class, Long.valueOf(1L));
        em.detach(b);
        System.out.println(b.getEmployees());
    }

}
