package hello.tracker.v1;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {}
