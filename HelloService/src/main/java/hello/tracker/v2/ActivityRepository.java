package hello.tracker.v2;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ActivityRepository extends PagingAndSortingRepository<Activity, Long> {}
