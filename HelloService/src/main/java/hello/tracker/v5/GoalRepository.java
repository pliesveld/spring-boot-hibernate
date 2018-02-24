package hello.tracker.v5;

import org.springframework.data.repository.PagingAndSortingRepository;

interface GoalRepository extends PagingAndSortingRepository<Goal, Long> {}
