package hello.tracker.v5;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Data;

@Data
@Entity
class GoalDurationCriterion extends GoalCriterion {

    private int minutes;

    public GoalDurationCriterion() {
    }

    public GoalDurationCriterion(int minutes) {
        this.minutes = minutes;
    }
}
