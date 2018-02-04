package hello.associations.book.v5;


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

import com.google.common.collect.Sets;
import hello.BaseDataLoader;
import hello.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static hello.associations.book.v5.BookTest.BOOK_TITLE;
import static hello.associations.book.v5.BookTest.PUBLISHER_NAME;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({BookDataLoader.class})
public class BookTest extends BaseTest {

    final static private Logger LOG = LogManager.getLogger();

    @Autowired private TestEntityManager entityManager;


    @Autowired private PublisherRepository publisherRepository;

    static final String PUBLISHER_NAME = "a SAMPLE PUBLISHER";

    static final String BOOK_TITLE = "aFirstBook";

    @After
    public void tearDown() {
        entityManager.flush();
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);
        assertNotNull(publisherRepository);
    }

    @Test
    public void addsExistingBookToExistingPublisher() throws Exception {
        Book book = new Book();
        book.setTitle(BOOK_TITLE);
        Publisher publisher = publisherRepository.findAll().iterator().next();
        publisher.getBooks().add(book);
    }

    @Test
    public void addsNewBookToExistingPublisher() throws Exception {
        Book book = new Book();
        book.setTitle("aThirdBook");
        Publisher publisher = publisherRepository.findAll().iterator().next();
        publisher.getBooks().add(book);
    }

    @Test
    public void canFindPublisherByBook() throws Exception {
        Publisher publisher = publisherRepository.findByBooksTitleEqualsIgnoreCase(BOOK_TITLE);
        assertNotNull(publisher);
        assertThat(publisher.getName(), startsWith(PUBLISHER_NAME));
    }
}

@Component
@Transactional
class BookDataLoader extends BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @Autowired private PublisherRepository publisherRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Book book = new Book();
        book.setTitle(BOOK_TITLE);

        Book book2 = new Book();
        book2.setTitle("aSecondBook");

        Publisher publisher = new Publisher();
        publisher.setName(PUBLISHER_NAME);
        publisher.setBooks(Sets.newHashSet(book, book2));
        publisherRepository.save(publisher);
        entityManager.flush();
        LOG.debug("****************************");
    }
}
