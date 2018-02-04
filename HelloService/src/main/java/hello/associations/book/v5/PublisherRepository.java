package hello.associations.book.v5;

import org.springframework.data.repository.PagingAndSortingRepository;


interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long> {
    Publisher findByNameIgnoringCase(String theSecondPublisher);

    Publisher findByBooksTitleEqualsIgnoreCase(String bookTitle);
}
