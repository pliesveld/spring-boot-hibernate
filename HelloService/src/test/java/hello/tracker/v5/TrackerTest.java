package hello.tracker.v5;


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

import java.time.DayOfWeek;
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
import static hello.tracker.v5.TrackerTest.USER_ID;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
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

    @Autowired private UserRepository userRepository;

    @Autowired private DailyGoalRepository dailyGoalRepository;

    static final String EXERCISE_NAME = "A SAMPLE EXERCISE";

    private String USER_NAME_EXAMPLE = "A SAMPLE USER";

    private String ACTIVITY_NAME_EXAMPLE1 = "A SAMPLE ACTIVITY";

    static final long GOAL_ID = 1;

    static final long ACTIVITY_ID = 1;

    static final long USER_ID = 1;

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

    private User getUser() {
        return entityManager.getEntityManager().getReference(User.class, USER_ID);
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);

        assertTrue(goalRepository.existsById(GOAL_ID));
        assertTrue(activityRepository.existsById(ACTIVITY_ID));
        assertTrue(userRepository.existsById(USER_ID));

        Optional<Goal> goal = goalRepository.findById(GOAL_ID);
        Optional<Activity> activity = activityRepository.findById(ACTIVITY_ID);
        Optional<User> user = userRepository.findById(USER_ID);

        assertTrue(goal.isPresent());
        assertTrue(activity.isPresent());
        assertTrue(user.isPresent());
    }

    @Test
    public void loadGoalID1() throws Exception {
        Goal goal = getGoal();
        assertNotNull(goal);
        assertThat(goal.getExercises(), IsIterableWithSize.iterableWithSize(0));
        assertThat(goal.getId(), is(equalTo(GOAL_ID)));
    }

    @Test
    public void saveDailyGoal() throws Exception {
        Goal goal = getGoal();
        Activity activity = getActivity();
        DayOfWeek weekday = DayOfWeek.SUNDAY;

        DailyGoal.Id id = new DailyGoal.Id(USER_ID, weekday);

        DailyGoal dailyGoal = new DailyGoal();
        dailyGoal.setId(id);
        dailyGoal.getGoals().put(activity, goal);
        entityManager.persist(dailyGoal);
    }

    @Test
    public void saveNewActivityToExistingDailyGoal() throws Exception {

        this.saveDailyGoal();
        entityManager.getEntityManager().flush();
        entityManager.getEntityManager().clear();

        Goal goal = getGoal();
        DailyGoal.Id id = new DailyGoal.Id(USER_ID, DayOfWeek.SUNDAY);
        Optional<DailyGoal> dailyGoal = dailyGoalRepository.findById(id);

        assertTrue(dailyGoal.isPresent());

//        Activity activity = getActivity();
//        Goal goal = dailyGoal.get().getGoals().get(activity);
//        assertNotNull(goal);
//        assertThat(goal.getId(),is(equalTo(GOAL_ID)));

        Activity activity = new Activity();
        activity.setName("ACTIVITY2 TEST");
        activity.setDescription("DESCRIPTION ACTIVITY2 TEST");
        entityManager.persistAndFlush(activity);

        dailyGoal.get().getGoals().put(activity,goal);

        entityManager.flush();
        assertThat(dailyGoalRepository.count(), is(greaterThan((long)1)));
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
        Query query = entityManager.getEntityManager().createQuery("Select new hello.tracker.v5.GoalReport(g.minutes, e.minutes, e.activity.name) " +
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

    @Autowired private UserRepository userRepository;

    private String ACTIVITY_NAME_EXAMPLE1 = "A SAMPLE ACTIVITY";

    private String USER_NAME_EXAMPLE = "A SAMPLE USER";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Goal goal = new Goal();
        goal.setMinutes(45);
        goalRepository.save(goal);

        Activity activity = new Activity();
        activity.setName(ACTIVITY_NAME_EXAMPLE1);
        activity.setDescription(ACTIVITY_NAME_EXAMPLE1);
        activityRepository.save(activity);

        User user = new User();
        user.setUsername(USER_NAME_EXAMPLE);
        userRepository.save(user);
        entityManager.flush();


        DailyGoal.Id id = new DailyGoal.Id(USER_ID,DayOfWeek.FRIDAY);
        DailyGoal dailyGoal = new DailyGoal();
        dailyGoal.setId(id);
        dailyGoal.getGoals().put(activity,goal);
        entityManager.persist(dailyGoal);

        LOG.debug("****************************");
    }
}
