package hello.associations.book.v4;

import org.springframework.data.repository.PagingAndSortingRepository;

interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Book findByTitleIgnoringCase(String name);
}
