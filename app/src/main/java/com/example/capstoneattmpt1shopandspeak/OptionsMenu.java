package com.example.capstoneattmpt1shopandspeak;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class OptionsMenu extends AppCompatActivity {

    String selectedTheme = "Black on White";
    String NotSelectedTheme = "";
    String NotSelectedTheme2 = "";
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

        fetchSP = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        selectedTheme = fetchSP.getString("Theme", "");

        SpinnerSetup();

        Log.i("SelectedTheme on Start", selectedTheme);

        if (fetchSP.getBoolean("TTS", true)) {
            TTS.setChecked(true);
        } else {
            TTS.setChecked(false);
        }
    }

    private void SaveSettings() {
        TextToSpeechOn = TTS.isChecked();

        Log.i("SelectedTheme on close", selectedTheme);

        sp = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putString("Theme", selectedTheme);
        spEdit.putBoolean("TTS", TextToSpeechOn);
        spEdit.apply();
    }

    private void ReturnHome() {

        //Save the users changed settings
        SaveSettings();

        Intent ReturnHome = new Intent(OptionsMenu.this, MainActivity.class);
        startActivity(ReturnHome);
    }

    private void OpenSpeakACommand() {
        Intent SpeakCommands = new Intent(OptionsMenu.this, SpeechText.class);
        startActivity(SpeakCommands);
    }


    private void SpinnerSetup() {

        //ColorScheme for different visual impairments
        if(selectedTheme == "Black on White"){
            NotSelectedTheme = "High Contrast";
            NotSelectedTheme2 = "White on Black";
        }else if (selectedTheme == "White on Black") {
            NotSelectedTheme = "High Contrast";
            NotSelectedTheme2 = "Black on White";
        }else if(selectedTheme == "High Contrast"){
            NotSelectedTheme = "Black on White";
            NotSelectedTheme2 = "White on Black";
        }else{
            NotSelectedTheme = "Black on White";
            NotSelectedTheme2 = "White on Black";
        }

        ColorScheme = new ArrayList<>();
        ColorScheme.add(selectedTheme);
        ColorScheme.add(NotSelectedTheme2);
        ColorScheme.add(NotSelectedTheme);
        //Spinner Adapter Boilerplate Code
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ColorScheme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.ColorSchemeSpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Selected Theme string, so we can save the activity
                selectedTheme = spinner.getSelectedItem().toString();
                int spinnerPosition = adapter.getPosition(selectedTheme);
                spinner.setSelection(spinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
