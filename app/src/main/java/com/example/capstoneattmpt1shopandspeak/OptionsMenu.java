package com.example.capstoneattmpt1shopandspeak;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class OptionsMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        //ColorScheme for different visual impairments
        List<String> ColorScheme = new ArrayList<String>();
        ColorScheme.add("Black on White");
        ColorScheme.add("High Contrast");
        ColorScheme.add("White on Black");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
          this, android.R.layout.simple_spinner_item, ColorScheme);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.ColorSchemeSpinner);
        spinner.setAdapter(adapter);

        Button RTM = findViewById(R.id.ReturnHomeBtn);
        RTM.setOnClickListener(v -> ReturnHome());
    }
    private void ReturnHome(){
        Intent ReturnHome = new Intent(OptionsMenu.this, MainActivity.class);
        startActivity(ReturnHome);
    }
}