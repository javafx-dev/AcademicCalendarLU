package academiccalendar.data.model;

/**
 *
 * @author Karis
 */
public class Model {
    private final static Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }
    
    // for adding/editing events
    public int event_day;
    public int event_month;
    public int event_year;

    // for the year and month the user has open, is "viewing"
    public int viewing_month;
    public int viewing_year;
    

    // for the current calendar being worked on
    public Long calendar_id;

    
    // for editing rules
    public int rule_days;
    public String rule_term;
    public String rule_descript;
    
    // for editing terms
    public String term_name;
    public String term_date;    

}
