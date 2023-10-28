package com.example.capstoneattmpt1shopandspeak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db2Connection {

    // JDBC URL, username, and password of DB2 server
    private static final String DB_URL = "jdbc:db2://192.168.1.16:50000/capstone";
    private static final String USER = "db2inst1";
    private static final String PASSWORD = "password";

    public static Connection getConnection() {
        try {
            // Load the DB2 JDBC driver
            Class.forName("com.ibm.db2.jcc.DB2Driver");

            // Create a connection to the database
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
