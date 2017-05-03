package academiccalendar.service;

import academiccalendar.model.DbEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface EventRepository extends CrudRepository<DbEvent, String>{
    void deleteByTermId(Long id);

    List<DbEvent> findByCalendarId(Long calendarId);

    void deleteByCalendarId(Long calendarId);

    @Query("SELECT p FROM DbEvent p WHERE p.calendar.id = :calendarId AND YEAR(p.date) = (:year) AND MONTH(p.date) = (:month)")
    List<DbEvent> findByCalendarIdAndYearAndMonth(@Param("calendarId") Long calendarId, @Param("year") int year, @Param("month")int month);
}
