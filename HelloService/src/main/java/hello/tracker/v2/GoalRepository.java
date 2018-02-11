package hello.tracker.v2;

import org.springframework.data.repository.PagingAndSortingRepository;

interface GoalRepository extends PagingAndSortingRepository<Goal, Long> {}
