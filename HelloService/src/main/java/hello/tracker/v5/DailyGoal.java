package hello.tracker.v5;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(schema = "TRACKER", name = "DAILY_GOAL")
class DailyGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GOAL_ID")
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONDITON",  nullable = false)
    private GoalCriterion goalCriterion;

    @OneToMany(mappedBy = "dailyGoal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Exercise> exercises = new ArrayList<>();

    public void addCriterion(GoalCriterion goalCriterion) {

        if (goalCriterion == null) {
            throw new IllegalArgumentException("Cannot pass null goal criterion");
        }

        if (goalCriterion.getDailyGoal() != null) {
            throw new IllegalArgumentException("GoalCriterion has already been assigned");
        }

        setGoalCriterion(goalCriterion);
        goalCriterion.setDailyGoal(this);
    }
}
