package demo;


import demo.entities.Bike;
import demo.entities.BikeShop;
import demo.entities.Employee;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class FetchingStrategiesTest extends BootstrapJPATest {

    @Test
    public void testEagerFetching() {
        BikeShop b = new BikeShop();
        Set<Bike> bikes = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Bike bike = new Bike();
            bike.setGears(i);
            bike.setBikeShop(b);
            bikes.add(bike);
        }
        b.setBikes(bikes);
        em.getTransaction().begin();
        em.persist(b);
        em.getTransaction().commit();
        em.clear();
        BikeShop b2 = em.find(BikeShop.class, 1);
        em.detach(b2);
        assertNotNull(b2.getBikes());
    }

    @Test(expected = LazyInitializationException.class)
    public void testLazyFetching() {
        BikeShop b = new BikeShop();
        Set<Employee> employees = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Employee e = new Employee();
            e.setBikeShop(b);
            e.setEmployeeNumber(i);
            employees.add(e);
        }
        b.setEmployees(employees);
        em.getTransaction().begin();
        em.persist(b);
        em.getTransaction().commit();
        em.clear();
        BikeShop b2 = em.find(BikeShop.class, 1);
        em.detach(b2);
        System.out.println(b2.getEmployees());
    }

}
