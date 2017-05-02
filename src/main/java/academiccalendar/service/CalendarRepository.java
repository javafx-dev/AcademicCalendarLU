package academiccalendar.service;

import academiccalendar.model.DbCalendar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CalendarRepository extends CrudRepository<DbCalendar, Long>{

    DbCalendar findByName(String calendarName);
}
