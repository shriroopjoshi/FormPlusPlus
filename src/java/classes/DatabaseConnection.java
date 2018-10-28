/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

//import com.mysql.jdbc.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dell
 */
public class DatabaseConnection {

    private static Connection connection;

    public static Connection getConnection() {
        try {
            Class.forName(Constants.DriverName);
            connection = DriverManager.getConnection(Constants.DBUrl, Constants.username, Constants.password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public static boolean closeConnection() {
        boolean closed = false;
        try {
            if (connection.isClosed()) {
                return closed;
            } else {
                connection.close();
                closed = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return closed;
    }
}
