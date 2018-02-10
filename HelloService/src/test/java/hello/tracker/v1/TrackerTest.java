package hello.tracker.v1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

import java.util.List;

import hello.BaseDataLoader;
import hello.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrackerConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({TrackerDataLoader.class})
public class TrackerTest extends BaseTest {

    final static private Logger LOG = LogManager.getLogger();

    @Autowired private TestEntityManager entityManager;

    @Autowired private ExerciseRepository exerciseRepository;

    @Autowired private GoalRepository goalRepository;

    static final String EXERCISE_NAME = "A SAMPLE EXERCISE";

    static final long GOAL_ID = 1;

    @After
    public void tearDown() {
        entityManager.flush();
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);
    }

    @Test
    public void testSaveExercise() throws Exception {
        Goal goal = entityManager.getEntityManager().getReference(Goal.class, GOAL_ID);
        Exercise exercise = new Exercise();
        exercise.setMinutes(15);
        exercise.setGoal(goal);
        exercise.setActivity("Walking the dog");
        exerciseRepository.save(exercise);
    }

    @Test
    public void makeGoalReportById() throws Exception {
        this.testSaveExercise();
        this.testSaveExercise();
        entityManager.flush();
        Query query = entityManager.getEntityManager().createQuery("Select new hello.tracker.v1.GoalReport(g.minutes, e.minutes, e.activity) " +
                      "from Goal g, Exercise e where g.id = e.goal.id");

        List<GoalReport> results = query.getResultList();
        assertNotNull(results);
        assertThat(results, Matchers.iterableWithSize(2));
        results.forEach(LOG::debug);
    }


}

@Component
@Transactional
class TrackerDataLoader extends BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @Autowired private ExerciseRepository exerciseRepository;

    @Autowired private GoalRepository goalRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Goal goal = new Goal();
        goal.setMinutes(45);
        goalRepository.save(goal);

        entityManager.flush();

        LOG.debug("****************************");
    }
}
