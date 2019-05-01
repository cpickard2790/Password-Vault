/**
 * CustomerSignup class handles signing the user up
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSignup
{
    // URL Connection to database
    final String DB_URL = "jdbc:derby://localhost:1527/PasswordVault";
    private Connection conn;
    
    /**
     * Constructor
     * @throws SQLException 
     */
    
    public UserSignup() throws SQLException
    {
        conn = DriverManager.getConnection(DB_URL);
    }
    
    /**
     * checkUsername searches for a given username
     * @param username
     * @return ResultSet result
     * @throws SQLException 
     */
    
    public boolean checkUsername(String username) throws SQLException
    {   
        boolean userTaken = false;
        
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM "
                + "LOGINS WHERE Username = ?");
        ps.setString(1, username);
        
        // Send the statement to the DBMS
        ResultSet result = ps.executeQuery(); 
        
        if (result.next())
            userTaken = true;
        
        return userTaken;
    }
    
    /**
     * insert method inserts the username and password into the database
     * @param username
     * @param password
     * @throws SQLException 
     */
    
    public void insert(String username, String password) throws SQLException
    {
        PreparedStatement pst = conn.prepareStatement
                                ("INSERT INTO APP.LOGINS(username, password)"
                                + " VALUES(?,?)");
                        pst.setString(1, username);
                        pst.setString(2, password);   
                
                        pst.executeUpdate();
        conn.close();
    }
}
