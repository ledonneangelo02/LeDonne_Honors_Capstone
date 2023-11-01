package com.example.capstoneattmpt1shopandspeak;

//All imports for API and app functions

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;



/**
    This class holds the ResultDisp Activity which is responsible for
    allowing the user to input a new item into our 'database' internal txt file
 */
public class ResultDisp extends AppCompatActivity {

    String Upc, Name, Cals, Servings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_disp);



        Intent i = getIntent();
        Upc = i.getStringExtra("bcode");
        Name = i.getStringExtra("nameOfItem");
        Servings = i.getStringExtra("Servings");
        Cals = i.getStringExtra("CaloriesPerServing");


        String tempUPC = Upc;
        Upc = Upc + ",";
        String Item = Name + ",";
        String Serv = Servings + ",";
        String Clrs = Cals;



        Intent x = new Intent(ResultDisp.this, ProductDisplay.class);
        x.putExtra("barcode", tempUPC);
        startActivity(x);

    }


    }
