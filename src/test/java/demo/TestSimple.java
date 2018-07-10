package demo;

import demo.entities.Bike;
import demo.entities.BikeName;
import demo.entities.BikeShop;
import demo.enums.Style;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

public class TestSimple {

    static EntityManager em;

    @BeforeClass
    public static void init() {
        em =  Persistence.createEntityManagerFactory("DEMO").createEntityManager();
    }

    @AfterClass
    public static void tearDown() {
        em.clear();
        em.close();
    }

    @Test
    public void testPersist() {
        // create entities
        BikeShop bikeShop = new BikeShop();
        bikeShop.setAddress("10 Mountain Road");
        Bike bike1 = new Bike(new BikeName("Giant", "Trance"), 18, Style.MOUNTAIN);
        Bike bike2 = new Bike(new BikeName("GT", "Avalanche"), 21, Style.MOUNTAIN);
        Bike bike3 = new Bike(new BikeName("S-Works", "Comp"), 18, Style.ROAD);
        Set<Bike> bikeSet = new HashSet<>();
        bikeSet.add(bike1);
        bikeSet.add(bike2);
        bikeSet.add(bike3);
        bikeShop.setBikes(bikeSet);

        //persist
        em.persist(bikeShop);
    }

}
