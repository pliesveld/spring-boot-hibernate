package hello.associations.book.v5;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(schema = "BOOK")
@Data
class Publisher {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @ElementCollection
    @CollectionTable(name = "BOOK", joinColumns = @JoinColumn(name = "PUBLISHER_ID"))
    private Set<Book> books = new HashSet<>();
}
