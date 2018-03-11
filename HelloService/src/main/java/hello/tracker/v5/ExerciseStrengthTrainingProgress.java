package hello.tracker.v5;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
class ExerciseStrengthTrainingProgress extends ExerciseProgress {
    private int reps;

    public ExerciseStrengthTrainingProgress() {
    }

    public ExerciseStrengthTrainingProgress(int reps) {
        this.reps = reps;
    }
}
