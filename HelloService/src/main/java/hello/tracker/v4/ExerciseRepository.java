package hello.tracker.v4;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {}
