/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public int event_day;
    public int event_month;
    
    public int calendar_start;
    public int calendar_end;  
    
}
