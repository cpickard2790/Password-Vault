/**
 * UserLogin class handles verifying if the user's username and password
 * match before logging them in, getting the user's id #, and getting the 
 * user's list of saved logins from the database.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserLogin
{
    // Connection to the database
    final String DB_URL = "jdbc:derby://localhost:1527/PasswordVault";
    private Connection conn;
    
    /**
     * Constructor
     * @throws SQLException
     */
    
    public UserLogin() throws SQLException
    {
        conn = DriverManager.getConnection(DB_URL);
    }
    
    /**
     * checkCredentials method verifies the users username and password are
     * correct
     * @param username The user's username
     * @param password The user's password
     * @return The boolean value of true or false if username/password match
     * @throws SQLException 
     */
    
    public boolean checkCredentials(String username, 
                                    String password) throws SQLException
    {
        boolean verified = false;
        
        PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM "
                + "LOGINS WHERE Username = ?");
        ps1.setString(1, username);
        
        ResultSet result = ps1.executeQuery();
        
        if (result.next())
        {
            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM "
                    + "LOGINS WHERE Password = ?");
            ps2.setString(1, password);
            
            ResultSet result2 = ps2.executeQuery();
            
            if (result2.next())
                verified = true;
        }
        else
            verified = false;
        
        
        return verified;
    }
    
    /**
     * getUserId method gets the users ID #
     * @param username The username to use to get the ID #
     * @return The ID #
     * @throws SQLException 
     */
    
    public int getUserId(String username) throws SQLException
    {
        int userId = 0;
        
        PreparedStatement ps = conn.prepareStatement("SELECT USER_ID"
                + " FROM LOGINS WHERE USERNAME = ?");
        ps.setString(1, username);
        
        ResultSet result = ps.executeQuery();
        
        if (result.next())
            userId = result.getInt(1);
        
        
        return userId;
    }
    
    /**
     * getSavedData method gets the Titles column from the database
     * @param userId The ID# of the user to get the corresponding titles
     * @return The Titles
     * @throws SQLException 
     */
    
    public ArrayList getSavedData(int userId)throws SQLException
    {
        ArrayList titles = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT TITLE FROM "
                + "POSTS WHERE PRO_ID = ?");
        
        ps.setInt(1, userId);
        ResultSet result = ps.executeQuery();
        
        while (result.next())
            titles.add(result.getString("TITLE"));
        
        
        return titles;
    }
}
