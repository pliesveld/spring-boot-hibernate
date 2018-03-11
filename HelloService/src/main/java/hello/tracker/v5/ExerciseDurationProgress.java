package hello.tracker.v5;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
class ExerciseDurationProgress extends ExerciseProgress {
    private int minutes;

    public ExerciseDurationProgress() {
    }

    public ExerciseDurationProgress(int minutes) {
        this.minutes = minutes;
    }
}
