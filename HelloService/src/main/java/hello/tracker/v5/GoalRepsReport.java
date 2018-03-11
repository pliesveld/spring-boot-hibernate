package hello.tracker.v5;

import lombok.Data;

@Data
class GoalRepsReport<T extends Number> {
    private T goalReps;
    private T exerciseReps;
    private String exerciseActivity;

    public GoalRepsReport(T goalReps, T exerciseReps, String exerciseActivity) {
        this.goalReps = goalReps;
        this.exerciseReps = exerciseReps;
        this.exerciseActivity = exerciseActivity;
    }
}
