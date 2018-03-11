package hello.tracker.v5;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "TRACKER")
@Data
class ExerciseStrengthTrainingProgress extends ExerciseProgress {
    private int reps;

    public ExerciseStrengthTrainingProgress() {
    }

    public ExerciseStrengthTrainingProgress(int reps) {
        this.reps = reps;
    }
}
