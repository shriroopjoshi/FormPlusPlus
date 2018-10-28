/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import beans.Form;
import classes.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dell
 */
public class FormOperations {
    private static String TableName = "Form";
    
    public static void insert(Form f) {
        try {
            String query = "INSERT INTO " + TableName + " VALUES (?, ?)";
            PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query);
            stmt.setString(1, f.getUserID());
            stmt.setString(2, f.getFormID());
            stmt.executeUpdate();
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ArrayList<String> getFormIdFromUserId(String userID) {
        ArrayList<String> arr = new ArrayList<>();
        try {
            ResultSet executeQuery = DatabaseConnection.getConnection().createStatement().executeQuery(
                                             "SELECT form_id FROM " + TableName + " WHERE uname = " + "'" + userID + "'"
                                             );
            while(executeQuery.next()) {
                arr.add(executeQuery.getString("form_id"));
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }
    
    public static boolean deleteEntry(Form f) throws SQLException {
        boolean deleted = false;
        String query = "DELETE FROM " + TableName + " WHERE uname = '" + f.getUserID() + "' AND form_id = '" + f.getFormID()+ "'";
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            int executeUpdate = stmt.executeUpdate(query);
            if(executeUpdate != 0)
                deleted = true;
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormOperations.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return deleted;
    }
}
