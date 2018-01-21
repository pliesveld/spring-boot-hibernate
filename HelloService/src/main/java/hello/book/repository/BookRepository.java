package hello.book.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import hello.book.model.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
}
