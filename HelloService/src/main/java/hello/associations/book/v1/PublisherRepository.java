package hello.associations.book.v1;

import org.springframework.data.repository.PagingAndSortingRepository;


interface BookRepository extends PagingAndSortingRepository<Book, Long> {
}
