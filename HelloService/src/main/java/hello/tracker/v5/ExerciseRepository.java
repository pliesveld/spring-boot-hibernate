package hello.tracker.v5;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {}
