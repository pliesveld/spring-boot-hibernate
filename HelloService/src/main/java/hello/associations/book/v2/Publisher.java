package hello.associations.book.v2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
class Book {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;


}
