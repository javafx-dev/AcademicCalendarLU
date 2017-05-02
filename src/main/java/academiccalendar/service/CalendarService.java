package academiccalendar.service;

import academiccalendar.model.DbCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EventRepository eventRepository;

    public void save(DbCalendar dbCalendar) {
        calendarRepository.save(dbCalendar);
    }

    public DbCalendar findOne(Long calendarId) {
        return calendarRepository.findOne(calendarId);
    }

    public Iterable<DbCalendar> findAll() {
        return calendarRepository.findAll();
    }

    @Transactional
    public void delete(DbCalendar calendar) {
        eventRepository.deleteByCalendarId(calendar.getId());
        calendarRepository.delete(calendar);
    }
}
