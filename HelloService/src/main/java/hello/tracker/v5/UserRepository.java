package hello.tracker.v5;

import org.springframework.data.repository.PagingAndSortingRepository;

interface UserRepository extends PagingAndSortingRepository<User, Long> {}
