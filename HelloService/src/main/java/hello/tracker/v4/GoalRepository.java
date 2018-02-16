package hello.tracker.v4;

import org.springframework.data.repository.PagingAndSortingRepository;

interface GoalRepository extends PagingAndSortingRepository<Goal, Long> {}
