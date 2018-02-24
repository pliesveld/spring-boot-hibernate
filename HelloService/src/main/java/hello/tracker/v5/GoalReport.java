package hello.tracker.v5;

import lombok.Data;

@Data
class GoalReport {
    private int goalMinutes;
    private int exerciseMinutes;
    private String exerciseActivity;

    public GoalReport(int goalMinutes, int exerciseMinutes, String exerciseActivity) {
        this.goalMinutes = goalMinutes;
        this.exerciseMinutes = exerciseMinutes;
        this.exerciseActivity = exerciseActivity;
    }
}
