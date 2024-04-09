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
public class Reservations {
    
    private static Connection connection;
    private static PreparedStatement addReserve;
    private static PreparedStatement getAllReserves;
    private static PreparedStatement getAllRooms;
    private static ResultSet resultSet;
    
    public static boolean addReservationEntry(String Faculty, Date date, int seats)
    {
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> rooms = Rooms.getRooms();
        ArrayList<String> checkRooms = getRoomsReservedByDateS(date);
        
        if (checkSpace(seats,date) == true)
        {
            for(int count = 0; count < rooms.size(); count++)
            {
                RoomEntry room = rooms.get(count);
                int roomSize = room.getSeats();
                if (roomSize >= seats && !checkRooms.contains(room))
                {
                    ArrayList<RoomEntry> ReservedRooms = getRoomsReservedByDate(date);
                    if(ReservedRooms.isEmpty() || ((!ReservedRooms.contains(room) || (openSeats(room.getName(), seats) == true)) && !checkRooms.contains(room.getName())))
                    {
                        try
                        {
                            addReserve = connection.prepareStatement("insert into reservations (Faculty, room, date, seats, timestamp) values(?,?,?,?,?)");
                            addReserve.setString(1, Faculty);
                            addReserve.setString(2, room.getName());
                            addReserve.setDate(3, date);
                            addReserve.setInt(4, room.getSeats());
                            addReserve.setTimestamp(5, currentTimestamp);
                            addReserve.executeUpdate();
                            return true;
                        }
                        catch(SQLException sqlException)
                        {
                            sqlException.printStackTrace();
                        }
                        return true;
                    }
                }
            }
            
        }
        return false;
    }
    
