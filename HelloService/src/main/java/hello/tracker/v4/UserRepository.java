package hello.tracker.v4;

import org.springframework.data.repository.PagingAndSortingRepository;

interface UserRepository extends PagingAndSortingRepository<User, Long> {}
