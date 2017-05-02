package academiccalendar.service;

import academiccalendar.model.DbEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface EventRepository extends CrudRepository<DbEvent, String>{
    void deleteByTermId(Long id);

    List<DbEvent> findByCalendarName(String calendarName);

    List<DbEvent> findByCalendarId(Long calendarId);

    void deleteByCalendarId(Long calendarId);
}
