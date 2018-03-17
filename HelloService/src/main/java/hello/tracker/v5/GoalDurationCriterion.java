package hello.tracker.v5;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "TRACKER")
class GoalDurationCriterion extends GoalCriterion {

    private int minutes;

    public GoalDurationCriterion() {
    }

    public GoalDurationCriterion(int minutes) {
        this.minutes = minutes;
    }
}
