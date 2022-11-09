package de.athoramine.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DevDatabase extends AbstractDatabase {
    public static Connection con;

    public static void connect() {
        try {
            con = DriverManager.getConnection(getUrl("athora"), USERNAME, PASSWORD);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isConnected() {
        return con != null;
    }

    public static SQLEntity query(String query) {
        try {
            if (con.isClosed())
                connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQLEntity sqlEntity = new SQLEntity();
        try {
            sqlEntity.statement = con.createStatement();
            sqlEntity.resultSet = sqlEntity.statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sqlEntity;
    }

    public static void update(String update) {
        try {
            if (con.isClosed())
                connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.executeUpdate(update);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (statement != null) statement.close(); } catch (Exception e) {};
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
