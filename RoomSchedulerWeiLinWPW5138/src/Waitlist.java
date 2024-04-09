/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
/**
 *
 * @author wengw
 */
public class Waitlist {
    
    private static Connection connection;
    private static PreparedStatement addWaitlist;
    private static PreparedStatement getAllWaitlist;
    private static ResultSet resultSet;
    
    public static void addWaitlistEntry(String Faculty, Date date, int seats)
    {
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        
        connection = DBConnection.getConnection();
        try
        {
            addWaitlist = connection.prepareStatement("insert into waitlist(Faculty, date, seats, timestamp) values (?,?,?,?)");
            addWaitlist.setString(1, Faculty);
            addWaitlist.setDate(2, date);
            addWaitlist.setInt(3, seats);
            addWaitlist.setTimestamp(4, currentTimestamp);
            addWaitlist.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        } 
    }
    
    public static void cancelWaitlistEntry(String Faculty, Date date)
    {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            getAllWaitlist = connection.prepareStatement("delete from waitlist where faculty = ? and date = ?");
            getAllWaitlist.setString(1, Faculty);
            getAllWaitlist.setDate(2, date);
            count = getAllWaitlist.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static void deleteWaitlistEntry(String Faculty, Date date)
    {
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        int count = 0;
        try
        {
            getAllWaitlist = connection.prepareStatement("delete from waitlist where faculty = ? and date = ?");
            getAllWaitlist.setString(1, Faculty);
            getAllWaitlist.setDate(2, date);
            count = getAllWaitlist.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<WaitlistEntry> getWaitlists()
    {
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        try
        {
            getAllWaitlist = connection.prepareStatement("select Faculty, date, seats, timestamp from waitlist order by date, timestamp");
            resultSet = getAllWaitlist.executeQuery();
            while(resultSet.next())
            {
                WaitlistEntry entry = new WaitlistEntry(resultSet.getString(1), resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4));
                waitlist.add(entry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlist;
    }
    
    public static ArrayList<WaitlistEntry> getWaitlistByDate(Date date)
    {
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        try
        {
            getAllWaitlist = connection.prepareStatement("select Faculty, seats, timestamp from waitlist where date = ?");
            getAllWaitlist.setDate(1, date);
            resultSet = getAllWaitlist.executeQuery();
            
            while(resultSet.next())
            {
               WaitlistEntry entry = new WaitlistEntry(resultSet.getString(1), date, resultSet.getInt(2), resultSet.getTimestamp(3));
               waitlist.add(entry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlist;
    }
    
    public static ArrayList<WaitlistEntry> getWaitlistByFaculty(String Faculty)
    {
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        try
        {
            getAllWaitlist = connection.prepareStatement("select date, seats, timestamp from waitlist where faculty = ?");
            getAllWaitlist.setString(1, Faculty);
            resultSet = getAllWaitlist.executeQuery();
            
            while(resultSet.next())
            {
               WaitlistEntry entry = new WaitlistEntry(Faculty, resultSet.getDate(1), resultSet.getInt(2), resultSet.getTimestamp(3));
               waitlist.add(entry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlist;
    }
}
