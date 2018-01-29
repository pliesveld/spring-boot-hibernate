package hello.associations.book.v1;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
class Publisher {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @OneToMany
    @JoinTable(
            name = "BOOK_PUBLISHER",
            joinColumns = @JoinColumn(name = "PUBLISHER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
    )
    private Set<Book> books = new HashSet<>();
}
