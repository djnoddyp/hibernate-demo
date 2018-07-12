package demo;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FetchingStrategiesTest {


    EntityManagerFactory emf;
    EntityManager em;

    @BeforeClass
    public void setup() {
        emf = Persistence.createEntityManagerFactory("DEMO");
        em = emf.createEntityManager();
    }

    @Test
    public void testLazyFetching() {

    }

}
