package hello.associations.book.v3;


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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static hello.associations.book.v3.BookTest.BOOK_TITLE;
import static hello.associations.book.v3.BookTest.PUBLISHER_NAME;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({BookDataLoader.class})
public class BookTest extends BaseTest {

    final static private Logger LOG = LogManager.getLogger();

    @Autowired private TestEntityManager entityManager;

    @Autowired private BookRepository bookRepository;

    @Autowired private PublisherRepository publisherRepository;

    static final String PUBLISHER_NAME = "a SAMPLE PUBLISHER";

    static final String BOOK_TITLE = "aFirstBook";

    private Publisher publisher;

    private Book book;

    @Before
    public void setUp() throws Exception {
        publisher = entityManager.getEntityManager().getReference(Publisher.class, (long)1);
        book = entityManager.getEntityManager().getReference(Book.class, (long)1);
    }

    @After
    public void tearDown() {
        entityManager.flush();
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);
        assertNotNull(bookRepository);
        assertNotNull(publisherRepository);
        assertNotNull(publisher);
        assertNotNull(book);
    }

    @Test
    public void addSameBookToExistingPublisher() throws Exception {
        assertThat(publisher.getBooks(), iterableWithSize(0));
    }

    @Test
    public void addsAssosicationByBook() throws Exception {
        book.setPublisher(publisher);
    }

    @Test
    public void addsAssosicationByPublisher() throws Exception {
        publisher.getBooks().add(book);
    }

    @Test
    @Ignore
    public void addsAssosicationByBookAndPublisher() throws Exception {
        book.setPublisher(publisher);
        publisher.getBooks().add(book);
    }
}


@Component
@Transactional
class BookDataLoader extends BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @Autowired private BookRepository bookRepository;

    @Autowired private PublisherRepository publisherRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Publisher publisher = new Publisher();
        publisher.setName(PUBLISHER_NAME);
        publisherRepository.save(publisher);

        Book book = new Book();
        book.setTitle(BOOK_TITLE);
        bookRepository.save(book);

        LOG.debug("****************************");
    }
}
