package hello.tracker.v5;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "TRACKER")
@Data
class ExerciseDurationProgress extends ExerciseProgress {
    private int minutes;

    public ExerciseDurationProgress() {
    }

    public ExerciseDurationProgress(int minutes) {
        this.minutes = minutes;
    }
}
