package com.example.capstoneattmpt1shopandspeak;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class OptionsMenu extends AppCompatActivity {

    String selectedTheme = "Black on White";
    String TTSOn;
    boolean TextToSpeechOn;
    String fileST = getFilesDir() + "/" + "settings.txt";

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
        spinner.setAdapter(adapter);

        //Save and go back to home page
        Button RTM = findViewById(R.id.ReturnHomeBtn);
        RTM.setOnClickListener(v -> ReturnHome());

        //Save and speak a command to the application
        Button SpeakCommand = findViewById(R.id.SpeakACommand);
        SpeakCommand.setOnClickListener(v -> OpenSpeakACommand());

        //Switch for TextToSpeech Toggling
        Switch TTS = findViewById(R.id.TextToSpeechToggle);
        TextToSpeechOn = TTS.isChecked();

        //Selected Theme string, so we can save the activity
        selectedTheme = spinner.getSelectedItem().toString();
    }

    private void ReturnHome(){
        SaveOptions();
        Intent ReturnHome = new Intent(OptionsMenu.this, MainActivity.class);
        startActivity(ReturnHome);
    }

    private void OpenSpeakACommand(){
        SaveOptions();
        Intent SpeakCommands = new Intent(OptionsMenu.this, SpeechText.class);
        startActivity(SpeakCommands);
    }

    private void SaveOptions(){
        if(TextToSpeechOn){
            TTSOn = "Yes";
        }else{
            TTSOn = "No";
        }

        try {
            OutputStreamWriter outStream = new OutputStreamWriter(this.openFileOutput(fileST, Context.MODE_PRIVATE));
            outStream.write("Theme: " + selectedTheme);
            outStream.write("TextToSpeech: " + TTSOn);
        }catch (IOException e){
            Log.e("Exception:", "File write Failed " + e);
        }
    }
}