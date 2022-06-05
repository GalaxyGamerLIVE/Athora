package AthoraCore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GlobalDatabase extends AbstractDatabase {
    public static Connection con;

    public static void connect() {
        try {
            con = DriverManager.getConnection(getUrl("athora_global"), USERNAME, PASSWORD);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isConnected() {
        if (con != null)
            return true;
        return false;
    }

    public static ResultSet query(String query) {
        try {
            if (con.isClosed())
                connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void update(String update) {
        try {
            if (con.isClosed())
                connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(update);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
