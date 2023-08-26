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
    String TTSOn;
    boolean TextToSpeechOn;
    SharedPreferences fetchSP, sp;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch TTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        //ColorScheme for different visual impairments
        List<String> ColorScheme = new ArrayList<>();
        ColorScheme.add("Black on White");
        ColorScheme.add("High Contrast");
        ColorScheme.add("White on Black");

        //Spinner Adapter Boilerplate Code
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ColorScheme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.ColorSchemeSpinner);

        //Save and go back to home page
        Button RTM = findViewById(R.id.ReturnHomeBtn);
        RTM.setOnClickListener(v -> ReturnHome());

        //Save and speak a command to the application
        Button SpeakCommand = findViewById(R.id.SpeakACommand);
        SpeakCommand.setOnClickListener(v -> OpenSpeakACommand());

        //Switch for TextToSpeech Toggling
        TTS = findViewById(R.id.TextToSpeechToggle);
        TextToSpeechOn = TTS.isChecked();

        //Selected Theme string, so we can save the activity
        selectedTheme = spinner.getSelectedItem().toString();
    }

    @Override
    protected void onResume(){
        super.onResume();

        fetchSP = this.getSharedPreferences("AppSettings",MODE_PRIVATE);
        selectedTheme = fetchSP.getString("Theme","");
        TextToSpeechOn = fetchSP.getBoolean("TTS",true);
        if(TextToSpeechOn){
            TTS.setChecked(true);
        }else{
            TTS.setChecked(false);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        sp = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putString("Theme", selectedTheme);
        spEdit.putBoolean("TTS", TextToSpeechOn);
        spEdit.apply();
    }

    private void ReturnHome(){
        Intent ReturnHome = new Intent(OptionsMenu.this, MainActivity.class);
        startActivity(ReturnHome);
    }

    private void OpenSpeakACommand(){
        Intent SpeakCommands = new Intent(OptionsMenu.this, SpeechText.class);
        startActivity(SpeakCommands);
    }

}
