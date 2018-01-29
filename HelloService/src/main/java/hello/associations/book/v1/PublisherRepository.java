package hello.associations.book.v1;

import org.springframework.data.repository.PagingAndSortingRepository;


interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long> {
}
