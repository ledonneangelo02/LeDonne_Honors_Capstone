package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;

import androidx.appcompat.app.AppCompatActivity;



public class ProductDisplay extends AppCompatActivity {

    String Rez;
    String url = "jdbc:db2:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);


        Intent i = getIntent();
        Rez = i.getStringExtra("barcode");




        Button thisButton = findViewById(R.id.buttonReturn);
        TextView MyTextView = findViewById(R.id.BarcodeID);
        MyTextView.setText(Rez);



        thisButton.setOnClickListener(view -> {
                Intent x = new Intent(this, SpeechText.class);
                startActivity(x);
        });

    }

}


