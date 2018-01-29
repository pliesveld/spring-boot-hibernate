package hello.associations.book.v2;

import org.springframework.data.repository.PagingAndSortingRepository;


interface BookRepository extends PagingAndSortingRepository<Book, Long> {
}
