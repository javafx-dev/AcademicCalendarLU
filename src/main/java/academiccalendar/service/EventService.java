package academiccalendar.service;

import academiccalendar.model.DbEvent;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    public List<DbEvent> findEventsByCalendarId(Long calendarId) {
        return eventRepository.findByCalendarId(calendarId);
    }

    public List<DbEvent> findEventsByCalendarIdAndYearAndMonth(Long calendarId, int year, int month) {
        return eventRepository.findByCalendarIdAndYearAndMonth(calendarId, year, month);
    }

    public void save(DbEvent event) {
        eventRepository.save(event);
    }

    public void delete(DbEvent event) {
        eventRepository.delete(event);
    }


}
