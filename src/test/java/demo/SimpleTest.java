package demo;

import demo.entities.Bike;
import demo.entities.BikeName;
import demo.entities.BikeShop;
import demo.enums.Style;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class SimpleTest extends BootstrapJPATest {

    final String query = "SELECT b FROM Bike b WHERE b.bikeShop = :bikeShop";

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

        assertSame(3, em.createQuery(query).setParameter("bikeShop", bikeShop).getResultList().size());
    }



}
