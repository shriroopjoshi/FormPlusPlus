/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import beans.FormDetail;
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
public class FormDetailOperations {

    private static String TableName = "FormDetail";

    public static void insert(FormDetail f) {
        try {
            String query = "INSERT INTO " + TableName + " VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query);
            stmt.setString(1, f.getFormID());
            stmt.setString(2, f.getFormName());
            stmt.setString(3, f.getFolderID());
            stmt.setString(4, f.getDateCreated());
            stmt.setString(5, f.getPublicLink());
            stmt.setString(6, null);
            stmt.executeUpdate();
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LoginOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean deleteFormEntry(String formID) throws SQLException {
        boolean deleted = false;
        String query = "DELETE FROM " + TableName + " WHERE form_id = '" + formID + "'";
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            int executeUpdate = stmt.executeUpdate(query);
            if (executeUpdate != 0) {
                deleted = true;
            }
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDetailOperations.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return deleted;
    }

    public static ArrayList<FormDetail> selectAll(String formID) {
        ArrayList<FormDetail> arr = new ArrayList<>();
        try {
            ResultSet executeQuery = DatabaseConnection.getConnection().createStatement().executeQuery(
                    "SELECT * FROM " + TableName + " WHERE form_id = '" + formID + "'"
            );
            while (executeQuery.next()) {
                String formId = executeQuery.getString("form_id");
                String formName = executeQuery.getString("form_name");
                String folderId = executeQuery.getString("folder_id");
                String dateCreated = executeQuery.getString("date_created");
                String public_link = executeQuery.getString("public_link");
                String form_link = executeQuery.getString("form_link");
                arr.add(new FormDetail(formId, formName, folderId, dateCreated, public_link, form_link));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FormDetailOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }

    public static String getFolderID(String formID) {
        String folderID = null;
        String query = "SELECT folder_id FROM " + TableName + " WHERE form_id = '" + formID + "'";
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            ResultSet executeQuery = stmt.executeQuery(query);
            while (executeQuery.next()) {
                System.out.println(formID + " XX");
                folderID = executeQuery.getString("folder_id");
            }
        } catch (SQLException ex) {
            System.out.println("BB");
            Logger.getLogger(FormDetailOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("folderID " + folderID);
        return folderID;
    }

    public static boolean uploadPublicLinkForForm(String formID, String link) {
        boolean uploaded = false;
        String query = "UPDATE " + TableName + " SET form_link = '" + link
                + "' WHERE form_id = '" + formID + "'";
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            int rows = stmt.executeUpdate(query);
            if (rows != 0) {
                uploaded = true;
            }
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDetailOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uploaded;
    }

    public static FormDetail selectAllFromFolderId(String folder_id) {
        FormDetail formDetail = null;
        String query = "SELECT * FROM " + TableName + " WHERE folder_id = '" + folder_id + "'";
        try {
            Statement stmt = DatabaseConnection.getConnection().createStatement();
            ResultSet executeQuery = stmt.executeQuery(query);
            if (executeQuery.next()) {
                String formId = executeQuery.getString("form_id");
                String formName = executeQuery.getString("form_name");
                String folderId = executeQuery.getString("folder_id");
                String dateCreated = executeQuery.getString("date_created");
                String public_link = executeQuery.getString("public_link");
                String form_link = executeQuery.getString("form_link");
                formDetail = new FormDetail(formId, formName, folderId, dateCreated, public_link, form_link);
            }
            DatabaseConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDetailOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formDetail;
    }
}
