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
/**
 *
 * @author wengw
 */       
public class Faculty {
    
    private static Connection connection;
    private static ArrayList<String> Faculty = new ArrayList<String>();
    private static PreparedStatement addFaculty;
    private static PreparedStatement getAllFaculty;
    private static ResultSet resultSet;

    public static boolean addFaculty(String name)
    {
       connection = DBConnection.getConnection();
       try
       {
           addFaculty = connection.prepareStatement("insert into Faculty(name) values (?)");
           addFaculty.setString(1, name);
           addFaculty.executeUpdate();
           return true;
       }
       catch(SQLException sqlException)
       {
           sqlException.printStackTrace();
       }
       return false;
    }
    
    public static ArrayList<String> getFaculty()
    {
        connection = DBConnection.getConnection();
        ArrayList<String> Faculty = new ArrayList<String>();
        try
        {
            getAllFaculty = connection.prepareStatement("select name from faculty");
            resultSet = getAllFaculty.executeQuery();
            
            while(resultSet.next())
            {
                Faculty.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return Faculty;
    }
    
}
