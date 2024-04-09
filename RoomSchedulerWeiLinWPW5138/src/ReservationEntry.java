/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
/**
 *
 * @author wengw
 */    
public class ReservationEntry {
    
    private String Faculty;
    private String Room;
    private Date date;
    private int seats;
    private Timestamp timestamp;
    
    public ReservationEntry(String Faculty, String Room, Date date,int seats,Timestamp timestamp)
    {
        this.Faculty = Faculty;
        this.Room = Room;
        this.date = date;
        this.seats = seats;
        this.timestamp = timestamp;
    }
    
    public String getFaculty()
    {
        return this.Faculty;
    }
    public String getRoom()
    {
        return this.Room;
    }
    public Date getDate()
    {
        return this.date;
    }
    public int getSeats()
    {
        return this.seats;
    }
    public Timestamp getTimestamp()
    {
        return this.timestamp;
    }
}
