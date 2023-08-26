package com.example.capstoneattmpt1shopandspeak;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class OptionsMenu extends AppCompatActivity {

    String selectedTheme = "Black on White";
    boolean TextToSpeechOn = true;
    SharedPreferences fetchSP, sp;
    Spinner spinner;
    Switch TTS;

    ArrayAdapter<String> adapter;
    List<String> ColorScheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);


        //ColorScheme for different visual impairments
        ColorScheme = new ArrayList<>();
        ColorScheme.add("Black on White");
        ColorScheme.add("High Contrast");
        ColorScheme.add("White on Black");

        //Save and go back to home page
        Button RTM = findViewById(R.id.ReturnHomeBtn);
        RTM.setOnClickListener(v -> ReturnHome());

        //Save and speak a command to the application
        Button SpeakCommand = findViewById(R.id.SpeakACommand);
        SpeakCommand.setOnClickListener(v -> OpenSpeakACommand());

        //Switch for TextToSpeech Toggling
        TTS = findViewById(R.id.TextToSpeechToggle);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //Spinner Adapter Boilerplate Code
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ColorScheme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.ColorSchemeSpinner);
        spinner.setAdapter(adapter);

        fetchSP = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        selectedTheme = fetchSP.getString("Theme", "");

        if(fetchSP.getBoolean("TTS", true)) {
            TTS.setChecked(true);
        } else {
            TTS.setChecked(false);
        }
        //Selected Theme string, so we can save the activity
        selectedTheme = spinner.getSelectedItem().toString();
        int spinnerPosition = adapter.getPosition(selectedTheme);
        spinner.setSelection(spinnerPosition);
    }

    private void ReturnHome(){

        TextToSpeechOn = TTS.isChecked();

        sp = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putString("Theme", selectedTheme);
        spEdit.putBoolean("TTS", TextToSpeechOn);
        spEdit.apply();

        Intent ReturnHome = new Intent(OptionsMenu.this, MainActivity.class);
        startActivity(ReturnHome);
    }

    private void OpenSpeakACommand(){
        Intent SpeakCommands = new Intent(OptionsMenu.this, SpeechText.class);
        startActivity(SpeakCommands);
    }

}
