package de.athoramine.core.database;

import java.sql.ResultSet;
import java.sql.Statement;

public class SQLEntity {

    public ResultSet resultSet = null;
    public Statement statement = null;

    public void close() {
        try { if (resultSet != null) resultSet.close(); } catch (Exception e) {}
        try { if (statement != null) statement.close(); } catch (Exception e) {}
    }

}
