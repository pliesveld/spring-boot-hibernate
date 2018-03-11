package hello.tracker.v5;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "TRACKER")
@Data
class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXERCISE_ID")
    private Long id;

    @NotNull
    @JoinColumn(name = "PROGRESS_ID", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private ExerciseProgress exerciseProgress;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID", nullable = false)
    private Activity activity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "GOAL_ID", nullable = false)
    private DailyGoal dailyGoal;
}
