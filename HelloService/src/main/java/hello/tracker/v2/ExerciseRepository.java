package hello.tracker.v2;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {}
