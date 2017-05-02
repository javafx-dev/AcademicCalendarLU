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

    public List<DbEvent> findEventsByCalendarName(String calendarName) {
        return eventRepository.findByCalendarName(calendarName);
    }

    public List<DbEvent> findEventsByCalendarId(Long calendarId) {
        return eventRepository.findByCalendarId(calendarId);
    }

    public void save(DbEvent event) {
        eventRepository.save(event);
    }

    public void delete(DbEvent event) {
        eventRepository.delete(event);
    }

    public List<DbEvent> findAllByCalendarName(String calendarName) {
        return eventRepository.findByCalendarName(calendarName);
    }
}
