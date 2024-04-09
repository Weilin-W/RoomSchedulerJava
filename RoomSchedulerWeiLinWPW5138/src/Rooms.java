/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author wengw
 */
public class Rooms {
    
    private static Connection connection;
    private static PreparedStatement addRoom;
    private static PreparedStatement getAllRooms;
    private static ResultSet resultSet;
    
    public Rooms()
    {
        connection = DBConnection.getConnection();
    }
    
    public static ArrayList<RoomEntry> getRooms()
    {
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> rooms = new ArrayList<RoomEntry>();
        try
        {
            getAllRooms = connection.prepareStatement("select name, seats from rooms order by seats");
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
    
    public static ObservableList<String> getRoomsName()
    {
        connection = DBConnection.getConnection();
        ObservableList<String> roomNames = FXCollections.observableArrayList();
        try {
            PreparedStatement getAllRooms = connection.prepareStatement("Select name from rooms order by seats");
            ResultSet resultSet = getAllRooms.executeQuery();
            while (resultSet.next()) {

                roomNames.add(resultSet.getString(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return roomNames;
    }
    
    public static void addRoom(String room, int seats)
    {
        connection = DBConnection.getConnection();
        try
        {
            addRoom = connection.prepareStatement("insert into rooms (name, seats) values (?,?)");
            addRoom.setString(1,room);
            addRoom.setInt(2,seats);
            addRoom.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static void cancelRoom(String room)
    {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            getAllRooms = connection.prepareStatement("delete from rooms where name = ?");
            getAllRooms.setString(1, room);
            count = getAllRooms.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static boolean checkRoom(String roomName) {

        try {
            PreparedStatement checkRoom = connection.prepareStatement("select * from rooms where name = ? ");
            checkRoom.setString(1, roomName);
            ResultSet resultSet = checkRoom.executeQuery();

            if (resultSet.next() == true) {

                return true;

            }

        } catch (SQLException sqlException) {

            sqlException.printStackTrace();
        }

        return false;
    }
    
    
    public static int getRoomSize(String roomName) {

        int roomSize = 0;
        try {
            PreparedStatement getRoomSize = connection.prepareStatement("select seats from rooms where name = ?");
            getRoomSize.setString(1, roomName);
            ResultSet resultSet = getRoomSize.executeQuery();

            if (resultSet.next() == true) {
                roomSize = Integer.parseInt(resultSet.getString(1));

            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return roomSize;
    }
}
