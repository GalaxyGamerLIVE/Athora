package AthoraCore.util;

import AthoraCore.util.configs.GeneralConfig;

import java.sql.*;

public class Database {

    private static final String URL        = "jdbc:mysql://" + GeneralConfig.generalConfig.getString("database.host") + ":" + GeneralConfig.generalConfig.getInt("database.port") + "/" + GeneralConfig.generalConfig.getString("database.database") + "?serverTimezone=Europe/Berlin&autoReconnect=true";
    private static final String USERNAME   = GeneralConfig.generalConfig.getString("database.user");
    private static final String PASSWORD   = GeneralConfig.generalConfig.getString("database.password");

    public static Connection con;

    public static void connect() {
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//            con = DbLib.getConnectionSource("localhost:3306/athora?autoReconnect=true", USERNAME, PASSWORD);
        } catch (SQLException exception) {
//            e.printStackTrace();
            exception.printStackTrace();
        }
    }

    public static boolean isConnected() {
        if (con != null){
            return true;
        }
        return false;
    }

    public static ResultSet query(String query){
        try {
            if (con.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = null;
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void update(String update){
        try {
            if (con.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(update);
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try{
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
