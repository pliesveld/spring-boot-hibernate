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

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Optional;

import hello.BaseDataLoader;
import hello.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Autowired private DailyGoalRepository dailyGoalRepository;

    @Autowired private ActivityRepository activityRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private DailyAgendaRepository dailyAgendaRepository;

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

    private DailyGoal getGoal() {
        return entityManager.getEntityManager().getReference(DailyGoal.class, GOAL_ID);
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

        assertTrue(dailyGoalRepository.existsById(GOAL_ID));
        assertTrue(activityRepository.existsById(ACTIVITY_ID));
        assertTrue(userRepository.existsById(USER_ID));

        Optional<DailyGoal> goal = dailyGoalRepository.findById(GOAL_ID);
        Optional<Activity> activity = activityRepository.findById(ACTIVITY_ID);
        Optional<User> user = userRepository.findById(USER_ID);

        assertTrue(goal.isPresent());
        assertTrue(activity.isPresent());
        assertTrue(user.isPresent());
    }

    @Test
    public void loadGoalID1() throws Exception {
        DailyGoal dailyGoal = getGoal();
        assertNotNull(dailyGoal);
        assertThat(dailyGoal.getExercises(), IsIterableWithSize.iterableWithSize(0));
        assertThat(dailyGoal.getId(), is(equalTo(GOAL_ID)));
    }

    @Test
    public void saveDailyGoal() throws Exception {
        DailyGoal dailyGoal = getGoal();
        Activity activity = getActivity();
        DayOfWeek weekday = DayOfWeek.SUNDAY;

        DailyAgenda.Id id = new DailyAgenda.Id(USER_ID, weekday);

        DailyAgenda dailyAgenda = new DailyAgenda();
        dailyAgenda.setId(id);
        dailyAgenda.getGoals().put(activity, dailyGoal);
        entityManager.persist(dailyAgenda);
    }

    @Test
    public void saveNewActivityToExistingDailyGoal() throws Exception {

        this.saveDailyGoal();
        entityManager.getEntityManager().flush();
        entityManager.getEntityManager().clear();

        DailyGoal goal = getGoal();
        DailyAgenda.Id id = new DailyAgenda.Id(USER_ID, DayOfWeek.SUNDAY);
        Optional<DailyAgenda> dailyGoal = dailyAgendaRepository.findById(id);

        assertTrue(dailyGoal.isPresent());

        Activity activity = new Activity();
        activity.setName("ACTIVITY2 TEST");
        activity.setDescription("DESCRIPTION ACTIVITY2 TEST");
        entityManager.persistAndFlush(activity);

        dailyGoal.get().getGoals().put(activity,goal);

        entityManager.flush();
        assertThat(dailyAgendaRepository.count(), is(greaterThan((long)1)));
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

        DailyGoal dailyGoal = getGoal();
        Exercise exercise = new Exercise();
        exercise.setActivity(getActivity());
        exercise.setMinutes(30);
        exercise.setDailyGoal(dailyGoal);
        exerciseRepository.save(exercise);
    }

    @Test
    public void savesNewGoalStrengthTraining() throws Exception {
        DailyGoal dailyGoal = new DailyGoal();
        dailyGoal.addCriterion(new GoalStrengthTrainingCriterion(15));
        dailyGoalRepository.save(dailyGoal);
    }

    @Test
    public void savesDurationExerciseAgainst() throws Exception {
        DailyGoal dailyGoal = getGoal();
        Exercise exercise = new Exercise();
        exercise.setMinutes(15);
        exercise.setDailyGoal(dailyGoal);
        exercise.setActivity(getActivity());
        exerciseRepository.save(exercise);
    }

}

@Component
@Transactional
class TrackerDataLoader extends BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @Autowired private ExerciseRepository exerciseRepository;

    @Autowired private DailyGoalRepository dailyGoalRepository;

    @Autowired private ActivityRepository activityRepository;

    @Autowired private UserRepository userRepository;

    private String ACTIVITY_NAME_EXAMPLE1 = "A SAMPLE ACTIVITY";

    private String USER_NAME_EXAMPLE = "A SAMPLE USER";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        DailyGoal dailyGoal = new DailyGoal();
        dailyGoal.addCriterion(new GoalDurationCriterion(60));

        DailyGoal dailyGoal2 = new DailyGoal();
        dailyGoal2.addCriterion(new GoalStrengthTrainingCriterion(15, 1));
        dailyGoalRepository.saveAll(Arrays.asList(dailyGoal, dailyGoal2));

        Activity activity = new Activity();
        activity.setName(ACTIVITY_NAME_EXAMPLE1);
        activity.setDescription(ACTIVITY_NAME_EXAMPLE1);
        activityRepository.save(activity);

        User user = new User();
        user.setUsername(USER_NAME_EXAMPLE);
        userRepository.save(user);
        entityManager.flush();


        DailyAgenda.Id id = new DailyAgenda.Id(USER_ID,DayOfWeek.FRIDAY);
        DailyAgenda dailyAgenda = new DailyAgenda();
        dailyAgenda.setId(id);
        dailyAgenda.getGoals().put(activity, dailyGoal);
        entityManager.persist(dailyAgenda);

        LOG.debug("****************************");
    }
}
