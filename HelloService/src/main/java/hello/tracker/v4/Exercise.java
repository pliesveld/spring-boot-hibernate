package hello.tracker.v4;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @Range(min = 1, max = 120)
    @Column(name = "EXERCISE_MINUTES", nullable = false)
    private int minutes;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID", nullable = false)
    private Activity activity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "GOAL_ID", nullable = false)
    private Goal goal;
}
