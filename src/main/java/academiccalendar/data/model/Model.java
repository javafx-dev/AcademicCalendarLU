package academiccalendar.data.model;

/**
 *
 * @author Karis
 */
public class Model {
    private static final  Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }
    
    // for adding/editing events
    public int eventDay;
    public int eventMonth;
    public int eventYear;

    // for the year and month the user has open, is "viewing"
    public int viewingMonth;
    public int viewingYear;
    

    // for the current calendar being worked on
    public Long calendarId;

    
    // for editing rules
    public int ruleDays;
    public String ruleTerm;
    public String ruleDescript;
    
    // for editing terms
    public String termName;
    public String termDate;

}
