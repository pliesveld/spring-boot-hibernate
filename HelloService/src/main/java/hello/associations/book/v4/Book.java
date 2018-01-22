package hello.associations.book.v4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
class Book {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "BOOK_ID")
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @OneToMany
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "BOOK_ID")
    private Collection<Author> authors = new ArrayList<>();
}
