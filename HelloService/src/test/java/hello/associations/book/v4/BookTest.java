package hello.associations.book.v4;


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

import java.util.Arrays;
import java.util.Collections;

import com.google.common.collect.Sets;
import hello.BaseDataLoader;
import hello.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static hello.associations.book.v4.BookTest.BOOK_TITLE;
import static hello.associations.book.v4.BookTest.PUBLISHER_NAME;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({BookDataLoader.class})
public class BookTest extends BaseTest {

    final static private Logger LOG = LogManager.getLogger();

    static final String PUBLISHER_NAME = "a SAMPLE PUBLISHER";

    static final String BOOK_TITLE = "aFirstBook";

    @Autowired private TestEntityManager entityManager;

    @Autowired private BookRepository bookRepository;

    @Autowired private PublisherRepository publisherRepository;

    @After
    public void tearDown() {
        entityManager.flush();
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);
        assertNotNull(bookRepository);
        assertNotNull(publisherRepository);
    }

    @Test
    @Transactional
    public void addSamePublisherToExistingBook() throws Exception {
        Publisher publisher = publisherRepository.findAll().iterator().next();
        Book book = bookRepository.findAll().iterator().next();
        publisher.getBooks().add(book);
        entityManager.persist(book);
    }

    @Test
    public void addNewBookToExistingPublisher() throws Exception {
        Publisher publisher = publisherRepository.findByNameIgnoringCase(PUBLISHER_NAME);
        assertNotNull(publisher);
        assertThat(publisher.getName(),containsString("SAMPLE"));
        Book book = bookRepository.findAll().iterator().next();
        publisher.getBooks().add(book);
        entityManager.persist(book);
    }

    @Test
    public void addNewBookToExistingPublisherByReference() throws Exception {
        Publisher publisher = entityManager.getEntityManager().getReference(Publisher.class, (long)1);
        Book book = bookRepository.findById((long)1).get();
        assertNotNull(book);
        publisher.getBooks().add(book);
        book.setPublisher(publisher);
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
