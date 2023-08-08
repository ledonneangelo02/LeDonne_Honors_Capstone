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

        //ColorScheme for different visual impairments
        String[] ColorScheme = {"Black on White", "High Contrast", "White on Black"};

      /*
        //User Preferences so when they open the app again, it will stay in a consistent state
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(R.string.preference_file_key,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ColorScheme", ColorScheme[2]);
        editor.putBoolean("TTS", true);
        editor.apply();
    */

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
}