package hello.tracker.v1;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GOAL_ID")
    private Long id;

    @Range(min = 1, max = 120)
    @Column(name = "MINUTES")
    private int minutes;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Exercise> exercises = new ArrayList<>();
}
