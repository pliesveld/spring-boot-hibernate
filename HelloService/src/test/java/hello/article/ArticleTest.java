package hello.article;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import hello.BaseDataLoader;
import hello.article.model.Article;
import hello.article.model.Tag;
import hello.article.repository.ArticleRepository;
import hello.article.repository.TagRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticleConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({ArticleDataLoader.class})
public class ArticleTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @After
    public void tearDown() {
        entityManager.flush();
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);
        assertNotNull(tagRepository);
        assertNotNull(articleRepository);
    }

    @Test
    public void persistArticle() throws Exception {
        Article article = new Article();
        article.setBody("This is a TESTBODY");
        article.setTitle("This is a TESTTITLE");
        Article article2 = articleRepository.save(article);
        assertThat(article2.getBody(), containsString("TESTBODY"));
        assertThat(article2.getTitle(), containsString("TESTTITLE"));
    }

    @Test
    public void persistTag() throws Exception {
        Tag tag = new Tag();
        tag.setValue("this is a TESTVALUE value");
        Tag tag2 = tagRepository.save(tag);
        assertThat(tag2.getValue(), containsString("TESTVALUE"));
    }

    @Test
    public void persistArticleTagLink() throws Exception {
        Tag tag = tagRepository.findAll().iterator().next();
        Article article = articleRepository.findAll().iterator().next();

        tag.setArticles(Collections.singletonList(article));
        article.setTags(Collections.singletonList(tag));

        entityManager.persist(tag);
        entityManager.persist(article);
        entityManager.flush();
    }
}


@Component
@Transactional
class ArticleDataLoader extends BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger();

    @Autowired TagRepository tagRepository;

    @Autowired ArticleRepository articleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Article article = new Article();
        article.setBody("This is a TESTBODY");
        article.setTitle("This is a TESTTITLE");
        Article article2 = articleRepository.save(article);
        assertThat(article2.getBody(), containsString("TESTBODY"));
        assertThat(article2.getTitle(), containsString("TESTTITLE"));

        Tag tag = new Tag();
        tag.setValue("this is a TESTVALUE value");
        Tag tag2 = tagRepository.save(tag);
        assertThat(tag2.getValue(), containsString("TESTVALUE"));
        entityManager.flush();
    }
}
