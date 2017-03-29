/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academiccalendar.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author RodolfoPC
 */
public class DBHandler {
    
    private static DBHandler handler;
    
    private static final String DB_URL = "jdbc:derby:calendarDatabase;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    
    //Arrays that contain the default programs, terms, and event type of the University
    private static String[] eventTypes = {"Academic", "Holiday", "Campus", "Sports"}; 
    private static String[] programs = {"Undergraduate", "Graduate (MBA)", "Online", "Accelerated Program"};
    private static String[] terms = {"FA SEM","SP SEM", "SU SEM", 
                                    "FA I MBA", "FA II MBA", "SP I MBA", "SP II MBA", "SU MBA",
                                    "FA QTR", "WIN QTR", "SP QTR", "SU QTR",
                                    "FA 1st Half", "FA 2nd Half", "SP 1st Half", "SP 2nd Half",
                                    "Campus General", "Campus STC", "Campus BV",
                                    "Holiday"};
    private static String defaultColor = "#000000";  //black is the default color
    
    //Variable that controls whether or not the tables have to be created and populated
    private static boolean tablesAlreadyExist = false;
    
    
    //Constructor
    public DBHandler() {
        
        //call to createConnection method that creates the connection between the database and the Java application
        createConnection();
        
        //checks if tables have been already created by an instantatiation of another object in the program, and if
        //the tables have not being created, then they are created and filled with the correspondent default records
        if (tablesAlreadyExist)
        {
            System.out.println("Tables already exist, so connection was the only thing created and now you are ready to go!");
        }
        else {
            
            createCalendarTable();
            //createDatesTable();  //it is commented out because we may not need this table anymore
            createTermsTable();
            createProgramsTable();
            createEventTypesTable();
            createEventsTable();
        
            insertDefaultValuesIntoTables();
            printAllDefaultRecords();
            tablesAlreadyExist = true;
            System.out.println("the static variable tablesAlreadyExist was changed to true. THEREFORE, NO other table should try to be created");
        }
        
        
    }
    
