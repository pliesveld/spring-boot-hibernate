package hello.tracker.v5;

import lombok.Data;

@Data
class GoalReport<T extends java.lang.Number> {
    private T goalMinutes;
    private T exerciseMinutes;
    private String exerciseActivity;

    public GoalReport(T goalMinutes, T exerciseMinutes, String exerciseActivity) {
        this.goalMinutes = goalMinutes;
        this.exerciseMinutes = exerciseMinutes;
        this.exerciseActivity = exerciseActivity;
    }
}
