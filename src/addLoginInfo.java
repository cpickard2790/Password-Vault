/**
 * addLoginInfo class handles adding data to the database
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class addLoginInfo
{
    // Connection to database
    final String DB_URL = "jdbc:derby://localhost:1527/PasswordVault";
    private Connection conn;
    
    /**
     * Constructor
     * @throws java.sql.SQLException
     */
    
    public addLoginInfo() throws SQLException
    {
        conn = DriverManager.getConnection(DB_URL);
    }
    
    /**
     * insert method handles adding the user's data for a site into the database
     * @param user The user's ID # 
     * @param title The title of the site
     * @param username The username used for the site
     * @param password The password of the site
     * @param url The URL for the site
     * @throws SQLException 
     */
    
    public void insert(int user, String title, String username,
            String password, String url) throws SQLException
    {
        
        PreparedStatement ps = conn.prepareStatement("INSERT INTO"
                + " APP.POSTS(pro_id, title, username, password, url) " +
                "VALUES(?, ?, ?, ?, ?)");
        ps.setInt(1, user);
        ps.setString(2, title);
        ps.setString(3, username);
        ps.setString(4, password);
        ps.setString(5, url);
        
        ps.executeUpdate();
        
        conn.close();
    }
    
    /**
     * The remove method removes a selected title and all of the titles saved
     * data
     * @param title The login info for site to remove
     * @throws SQLException 
     */
    
    public void remove(String title) throws SQLException
    {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM APP.POSTS"
                + " WHERE TITLE = ?");
        ps.setString(1, title);
        
        ps.executeUpdate();
        
        conn.close();
    }
    

    
    /**
     * show method displays the site's login data
     * @param title The title of site
     * @return ArrayList of data stored in database
     * @throws SQLException 
     */
    
    public ArrayList show(String title) throws SQLException
    {
        ArrayList data = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM "
                + "APP.POSTS WHERE TITLE = ?");
        ps.setString(1, title);
        ResultSet result = ps.executeQuery();
        
        while (result.next())
        {
            data.add(result.getString("TITLE"));
            data.add(result.getString("USERNAME"));
            data.add(result.getString("PASSWORD"));
            data.add(result.getString("URL"));
        }
        
        conn.close();

        return data;
    }
    
    public void update(String username, String password, String url,
                        int user) throws SQLException
    {
        PreparedStatement ps = conn.prepareStatement("UPDATE APP.POSTS SET"
                + " USERNAME = ?, PASSWORD = ?, URL = ? "
                + "WHERE PRO_ID = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, url);
        ps.setInt(4, user);
        
        ps.executeUpdate();
        
        System.out.println("Updated!");
        
        conn.close();
    }
    
    public String viewPassword(String title) throws SQLException
    {
        String pass = "";
        PreparedStatement ps = conn.prepareStatement("SELECT PASSWORD FROM "
                + "APP.POSTS WHERE TITLE = ?");
        ps.setString(1, title);
        ResultSet result = ps.executeQuery();
        
        while (result.next())
            pass = result.getString("PASSWORD");
        
        conn.close();
        return pass;
    }
    
    public String hidePassword(String title) throws SQLException
    {
        String pass = "";
        PreparedStatement ps = conn.prepareStatement("SELECT PASSWORD FROM "
                + "APP.POSTS WHERE TITLE = ?");
        ps.setString(1, title);
        ResultSet result = ps.executeQuery();
        while (result.next())
            pass = result.getString("PASSWORD");
        
        int passLength = pass.length();
        StringBuilder sb = new StringBuilder(passLength);
        for (int i = 0; i < passLength; i++)
            sb.append('*');
        
        pass = sb.toString();
        
        return pass;
    }
    
    public void closeConnection() throws SQLException
    {
        conn.close();
    }
}