    //Create Connection between Java Application and the JDBC
    void createConnection() {
    
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //********** Functions that create the tables if they do not exist ********
    
    //Create CALENDARS Table
    void createCalendarTable(){
        
        String TableName = "CALENDARS";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                System.out.println("Table " + TableName + " already exists. Ready to go!");
            }
            else {
                String query1 = "CREATE TABLE " + TableName + "("
                        + "CalendarName varchar(200) primary key not null,\n"
                        + "StartYear integer,\n"
                        + "EndYear integer"
                        //+ "EndYear integer,\n"
                        //+ "constraint " + TableName + "_PK primary key ('calendarName'),\n"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    }
    
    //Create DATES Table
    void createDatesTable(){
        
        String TableName = "DATES";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                System.out.println("Table " + TableName + " already exists. Ready to go!");
            }
            else {
                String query1 = "CREATE TABLE " + TableName + "("
                        + "DateInCalendar date primary key not null,\n"
                        + "DayOfWeek varchar(10),\n"
                        + "CalendarName varchar(200),\n"
                        + "constraint " + TableName + "_FK foreign key (CalendarName) references CALENDARS(CalendarName)"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    }
    
    
    //Create TERMS Table
    void createTermsTable(){
        
        String TableName = "TERMS";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                System.out.println("Table " + TableName + " already exists. Ready to go!");
            }
            else {
                String query1 = "CREATE TABLE " + TableName + "("
                        + "TermID integer primary key not null,\n"
                        + "TermName varchar(20),\n"
                        + "TermColor varchar(9)"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    }
    
    //Create PROGRAMS Table
    void createProgramsTable(){
        
        String TableName = "PROGRAMS";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                System.out.println("Table " + TableName + " already exists. Ready to go!");
            }
            else {
                String query1 = "CREATE TABLE " + TableName + "("
                        + "ProgramID integer primary key not null,\n"
                        + "ProgramName varchar(100)"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    }
    
    
    //Create EVENT_TYPES Table
    void createEventTypesTable(){
        
        String TableName = "EVENT_TYPES";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                System.out.println("Table " + TableName + " already exists. Ready to go!");
            }
            else {
                String query1 = "CREATE TABLE " + TableName + "("
                        + "EventTypeID integer primary key not null,\n"
                        + "EventTypeName varchar(100)"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    }
    
    
    //Create EVENTS Table
    void createEventsTable(){
        
        String TableName = "EVENTS";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                System.out.println("Table " + TableName + " already exists. Ready to go!");
            }
            else {
                String query1 = "CREATE TABLE " + TableName + "("
                        + "EventTypeID integer not null,\n"
                        + "TermID integer not null,\n"
                        + "ProgramID integer not null,\n"
                        + "EventDescription varchar(200) not null,\n"
                        + "EventDate date not null,\n"
                        + "CalendarName varchar(200) not null,\n"
                        + "constraint " + TableName + "_PK primary key(EventTypeID, TermID, ProgramID, EventDescription, EventDate, CalendarName),\n"
                        + "constraint " + TableName + "_FK1 foreign key (EventTypeID) references EVENT_TYPES(EventTypeID),\n"
                        + "constraint " + TableName + "_FK2 foreign key (TermID) references TERMS(TermID),\n"
                        + "constraint " + TableName + "_FK3 foreign key (ProgramID) references PROGRAMS(ProgramID),\n"
                        + "constraint " + TableName + "_FK4 foreign key (CalendarName) references CALENDARS(CalendarName)"
                        //+ "constraint " + TableName + "_FK4 foreign key (EventDate) references DATES(DateInCalendar)"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    } 
    
    //************************************************************************
    //************************************************************************
    
    boolean checkIfTableIsEmpty(String tableName) {
        boolean checkingResult = false;
        try {
            stmt = conn.createStatement();
            
            ResultSet res = stmt.executeQuery("SELECT * FROM " + tableName);
            while (res.next()){
                checkingResult = true;
                break;
            }    
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- checking Table failed/error");
            return false;
        } finally {
        }
        return checkingResult;
    }
    
    //Populate the tables TERMS, PROGRAMS, and EVENT_TYPES in the database with default values
    void insertDefaultValuesIntoTables() {
        
        String TableName = "EVENT_TYPES";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                
                boolean dataExistsInTable = checkIfTableIsEmpty(TableName);
                if (!dataExistsInTable)
                {
                    int id = 1;
                    for(int i=0; i < eventTypes.length; i++)
                    {
                        String query2 = "INSERT INTO " + TableName + " VALUES(" + id + ", '" + eventTypes[i]+ "')";
                        stmt.execute(query2);
                        id++;
                    }
                    System.out.println("Default values SUCCESSFULLY inserted Table " + TableName + "!!!");
                }
                else {
                    System.out.println("Default values already exist in Table " + TableName);
                }
                
            }
            else {
                System.out.println("Table " + TableName + " does not exist");
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
        
        
        TableName = "PROGRAMS";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                boolean dataExistsInTable = checkIfTableIsEmpty(TableName);
                if (!dataExistsInTable)
                {
                    int id = 1;
                    for(int i=0; i < programs.length; i++)
                    {
                        String query2 = "INSERT INTO " + TableName + " VALUES(" + id + ", '" + programs[i]+ "')";
                        stmt.execute(query2);
                        id++;
                    }
                    System.out.println("Default values SUCCESSFULLY inserted Table " + TableName + "!!!");
                }
                else {
                    System.out.println("Default values already exist in Table " + TableName);
                }
            }
            else {
                System.out.println("Table " + TableName + " does not exist");
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
        
        TableName = "TERMS";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                
                boolean dataExistsInTable = checkIfTableIsEmpty(TableName);
                if (!dataExistsInTable)
                {
                    int id = 1;
                    for(int i=0; i < terms.length; i++)
                    {
                        String query2 = "INSERT INTO " + TableName + " VALUES(" + id + ", '" + terms[i]+ "', '" + defaultColor +"')";
                        stmt.execute(query2);
                        id++;
                    }
                    System.out.println("Default values SUCCESSFULLY inserted Table " + TableName + "!!!");
                }
                else {
                    System.out.println("Default values already exist in Table " + TableName);
                }
                
            }
            else {
                System.out.println("Table " + TableName + " does not exist");
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
        
        
        
        TableName = "CALENDARS";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                
                boolean dataExistsInTable = checkIfTableIsEmpty(TableName);
                if (!dataExistsInTable)
                {
                    /*
                    int id = 1;
                    for(int i=0; i < terms.length; i++)
                    {
                        String query2 = "INSERT INTO " + TableName + " VALUES(" + id + ", '" + terms[i]+ "', '" + defaultColor +"')";
                        stmt.execute(query2);
                        id++;
                    }
                    */
                    String query2 = "INSERT INTO " + TableName + " VALUES('Test Name', 2017, 2018)";
                    stmt.execute(query2);
                    System.out.println("Default values SUCCESSFULLY inserted Table " + TableName + "!!!");
                }
                else {
                    System.out.println("Default values already exist in Table " + TableName);
                }
                
            }
            else {
                System.out.println("Table " + TableName + " does not exist");
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
        
           
    }
    
    
    
    void printAllDefaultRecords(){
        try {
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM EVENT_TYPES");
            System.out.println("----------------------------------------");
            System.out.println("Table EVENT_TYPES default records:");
            while (res.next()){
                System.out.println(res.getString("EventTypeID") + " - " + res.getString("EventTypeName"));
            }
            
            System.out.println("----------------------------------------");
            System.out.println("----------------------------------------");
            System.out.println("Table PROGRAMS default records:");
            res = stmt.executeQuery("SELECT * FROM PROGRAMS");
            while (res.next()){
                System.out.println(res.getString("ProgramID") + " - " + res.getString("ProgramName"));
            }
            
            System.out.println("----------------------------------------");
            System.out.println("----------------------------------------");
            System.out.println("Table TERMS default records:");
            res = stmt.executeQuery("SELECT * FROM TERMS");
            while (res.next()){
                System.out.println(res.getString("TermID") + " - " + res.getString("TermName") + ", color: " + res.getString("TermColor"));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- Error when printing");
        } finally {
        }
    }
    
    
    
    
    
    
    public ResultSet executeQuery(String query) {
        ResultSet result;
        
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at executeQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        
        return result;
    }
    
    public boolean executeAction(String query2) {
        try {
            stmt = conn.createStatement();
            stmt.execute(query2);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at executeQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {
        }
    }
    
    
    
    
    
    
    
}
