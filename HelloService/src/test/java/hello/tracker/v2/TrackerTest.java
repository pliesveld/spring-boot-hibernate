package hello.tracker.v2;


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
import java.util.Optional;

import hello.BaseDataLoader;
import hello.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.*;
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

    @Autowired private ActivityRepository activityRepository;

    static final String EXERCISE_NAME = "A SAMPLE EXERCISE";

    static final long GOAL_ID = 1;

    static final long ACTIVITY_ID = 1;


    private String ACTIVITY_NAME_EXAMPLE1 = "A SAMPLE ACTIVITY";

    @After
    public void tearDown() {
        entityManager.flush();
    }

    private Goal getGoal() {
        return entityManager.getEntityManager().getReference(Goal.class, GOAL_ID);
    }

    private Activity getActivity() {
        return entityManager.getEntityManager().getReference(Activity.class, ACTIVITY_ID);
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);

        assertTrue(goalRepository.existsById(GOAL_ID));
        assertTrue(activityRepository.existsById(ACTIVITY_ID));

        Optional<Goal> goal = goalRepository.findById(GOAL_ID);
        Optional<Activity> activity = activityRepository.findById(ACTIVITY_ID);

        assertTrue(goal.isPresent());
        assertTrue(activity.isPresent());
    }

    @Test
    public void loadGoalID1() throws Exception {
        Goal goal = getGoal();
        assertNotNull(goal);
        assertThat(goal.getExercises(), IsIterableWithSize.iterableWithSize(0));
        assertThat(goal.getId(), is(equalTo(GOAL_ID)));
    }

    @Test
    public void testSaveExercise() throws Exception {
        Goal goal = getGoal();
        Exercise exercise = new Exercise();
        exercise.setMinutes(15);
        exercise.setGoal(goal);
        exercise.setActivity(getActivity());
        exerciseRepository.save(exercise);
    }

    @Test
    public void testSaveTwoExercisesOfSameActivity() throws Exception {
        Goal goal = getGoal();

        Long EXERCISE1_ID;
        Long EXERCISE2_ID;

        {
            Exercise exercise1 = new Exercise();
            exercise1.setMinutes(15);
            exercise1.setGoal(goal);
            exercise1.setActivity(getActivity());
            exerciseRepository.save(exercise1);

            Exercise exercise2 = new Exercise();
            exercise2.setMinutes(15);
            exercise2.setGoal(goal);
            exercise2.setActivity(getActivity());
            exerciseRepository.save(exercise2);

            EXERCISE1_ID = exercise1.getId();
            EXERCISE2_ID = exercise2.getId();
        }

        entityManager.flush(); entityManager.clear();

        {
            Exercise exercise1 = exerciseRepository.findById(EXERCISE1_ID).get();
            Exercise exercise2 = exerciseRepository.findById(EXERCISE2_ID).get();

            assertNotNull(exercise1);
            assertNotNull(exercise1.getId());
            assertNotNull(exercise1.getActivity());
            assertNotNull(exercise2);
            assertNotNull(exercise2.getId());
            assertNotNull(exercise2.getActivity());
        }
        assertNotNull(EXERCISE1_ID);
    }

    @Test
    public void savesNewActivity() throws Exception {
        Activity activity = new Activity();
        activity.setName(ACTIVITY_NAME_EXAMPLE1);
        activity.setDescription(ACTIVITY_NAME_EXAMPLE1);
        activityRepository.save(activity);
    }

    @Test
    public void savesNewExercise() throws Exception {
        this.savesNewActivity();
        entityManager.flush();

        Goal goal = getGoal();
        Exercise exercise = new Exercise();
        exercise.setActivity(getActivity());
        exercise.setMinutes(30);
        exercise.setGoal(goal);
        exerciseRepository.save(exercise);
    }

    @Test
    public void makeGoalReportById() throws Exception {
        this.testSaveExercise();
        this.testSaveExercise();
        entityManager.flush();
        Query query = entityManager.getEntityManager().createQuery("Select new hello.tracker.v2.GoalReport(g.minutes, e.minutes, e.activity.name) " +
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

    @Autowired private ActivityRepository activityRepository;

    private String ACTIVITY_NAME_EXAMPLE1 = "A SAMPLE ACTIVITY";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Goal goal = new Goal();
        goal.setMinutes(45);
        goalRepository.save(goal);

        Activity activity = new Activity();
        activity.setName(ACTIVITY_NAME_EXAMPLE1);
        activity.setDescription(ACTIVITY_NAME_EXAMPLE1);
        activityRepository.save(activity);

        entityManager.flush();

        LOG.debug("****************************");
    }
}
