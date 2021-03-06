package academiccalendar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TERMS")
public class DbTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;

    @ManyToOne
    private DbColorGroup colorGroup;

    @Column
    private Date startDate;

    @OneToMany(mappedBy = "term")
    private List<DbEvent> events;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DbColorGroup getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(DbColorGroup colorGroup) {
        this.colorGroup = colorGroup;
    }

    public List<DbEvent> getEvents() {
        return events;
    }

    public void setEvents(List<DbEvent> events) {
        this.events = events;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
