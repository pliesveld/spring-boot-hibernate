package hello.tracker.v3;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {}
