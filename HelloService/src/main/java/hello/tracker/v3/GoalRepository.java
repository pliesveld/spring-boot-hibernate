package hello.tracker.v3;

import org.springframework.data.repository.PagingAndSortingRepository;

interface GoalRepository extends PagingAndSortingRepository<Goal, Long> {}
