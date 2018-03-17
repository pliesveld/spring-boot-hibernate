package hello.tracker.v5;

import org.springframework.data.repository.PagingAndSortingRepository;

interface DailyGoalRepository extends PagingAndSortingRepository<DailyGoal, Long> {}
