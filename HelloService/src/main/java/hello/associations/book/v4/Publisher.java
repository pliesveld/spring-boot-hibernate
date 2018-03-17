package hello.associations.book.v4;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(schema = "BOOK")
@Data
class Publisher {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "PUBLISHER_ID")
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @OneToMany(mappedBy = "publisher")
    private Collection<Book> books = new ArrayList<>();
}
