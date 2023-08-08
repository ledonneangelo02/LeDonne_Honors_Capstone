package com.example.capstoneattmpt1shopandspeak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceDataStore;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.prefs.Preferences;

public class OptionsMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PreferenceDataStore {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        //ColorScheme for different visual impairments
        String[] ColorScheme = { "Black on White", "High Contrast", "White on Black"};

        //User Preferences so when they open the app again, it will stay in a consistent state
        SharedPreferences settings = getSharedPreferences("UserInfo",  MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("TTS", true);
        editor.putString("ColorScheme", ColorScheme[2]);
        editor.apply();



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

    @Override
    public void putString(String key, @Nullable String value) {
        PreferenceDataStore.super.putString(key, value);
    }
}