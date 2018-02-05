package hello.tracker.v1;

import lombok.Data;

@Data
public class GoalReport {
    private int goalMinutes;
    private int exerciseMinutes;
    private String exerciseActivity;

    public GoalReport(int goalMinutes, int exerciseMinutes, String exerciseActivity) {
        this.goalMinutes = goalMinutes;
        this.exerciseMinutes = exerciseMinutes;
        this.exerciseActivity = exerciseActivity;
    }
}
