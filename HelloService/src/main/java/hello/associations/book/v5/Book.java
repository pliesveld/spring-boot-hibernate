package hello.associations.book.v5;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Embeddable
@Data
class Book {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "BOOK_TITLE", nullable = true)
    private String title;
}