    public static void cancelReservation(String Faculty, Date date)
    {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            getAllRooms = connection.prepareStatement("delete from reservations where faculty = ? and date = ?");
            getAllRooms.setString(1, Faculty);
            getAllRooms.setDate(2, date);
            count = getAllRooms.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static void deleteReservation(String Room)
    {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            getAllRooms = connection.prepareStatement("delete from reservations where room = ?");
            getAllRooms.setString(1, Room);
            count = getAllRooms.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ReservationEntry> getReservationsByFaculty(String Faculty)
    {
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservation = new ArrayList<>();
        try
        {
            getAllReserves = connection.prepareStatement("select room, date, seats, timestamp from reservations where faculty = ?");
            getAllReserves.setString(1, Faculty);
            resultSet = getAllReserves.executeQuery();
            
            while(resultSet.next())
            {
                ReservationEntry reserve = new ReservationEntry(Faculty, resultSet.getString(1), resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4));
                reservation.add(reserve);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return reservation;
    }
    
    public static ArrayList<ReservationEntry> getReservationsByDate(Date date)
    {
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservation = new ArrayList<>();
        try
        {
            getAllReserves = connection.prepareStatement("select faculty, room, seats, timestamp from reservations where date = ?");
            getAllReserves.setDate(1, date);
            resultSet = getAllReserves.executeQuery();
            
            while(resultSet.next())
            {
                ReservationEntry reserve = new ReservationEntry(resultSet.getString(1), resultSet.getString(2),date,resultSet.getInt(3),resultSet.getTimestamp(4));
                reservation.add(reserve);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return reservation;
    }
    
    public static ArrayList<RoomEntry> getRoomsReservedByDate(Date date)
    {
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> rooms = new ArrayList<>();
        try
        {
            getAllRooms = connection.prepareStatement("Select room, seats from reservations where date = ?");
            getAllRooms.setDate(1, date);
            resultSet = getAllRooms.executeQuery();
            
            while(resultSet.next())
            {
                RoomEntry room = new RoomEntry(resultSet.getString(1), resultSet.getInt(2));
                rooms.add(room);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return rooms;
    }
    
    public static ArrayList<String> getRoomsReservedByDateS(Date date)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> rooms = new ArrayList<>();
        try
        {
            getAllRooms = connection.prepareStatement("Select room, seats from reservations where date = ?");
            getAllRooms.setDate(1, date);
            resultSet = getAllRooms.executeQuery();
            
            while(resultSet.next())
            {
                rooms.add(resultSet.getString("room"));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return rooms;
    }
    
    public static ArrayList<ReservationEntry> getReservedRoomInfo()
    {
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> rooms = new ArrayList<>();
        try
        {
            getAllRooms = connection.prepareStatement("Select faculty, room, date, seats, timestamp from reservations");
            resultSet = getAllRooms.executeQuery();
            
            while(resultSet.next())
            {
                String Faculty = resultSet.getString(1);
                String room = resultSet.getString(2);
                Date date = resultSet.getDate(3);
                int seats = resultSet.getInt(4);
                Timestamp timestamp = resultSet.getTimestamp(5);
                
                ReservationEntry reserve = new ReservationEntry(Faculty, room, date, seats, timestamp);
                rooms.add(reserve);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return rooms;
    }
    
    public static ArrayList<RoomEntry> getReservedRooms()
    {
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> rooms = new ArrayList<>();
        try
        {
            getAllRooms = connection.prepareStatement("Select room, seats from reservations");
            resultSet = getAllRooms.executeQuery();
            
            while(resultSet.next())
            {
                RoomEntry room = new RoomEntry(resultSet.getString(1), resultSet.getInt(2));
                rooms.add(room);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return rooms;
    }
    
    public static int getTotalSeats(String Room)
    {
        connection = DBConnection.getConnection();
        int total = 0;
        
        try
        {
            getAllRooms = connection.prepareStatement("Select seats from reservations where room = ?");
            getAllRooms.setString(1, Room);
            resultSet = getAllRooms.executeQuery();
            
            while(resultSet.next())
            {
                total = total + resultSet.getInt(1);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return total;
    }
    
    public static boolean openSeats(String roomName, int seats)
    {
        ArrayList<RoomEntry> reservedRooms = getReservedRooms();
        Rooms room = new Rooms();
        int currentSize = room.getRoomSize(roomName);
        
        for (int count = 0; count < reservedRooms.size(); count++)
        {
            String RoomName = reservedRooms.get(count).getName();
            
            if(roomName.equals(RoomName))
            {
                int reservedSeats = getTotalSeats(RoomName);
                int openSeats = currentSize - reservedSeats;
                
                if(openSeats >= seats)
                {
                    return true;
                }
            }
        }
        
        ArrayList<RoomEntry> allRooms = room.getRooms();
        for (int count = 0; count < allRooms.size(); count++)
        {
            String ALLRoomName = allRooms.get(count).getName();
            
            if(roomName.equals(ALLRoomName))
            {
                int reservedSeats = getTotalSeats(ALLRoomName);
                int openSeats = currentSize - reservedSeats;
                
                if(openSeats >= seats)
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    public static boolean getReserved(String roomName, Date date)
    {
        ArrayList<RoomEntry> reservedRooms = getRoomsReservedByDate(date);
        
        for (int count = 0; count < reservedRooms.size(); count++)
        {
            if(reservedRooms.get(count).getName().equals(roomName))
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkSpace(int seats, Date date)
    {
        ArrayList<RoomEntry> reservedRooms = getRoomsReservedByDate(date);
        
        Rooms rooms = new Rooms();
        for (int count = 0; count < reservedRooms.size(); count++)
        {
            int initial = Rooms.getRoomSize(reservedRooms.get(count).getName());
            int reserved = (reservedRooms.get(count).getSeats());
            int open = (initial - reserved);
            if (open >= seats)
            {
                return true;
            }
        }
        
        ArrayList<RoomEntry> openRoom = rooms.getRooms();
        
        for (int count = 0; count < openRoom.size(); count++)
        {
            String roomName = openRoom.get(count).getName();
            int roomSeats = openRoom.get(count).getSeats();
            
            if(getReserved(roomName, date) == false)
            {
                if(roomSeats >= seats)
                {
                    return true;
                }
            }
        }
        return false;  
    }
}
