package hello.book;


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
import hello.book.model.Author;
import hello.book.model.Book;
import hello.book.repository.AuthorRepository;
import hello.book.repository.BookRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest(showSql = false)
@Import({BookDataLoader.class})
public class BookTest {

    final static private Logger LOG = LogManager.getLogger();

    @Autowired private TestEntityManager entityManager;

    @Autowired private BookRepository bookRepository;

    @Autowired private AuthorRepository authorRepository;

    @After
    public void tearDown() {
        entityManager.flush();
    }

    @Test
    public void testContextLoad() throws Exception {
        assertNotNull(entityManager);
        assertNotNull(bookRepository);
        assertNotNull(authorRepository);
    }

    @Test
    @Transactional
    public void addSameAuthorToExistingBook() throws Exception {

        Author author = authorRepository.findAll().iterator().next();
        Book book = bookRepository.findAll().iterator().next();
        book.getAuthors().add(author);
        entityManager.persist(book);

    }


    @Test
    public void addNewAuthorToexistingBook() throws Exception {
        Author author = authorRepository.findByNameIgnoringCase("theSecondAuthor");
        assertNotNull(author);
        assertThat(author.getName(),containsString("Second"));
        Book book = bookRepository.findAll().iterator().next();
        book.getAuthors().add(author);
        entityManager.persist(book);
    }

    @Test
    public void addNewAuthorToExistingBookByReference() throws Exception {
        Author author = entityManager.getEntityManager().getReference(Author.class, (long)2);
        Book book = bookRepository.findById((long)1).get();
        assertNotNull(book);
        book.getAuthors().add(author);
    }
}


@Component
@Transactional
class BookDataLoader extends BaseDataLoader implements ApplicationListener<ApplicationReadyEvent> {
    final static private Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @Autowired private BookRepository bookRepository;

    @Autowired private AuthorRepository authorRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Author author = new Author();
        author.setName("theFirstAuthor");
        authorRepository.save(author);

        Book book = new Book();
        book.setTitle("a SAMPLEBOOK");
        book.setAuthors(Collections.singleton(author));
        bookRepository.save(book);

        Author author2 = new Author();
        author2.setName("theSecondAuthor");
        authorRepository.save(author2);


        LOG.debug("****************************");
    }
}