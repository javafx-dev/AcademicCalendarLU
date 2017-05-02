package academiccalendar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RULES")
public class DbRule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String description;
    @Column
    private Integer dayFromStart;

    @ManyToOne
    private DbTerm term;

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

    public Integer getDayFromStart() {
        return dayFromStart;
    }

    public void setDayFromStart(Integer dayFromStart) {
        this.dayFromStart = dayFromStart;
    }

    public DbTerm getTerm() {
        return term;
    }

    public void setTerm(DbTerm term) {
        this.term = term;
    }
}
