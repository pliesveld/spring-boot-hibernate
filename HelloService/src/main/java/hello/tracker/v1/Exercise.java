package hello.tracker.v1;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Range(min= 1, max = 120)
    private int minutes;

    @NotNull
    private String activity;

    @ManyToOne
    private Goal goal;
}
