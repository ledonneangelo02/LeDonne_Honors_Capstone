package com.example.capstoneattmpt1shopandspeak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class OptionsMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);


        String[] ColorScheme = { "Black on White", "High Contrast",
                "Interview prep", "Algorithms",
                "DSA with java", "OS" };

        Spinner Schemespin = findViewById(R.id.ColorSchemeSpinner);

        Schemespin.setOnItemSelectedListener(this);

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ColorScheme);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Schemespin.setAdapter(ad);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}