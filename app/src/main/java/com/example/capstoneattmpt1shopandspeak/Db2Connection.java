package com.example.capstoneattmpt1shopandspeak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db2Connection {

    // JDBC URL, username, and password of DB2 server
    private static final String DB_URL = "jdbc:db2://zos.kctr.marist.edu:5045/DALLASD";
    private static final String USER = "KC03E77";
    private static final String PASSWORD = "Aceace40";

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
