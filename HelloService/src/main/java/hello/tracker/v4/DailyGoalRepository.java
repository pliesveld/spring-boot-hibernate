package hello.tracker.v4;

import org.springframework.data.repository.PagingAndSortingRepository;

interface DailyGoalRepository extends PagingAndSortingRepository<DailyGoal, DailyGoal.Id> {

}
