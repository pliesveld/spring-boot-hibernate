package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import(BaseDataLoader.class)
@Transactional
public class DomainPersistenceTest {

    final static private Logger LOG = LogManager.getLogger();

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenContext_shouldLoad() throws Exception {
        assertNotNull(entityManager);
    }
}

