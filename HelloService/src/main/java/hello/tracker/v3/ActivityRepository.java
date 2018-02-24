package hello.tracker.v3;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ActivityRepository extends PagingAndSortingRepository<Activity, Long> {}
