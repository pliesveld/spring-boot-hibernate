package hello.associations.book.v3;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
class Author {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @ManyToOne
    private Book book;
}
