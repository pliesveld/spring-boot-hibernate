package hello.associations.book.v2;


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

import com.google.common.collect.Sets;
import hello.BaseDataLoader;
import hello.BaseTest;
import net.bytebuddy.matcher.StringMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static hello.associations.book.v2.BookTest.BOOK_TITLE;
import static hello.associations.book.v2.BookTest.PUBLISHER_NAME;
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

    @Before
    public void setUp() throws Exception {
        publisher = entityManager.getEntityManager().getReference(Publisher.class, (long)1);
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
        assertNotNull(loadEventListener);
        assertNotNull(publisher);
        assertNotNull(publisher.getName());
        assertThat(publisher.getName(), hasToString(PUBLISHER_NAME));
        assertNotNull(publisher.getName());
    }

    @Test
    public void addSamePublisherToExistingBook() throws Exception {
        Publisher publisher = publisherRepository.findAll().iterator().next();
        Book book = bookRepository.findAll().iterator().next();
        book.setPublisher(publisher);
        entityManager.persist(book);
    }

    @Test
    public void addNewBookToExistingPublisher() throws Exception {
        Book book = bookRepository.findById((long)3).get();
        assertNotNull(book);
        book.setPublisher(publisher);
    }

    @Test
    public void addNewBookToExistingPublisherByReference() throws Exception {
        Book book = entityManager.getEntityManager().getReference(Book.class, (long)3);
        assertNotNull(book);
        book.setPublisher(publisher);
    }

    @Test
    public void getBooksContainingTwoPublishers() throws Exception {
        this.addNewBookToExistingPublisher();
        entityManager.flush();
        assertThat(bookRepository.count(), comparesEqualTo((long)3));
        assertThat(publisherRepository.count(), comparesEqualTo((long)1));
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
        book.setPublisher(publisher);
        bookRepository.save(book);

        Book book2 = new Book();
        book2.setTitle("aSecondBook");
        book2.setPublisher(publisher);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("aThirdBook");
        bookRepository.save(book3);
        LOG.debug("****************************");
    }
}
