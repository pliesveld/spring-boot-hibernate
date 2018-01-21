package hello.book.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import hello.book.model.Author;

public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
    Author findByNameIgnoringCase(String name);
}
