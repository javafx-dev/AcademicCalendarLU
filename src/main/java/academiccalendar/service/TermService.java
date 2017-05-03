package academiccalendar.service;

import academiccalendar.model.DbColorGroup;
import academiccalendar.model.DbTerm;
import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TermService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TermService.class);
    private static final String[] TERMS = {"FA SEM", "SP SEM", "SU SEM",
            "FA I MBA", "FA II MBA", "SP I MBA", "SP II MBA", "SU MBA",
            "FA QTR", "WIN QTR", "SP QTR", "SU QTR",
            "FA 1st Half", "FA 2nd Half", "SP 1st Half", "SP 2nd Half",
            "Campus General", "Campus STC", "Campus BV",
            "Holiday"};

    private static final String DEFAULT_COLOR = "255-255-255";  //white is the default color

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ColorGroupRepository colorGroupRepository;

    @PostConstruct
    private void init() {
        LOGGER.info("Inserting default terms");
        for (String term : TERMS) {
            if (termRepository.existsByName(term)) {
                LOGGER.info("Term: [{}] already exists", term);
            } else {

                DbColorGroup colorGroup = colorGroupRepository.findByColor(DEFAULT_COLOR);
                if (colorGroup == null) {
                    colorGroup = new DbColorGroup();
                    colorGroup.setColor(DEFAULT_COLOR);
                    colorGroup.setName("Default Group");
                    colorGroupRepository.save(colorGroup);
                }

                DbTerm dbTerm = new DbTerm();
                dbTerm.setName(term);
                dbTerm.setColorGroup(colorGroup);
                dbTerm.setStartDate(new Date());
                termRepository.save(dbTerm);
            }
        }
    }

    public DbTerm findByName(String name) {
        return termRepository.findByName(name);

    }

    public ObservableList<String> getListOfTerms() {
        ObservableList<String> terms = FXCollections.observableArrayList();
        termRepository.findAll().forEach(dbTerm -> terms.add(dbTerm.getName()));
        return terms;
    }

    public DbTerm findById(Long termId) {
        return termRepository.findOne(termId);
    }

    public List<DbTerm> findAll() {
        return Lists.newArrayList(termRepository.findAll());
    }

    @Transactional
    public void deleteByName(String termName) {
        LOGGER.info("Deleting term: {}", termName);
        DbTerm term = termRepository.findByName(termName);
        eventRepository.deleteByTermId(term.getId());
        termRepository.delete(term);
    }

    @Transactional
    public void updateTerm(String termName, LocalDate newTermStartingDate, Long colorGroupId) {
        DbTerm dbTerm = termRepository.findByName(termName);
        ZoneId z = ZoneId.systemDefault();
        ZonedDateTime zdt = newTermStartingDate.atStartOfDay(z);
        Instant instant = zdt.toInstant();
        java.util.Date d = java.util.Date.from(instant);
        dbTerm.setStartDate(d);
        dbTerm.setColorGroup(colorGroupRepository.findOne(colorGroupId));
        termRepository.save(dbTerm);
    }

    public DbTerm findOne(long termID) {
        return termRepository.findOne(termID);
    }
}
