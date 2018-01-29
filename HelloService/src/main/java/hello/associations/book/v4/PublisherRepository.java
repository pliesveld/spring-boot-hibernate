package hello.associations.book.v4;

import org.springframework.data.repository.PagingAndSortingRepository;


interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long> {
}
