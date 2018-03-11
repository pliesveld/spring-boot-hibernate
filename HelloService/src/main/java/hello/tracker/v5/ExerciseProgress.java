package hello.tracker.v5;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "TRACKER")
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class ExerciseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONDITION_ID")
    private Long id;

    @NotNull
    @OneToOne(mappedBy = "exerciseProgress")
    private Exercise exercise;
}
