package hello.article.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(schema = "ARTICLE")
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String value;

    @ManyToMany(mappedBy = "tags")
    private List<Article> articles;

}
