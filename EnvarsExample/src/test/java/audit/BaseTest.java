package audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import static audit.Constants.DIARY_ID;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

class Constants {
    protected static Long DIARY_ID = 1L;
}

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuditConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({ScriptDataLoader.class})
public class BaseTest {

    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @Autowired protected ScriptRepository scriptRepository;

    @Autowired protected TestEntityManager entityManager;

    protected Script script;

    @Before
    public void setUp() throws Exception {
        LOG.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        script = scriptRepository.findById(DIARY_ID).get();
        LOG.debug("++++++++++++++++++++++++++++");
    }

    @After
    public void flushAfterTest() {
        if (entityManager.getEntityManager().getTransaction().isActive()) {
            entityManager.flush();
            entityManager.clear();
        }
    }

    protected void transactionCommit() {
        entityManager.flush();
        entityManager.clear();
        entityManager.getEntityManager().getTransaction().commit();
    }

    @Test
    public void testUpdates() {
        assertNotNull(script);
        assertHasRevisionsCount(1);
        script.setText("This is the second revision.");
        scriptRepository.save(script);
        transactionCommit();
        assertHasRevisionsCount(2);
    }

    @Test
    public void testAuditHQL() {


//        Query query = entityManager.getEntityManager().createQuery("SELECT AUD.originalId FROM audit.Script_AUD as AUD");
//        Query query = entityManager.getEntityManager().createQuery("SELECT id.REV FROM audit.Script_AUD as AUD");
        Query query = entityManager.getEntityManager().createQuery("SELECT AUD.id.REV.timestamp FROM audit.Script_AUD as AUD WHERE AUD.id.REV.id = :id", Long.class);
        query.setParameter("id", Integer.parseInt(script.getId().toString()));
        Object result = query.getSingleResult();

        LOG.debug("Script created: {}", script.getCreatedDate());
        LOG.debug("Script modified: {}", script.getModifiedDate());
        LOG.debug("Audit timestamp: {}", result);



    }

    public void assertHasRevisionsCount(int expected) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager.getEntityManager());
        List<Number> revisions =  auditReader.getRevisions(Script.class, DIARY_ID);
        assertNotNull(revisions);
        assertThat(revisions, iterableWithSize(expected));
    }
}

@Component
@Transactional
class ScriptDataLoader extends BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @Autowired
    ScriptRepository scriptRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Script script = new Script();
        script.setText("Dear Diary; What is the meaning of life?");
        scriptRepository.save(script);
        entityManager.flush();
        LOG.debug("****************************");
    }
}
