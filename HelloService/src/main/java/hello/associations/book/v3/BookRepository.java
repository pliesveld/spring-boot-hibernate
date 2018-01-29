package hello.associations.book.v3;

import org.springframework.data.repository.PagingAndSortingRepository;


interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
    Author findByNameIgnoringCase(String name);
}
