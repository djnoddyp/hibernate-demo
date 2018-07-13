package demo;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class BootstrapJPATest {

    static EntityManager em;

    @Before
    public void init() {
        em =  Persistence.createEntityManagerFactory("DEMO").createEntityManager();
    }

    @After
    public void tearDown() {
        em.clear();
        em.close();
    }

}
