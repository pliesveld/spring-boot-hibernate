package hello.tracker.v5;

import org.springframework.data.repository.PagingAndSortingRepository;

interface DailyAgendaRepository extends PagingAndSortingRepository<DailyAgenda, DailyAgenda.Id> {

}
