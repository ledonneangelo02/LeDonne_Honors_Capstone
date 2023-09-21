package com.example.capstoneattmpt1shopandspeak;

//All imports for API and app functions

import android.os.Bundle;
import java.sql.*;

import androidx.appcompat.app.AppCompatActivity;



/**
    This class holds the ResultDisp Activity which is responsible for
    allowing the user to input a new item into our 'database' internal txt file
 */
public class ResultDisp extends AppCompatActivity {

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rSet = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_disp);

        TryConnection();

    }

    public void TryConnection(){
        try {
            Class.forName("com.ibm.db2.jcc.DB@Driver");
            conn = DriverManager.getConnection("jdbc:db2://zos.kctr.marist.edu:4035/DSN00115", "KC03E77", "ACEACE30");

            pstmt = conn.prepareStatement("Select * from Products");
            rSet = pstmt.executeQuery();


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}

