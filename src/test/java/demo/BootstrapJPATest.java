package demo;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class BootstrapJPATest {

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

}
