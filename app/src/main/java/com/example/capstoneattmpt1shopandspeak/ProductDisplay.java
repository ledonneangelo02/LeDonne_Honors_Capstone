package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.BreakIterator;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;


public class ProductDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        Connection connection = Db2Connection.getConnection();

        TextView queryResultTextView = findViewById(R.id.queryResultTextView);

        if (connection != null) {
            try {
                // Execute a SELECT * query
                String query = "SELECT * FROM Products";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Process and display the query results
                StringBuilder resultText = new StringBuilder();
                while (resultSet.next()) {
                    // Replace "column_name" with your actual column names
                    String columnValue = resultSet.getString("column_name");
                    resultText.append(columnValue).append("\n");
                }

                // Display the results in the TextView
                queryResultTextView.setText(resultText.toString());

                // Close the database connection
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exceptions
            }
        } else {
            // Handle the case when the connection couldn't be established

        }


    }

}


