package hello.tracker.v5;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table(schema = "TRACKER")
class GoalStrengthTrainingCriterion extends GoalCriterion implements Comparable<GoalStrengthTrainingCriterion> {

    private int reps;

    private int sets;

    public GoalStrengthTrainingCriterion() {
    }

    public GoalStrengthTrainingCriterion(int reps) {
        this.reps = reps;
        this.sets = 1;
    }

    public GoalStrengthTrainingCriterion(int reps, int sets) {
        this.reps = reps;
        this.sets = sets;
    }

    @Override
    public int compareTo(GoalStrengthTrainingCriterion o) {
        return Integer.compare(this.getTotalRepetitions(), o.getTotalRepetitions());
    }

    @Transient
    public int getTotalRepetitions() {
        return reps * sets;
    }
}
