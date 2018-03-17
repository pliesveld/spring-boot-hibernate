package hello.article.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(schema = "ARTICLE")
@Data
public class Article {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 5000)
    private String body;

    @ManyToMany
    @JoinTable(
            name = "ARTICLE_TAGS",
            joinColumns = @JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID", referencedColumnName = "ID")
    )
    private List<Tag> tags;

}
