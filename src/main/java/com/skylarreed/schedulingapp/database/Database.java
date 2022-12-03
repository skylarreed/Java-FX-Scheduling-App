package com.skylarreed.schedulingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A class that manages the connection to the database.
 * @author Skylar Reed
 */
public class Database {
    private static Connection conn;
    public static final String USERNAME = "root";
    private static final String PASSWORD = "Skylar90359!";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/client_schedule";
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * A method that gets the connection to the database.
     * @return the connection to the database.
     */
    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        }
        return conn;
    }

}
