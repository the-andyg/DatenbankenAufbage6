package org.example;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    final static String db_name = "XEPDB2";
    final static String db_pass = "aGKg4nXP8r";
    final static String db_user = "g19";
    private Connection connection = null;

    public boolean connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/db1", "root", "");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public DatabaseMetaData getMetaData() {
        DatabaseMetaData meta = null;
        try {
            meta = this.connection.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meta;
    }

//    public ResultSet execStatement() {
//
//    }

    public boolean closeConnection() {
        if(this.connection != null) {
            try {
                this.connection.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
