package hello.tracker.v1;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface GoalRepository extends PagingAndSortingRepository<Goal, Long> {}
