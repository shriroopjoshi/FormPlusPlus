/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import beans.Login;
import classes.DatabaseConnection;
import java.sql.PreparedStatement;
//import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dell
 */
public class LoginOperations {
    private static String TableName = "Login";

    public static boolean isUserPresent(Login u) {
        boolean present = false;
        try {
            present = DatabaseConnection.getConnection().createStatement().executeQuery(
                    "SELECT uname FROM " + TableName + " WHERE uname = " + "'" + u.getUsername() + "'"
                    ).next();
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return present;
    }

    public static void Insert(Login u) {
        try {
            String query = "INSERT INTO " + TableName + " VALUES (?, ?)";
            PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query);
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getRefreshToken());
            stmt.executeUpdate();
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String getRefreshToken(String user) {
        String rt = "";
        try {
            ResultSet executeQuery = DatabaseConnection.getConnection().createStatement().executeQuery(
                                             "SELECT rtoken FROM " + TableName + " WHERE uname = " + "'" + user + "'"
                                             );
            if(executeQuery.next())
                rt = executeQuery.getString("rtoken");
        } catch (Exception ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rt;
    }

    public static void updateRefreshToken(Login u) {
        try {
            String query = "UPDATE " + TableName + " SET rtoken = ? WHERE uname = ?";
            PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query);
            stmt.setString(1, u.getRefreshToken());
            stmt.setString(2, u.getUsername());
            stmt.executeUpdate();
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
