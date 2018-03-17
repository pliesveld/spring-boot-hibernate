package hello.tracker.v5;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import hello.BaseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrackerConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({TrackerDataLoader.class})
public class GoalReportTest extends BaseTest {

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

    static final long GOAL_ID_DURATION = 1;

    static final long GOAL_ID_STRENGTH_TRAINING = 2;

    static final long ACTIVITY_ID = 1;

    static final long USER_ID = 1;

    @After
    public void tearDown() {
        entityManager.flush();
    }

    private DailyGoal getDurationGoal() {
        return entityManager.getEntityManager().getReference(DailyGoal.class, GOAL_ID_DURATION);
    }

    private DailyGoal getStrengthTrainingGoal() {
        return entityManager.getEntityManager().getReference(DailyGoal.class, GOAL_ID_STRENGTH_TRAINING);
    }

    private Activity getActivity() {
        return entityManager.getEntityManager().getReference(Activity.class, ACTIVITY_ID);
    }

    private User getUser() {
        return entityManager.getEntityManager().getReference(User.class, USER_ID);
    }

    @Test
    public void testReportHQL() {
        entityManager.getEntityManager().createQuery("FROM User");
    }

    @Test
    public void saveExerciseDurationProgress() throws Exception {
        DailyGoal dailyGoal = getDurationGoal();
        Exercise exercise = new Exercise();
        exercise.setActivity(getActivity());
        ExerciseDurationProgress exerciseDurationProgress = new ExerciseDurationProgress(15);
        exerciseDurationProgress.setExercise(exercise);
        exercise.setExerciseProgress(exerciseDurationProgress);
        exercise.setDailyGoal(dailyGoal);
        exerciseRepository.save(exercise);
    }

    @Test
    public void saveExerciseStrengthTrainingProgress() throws Exception {
        DailyGoal dailyGoal = getStrengthTrainingGoal();
        Exercise exercise = new Exercise();
        exercise.setActivity(getActivity());
        ExerciseStrengthTrainingProgress exerciseDurationProgress = new ExerciseStrengthTrainingProgress(15);
        exerciseDurationProgress.setExercise(exercise);
        exercise.setExerciseProgress(exerciseDurationProgress);
        exercise.setDailyGoal(dailyGoal);
        exerciseRepository.save(exercise);
    }

    @Test
    public void makeGoalReportById() throws Exception {
        this.saveExerciseDurationProgress();
        this.saveExerciseDurationProgress();

        entityManager.flush();
        Query query = entityManager.getEntityManager()
            .createQuery(
       "Select new hello.tracker.v5.GoalDurationReport(gdc.minutes, e.exerciseProgress.minutes, e.activity.name) " +
                "from DailyGoal dg, Exercise e, GoalDurationCriterion gdc " +
                "where dg.id = e.dailyGoal.id and gdc.id = dg.goalCriterion.id");

        List<GoalDurationReport> results = query.getResultList();
        assertNotNull(results);
        assertThat(results, Matchers.iterableWithSize(2));
        results.forEach(LOG::debug);
    }

    @Test
    public void testStrengthTraining_goalReport() throws Exception {
        this.saveExerciseStrengthTrainingProgress();
        this.saveExerciseStrengthTrainingProgress();
        this.saveExerciseStrengthTrainingProgress();

        entityManager.flush();
        Query query = entityManager.getEntityManager()
            .createQuery(
       "Select new hello.tracker.v5.GoalRepsReport(gstc.reps, e.exerciseProgress.reps, e.activity.name) " +
                "from DailyGoal dg, Exercise e, GoalStrengthTrainingCriterion gstc " +
                "where dg.id = e.dailyGoal.id and gstc.id = dg.goalCriterion.id");

        List<GoalRepsReport> results = query.getResultList();
        assertNotNull(results);
        assertThat(results, Matchers.iterableWithSize(3));
        results.forEach(LOG::debug);
    }


}

