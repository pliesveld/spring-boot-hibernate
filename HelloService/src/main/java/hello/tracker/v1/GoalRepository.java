package hello.tracker.v1;

import org.springframework.data.repository.PagingAndSortingRepository;

interface GoalRepository extends PagingAndSortingRepository<Goal, Long> {}
