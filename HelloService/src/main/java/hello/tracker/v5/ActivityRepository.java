package hello.tracker.v5;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ActivityRepository extends PagingAndSortingRepository<Activity, Long> {}
