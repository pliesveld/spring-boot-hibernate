package hello.tracker.v5;

import lombok.Data;

@Data
class GoalDurationReport<T extends java.lang.Number> {
    private T goalMinutes;
    private T exerciseMinutes;
    private String exerciseActivity;

    public GoalDurationReport(T goalMinutes, T exerciseMinutes, String exerciseActivity) {
        this.goalMinutes = goalMinutes;
        this.exerciseMinutes = exerciseMinutes;
        this.exerciseActivity = exerciseActivity;
    }
}
