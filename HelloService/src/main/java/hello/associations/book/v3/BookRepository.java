package hello.associations.book.v3;

import org.springframework.data.repository.PagingAndSortingRepository;


interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Book findByNameIgnoringCase(String name);
}
