package com.example.capstoneattmpt1shopandspeak;


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

        //Get the shared preferences from the AppSettings section in private mode
        fetchSP = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        selectedTheme = fetchSP.getString("Theme", "");

        //Setup the spinner with the values of different themes
        SpinnerSetup();

        //Whatever the stored state of the text to speech is
        TTS.setChecked(fetchSP.getBoolean("TTS", true));
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


    /*
        This Function is strictly for setting up and dealing with the Spinner for the theme selection.
    */
    private void SpinnerSetup() {

        //ColorScheme for different visual impairments
        ColorScheme = new ArrayList<>();
        ColorScheme.add(selectedTheme);
        ColorScheme.add("High Contrast");
        ColorScheme.add("White on Black");

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}

        });
    }
}
