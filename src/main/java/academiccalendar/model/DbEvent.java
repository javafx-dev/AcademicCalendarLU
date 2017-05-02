package academiccalendar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "EVENTS")
public class DbEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String description;

    //TODO change to Date time as JPA doesn't support new Time API
    @Column
    private LocalDate date;

    @ManyToOne
    private DbTerm term;

    @ManyToOne
    private DbCalendar calendar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public DbTerm getTerm() {
        return term;
    }

    public void setTerm(DbTerm term) {
        this.term = term;
    }

    public DbCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(DbCalendar calendar) {
        this.calendar = calendar;
    }
}
