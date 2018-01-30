package hello.associations.book.v3;

import org.springframework.data.repository.PagingAndSortingRepository;


interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long> {
    Publisher findByNameIgnoringCase(String theSecondPublisher);
}
