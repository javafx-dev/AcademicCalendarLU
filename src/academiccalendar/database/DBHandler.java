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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    //private static String[] eventTypes = {"Academic", "Holiday", "Campus", "Sports"}; 
    //private static String[] programs = {"Undergraduate", "Graduate (MBA)", "Online", "Accelerated Program"};
    private static String[] terms = {"FA SEM","SP SEM", "SU SEM", 
                                    "FA I MBA", "FA II MBA", "SP I MBA", "SP II MBA", "SU MBA",
                                    "FA QTR", "WIN QTR", "SP QTR", "SU QTR",
                                    "FA 1st Half", "FA 2nd Half", "SP 1st Half", "SP 2nd Half",
                                    "Campus General", "Campus STC", "Campus BV",
                                    "Holiday"};
    private static String defaultColor = "#000000";  //black is the default color
    
    //Variable that controls whether or not the tables have to be created and populated
    private static boolean tablesAlreadyExist = false;
    
    private static String defaultTermStartDate = "2017-08-28";
    
    
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
            createTermsTable();
            createEventsTable();
            createRulesTable();
        
            insertDefaultValuesIntoTables();
            printAllDefaultRecords();
            tablesAlreadyExist = true;
            System.out.println("the static variable tablesAlreadyExist was changed to true. THEREFORE, NO other table should try to be created");
            
            // the following lines are just here to test the correct functionality of the getListOfTerms method
            try {
                ObservableList<String> auxList = this.getListOfTerms();
                //ArrayList<String> auxList = this.getListOfTerms();
            } catch (SQLException ex) {
                Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // the following lines are just here to test the correct functionality of the getTermID method
            int getIDTest = this.getTermID("FA QTR");
            
            // the following lines are just here to test the correct functionality of the getTermName method
            String nameAux = this.getTermName(2);
            
            // the following lines are just here to test the correct functionality of the getTermStartDate method
            String auxTermDate = this.getTermStartDate(3);
            
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
    
    //********** Functions that create the tables if they do not exist ***************************
    
    //**************************  CALENDARS Table  ***********************************************
    //Function that creates CALENDARS Table
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
                        + "EndYear integer,\n"
                        + "StartDate date"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    }
    
    
    //**************************  TERMS Table  ***********************************************
    //Function that creates TERMS Table
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
                        + "TermColor varchar(9),\n"
                        + "TermStartDate date"
                        + ")";
                stmt.execute(query1);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- setupDatabase");
        } finally {
        }
    }
    
    //**************************  RULES Table  ***********************************************
    //Function that creates RULES Table
    void createRulesTable(){
        
        String TableName = "RULES";
        try {
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet listOfTables = dbm.getTables(null,null, TableName.toUpperCase(), null);
            
            if (listOfTables.next()) {
                System.out.println("Table " + TableName + " already exists. Ready to go!");
            }
            else {
                String query1 = "CREATE TABLE " + TableName + "("
                        + "EventDescription varchar(200) not null,\n"
                        + "TermID integer not null,\n"
                        + "DaysFromStart integer,\n"
                        + "constraint " + TableName + "_PK primary key(EventDescription, TermID)"
                        //+ "constraint " + TableName + "_FK1 foreign key (TermID) references TERMS(TermID),\n"
                        //+ "constraint " + TableName + "_FK2 foreign key (EventDescription) references EVENTS(EventDescription)"
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
                        + "EventDescription varchar(200) not null,\n"
                        + "EventDate date not null,\n"
                        + "TermID integer not null,\n"
                        + "CalendarName varchar(200) not null,\n"
                        + "constraint " + TableName + "_PK primary key(EventDescription, EventDate, TermID, CalendarName),\n"
                        + "constraint " + TableName + "_FK1 foreign key (TermID) references TERMS(TermID),\n"
                        + "constraint " + TableName + "_FK2 foreign key (CalendarName) references CALENDARS(CalendarName)"
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
    
    
    //Function that checks if a table in the database is empty (has no records), and return a boolean values based on the checking result
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
    
    //Populate the tables TERMS with default values
    void insertDefaultValuesIntoTables() {
        
        String TableName = "TERMS";
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
                        String query2 = "INSERT INTO " + TableName + " VALUES(" + id + ", '" + terms[i]+ "', '" + defaultColor +"', '2017-08-28')";
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
        
        
        // The following lines are for testing purposes. They will be deleted later
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
                    String query2 = "INSERT INTO " + TableName + " VALUES('Test Name', 2017, 2018, '2017-08-01')";
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
    
    
    // This function is for testing purposes. Helps the programmer see all the terms in the TERMS Table and all the Rules in the RULES table
    void printAllDefaultRecords(){
        try {
            
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM TERMS");
            System.out.println("----------------------------------------");
            System.out.println("----------------------------------------");
            System.out.println("Table TERMS default records:");
            while (res.next()){
                System.out.println(res.getString("TermID") + " - " + res.getString("TermName") + ", color: " + res.getString("TermColor"));
            }
            
            
            System.out.println("----------------------------------------");
            System.out.println("----------------------------------------");
            System.out.println("Table RULES default records:");
            res = stmt.executeQuery("SELECT * FROM RULES");
            while (res.next()){
                System.out.println("Record has values: " +res.getString("EventDescription") + " - " + res.getString("TermID") + " - " + res.getString("DaysFromStart"));
            }
            
        }
        catch (SQLException e) {
            System.err.println(e.getMessage() + "--- Error when printing");
        } finally {
        }
    }
    
    
    
    
    
    //Function that executes a SELECT query and returns the requested values/data from the database
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
    
    
    //Function that executes an insertion, deletion, or update query
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
    
    
    
    
    
    //Function that return the complete list of terms that exist in the database
    public ObservableList<String> getListOfTerms() throws SQLException {
        
        
        System.out.println("************************************************");
        System.out.println("************************************************");
        System.out.println("************************************************");
        System.out.println("");
        System.out.println("Priinting the observableArrayList of term");
        System.out.println("");
        //ArrayList that will contain all terms saved in the TERMS Tables
        ObservableList<String> listOfTerms = FXCollections.observableArrayList();// = new ObservableList();
        
        //Query that will obtain all available Term Names from the database table TERMS
        String queryListOfTerms = "SELECT TermName FROM TERMS";
        //Variable that will hold the result of executing the previous query
        ResultSet rs = executeQuery(queryListOfTerms);
        
        
        try
        {
            //While there are Term Names in the ResultSet variable, add each one of them to the ArrayList of Strings
           while(rs.next()) 
            {
                //get the term name and store it in a String variable
                String termName = rs.getString("TermName");
                //add Term Name to list of terms
                listOfTerms.add(termName);
            } 
        }
        catch (SQLException e) 
        {
            System.err.println(e.getMessage() + "--- error at getListOfTerms method in DBHandler class");
        }
        
        //********************************************************************************************************
        //*******  These lines of code are to Test we get all terms correctly from databse table TERMS  **********
        
        //Print full array with all Term Names for testing that we get the list we need
        System.out.println("Observable list of terms is the following:  " + listOfTerms);
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        int arrayListSize = listOfTerms.size();
        int idTerm = 1;
        for (int i=0; i < arrayListSize; i++)
        {
            System.out.println("ID: " + idTerm + " --- " + listOfTerms.get(i));
            idTerm++;
        }
        //*******************************************************************************************************
        //*******************************************************************************************************
        
        return listOfTerms;
    }
    
    
            
    //Function that returns the Term ID based on a given term name        
    public int getTermID(String termName) {
        
        int termID = 0;
        String getTermIDQuery = "Select TermID From TERMS WHERE TermName='" + termName + "'";
        ResultSet res = executeQuery(getTermIDQuery);
        
        try
        {
            while(res.next())
            {
                termID = res.getInt("TermID");
            }
        }
        catch (SQLException e) 
        {
            System.err.println(e.getMessage() + "--- error at getTermID method in DBHandler class");
        } 
        
        //test function for correct result
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        System.out.println("Term ID for " + termName + " is: " + termID);
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        
        return termID;
    }
    
    
    public String getTermName(int termIDAux) {
        
        //Declare variable that will contain the name of the term
        String nameOfTerm = "x";
        //Create query that will find a matching result for the termName based on the term's ID
        String getTermNameQuery = "SELECT TermName FROM TERMS "
                                + "WHERE TERMS.TermID=" + termIDAux;
        
        System.out.println("Query to get TermName is: " + getTermNameQuery);
        
        
        ResultSet res = executeQuery(getTermNameQuery);
        
        try
        {
            while(res.next())
            {
                nameOfTerm = res.getString("TermName");
            }
        }
        catch (SQLException e) 
        {
            System.err.println(e.getMessage() + "--- error at getTermID method in DBHandler class");
        } 
        
        //test function for correct result
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        System.out.println("Term Name for " + termIDAux + " is: " + nameOfTerm);
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        
        
        
        return nameOfTerm;
    }
    
    public String getTermStartDate(int auxTermID){
        
        String termStartDate = defaultTermStartDate;
        
        String getTermDateQuery = "SELECT TermStartDate FROM TERMS "
                            + "WHERE TERMS.TermID=" + auxTermID;
        
        System.out.println("*********************************************************");
        System.out.println("Query to get term start date is: " + getTermDateQuery);
        System.out.println("*********************************************************");
        
        ResultSet res = executeQuery(getTermDateQuery);
        
        try
        {
            while(res.next())
            {
                termStartDate = res.getString("TermStartDate");
            }
        }
        catch (SQLException e) 
        {
            System.err.println(e.getMessage() + "--- error at getTermID method in DBHandler class");
        } 
        
        
        System.out.println("*********************************************************");
        System.out.println("The start date of the Term (" + auxTermID + ") is:  " + termStartDate);
        System.out.println("*********************************************************");
        
        
        return termStartDate;
    }
}
