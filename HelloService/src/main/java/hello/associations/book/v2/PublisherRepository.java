package hello.associations.book.v2;

import org.springframework.data.repository.PagingAndSortingRepository;


interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long> {
}
