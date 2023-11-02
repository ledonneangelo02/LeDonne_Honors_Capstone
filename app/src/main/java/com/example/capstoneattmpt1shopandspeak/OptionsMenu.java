package com.example.capstoneattmpt1shopandspeak;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class OptionsMenu extends AppCompatActivity {

    boolean TextToSpeechOn = true;
    SharedPreferences fetchSP, sp;
    Switch TTS;
    TextToSpeech txtTspch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        //Save and go back to home page
        Button ReturnHomeButton = findViewById(R.id.ReturnHomeBtn);
        ReturnHomeButton.setOnClickListener(v -> ReturnHome());

        //Save and speak a command to the application
        Button SpeakCommand = findViewById(R.id.SpeakACommand);
        SpeakCommand.setOnClickListener(v -> OpenSpeakACommand());

        //Switch for TextToSpeech Toggling
        TTS = findViewById(R.id.TextToSpeechToggle);

        //Get the shared preferences from the AppSettings section in private mode
        fetchSP = this.getSharedPreferences("AppSettings", MODE_PRIVATE);

        //Whatever the stored state of the text to speech is
        TTS.setChecked(fetchSP.getBoolean("TTS", true));

        if(TTS.isChecked()){
            TextToSpeechInstuctions();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void SaveSettings() {
        TextToSpeechOn = TTS.isChecked();

        sp = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean("TTS", TextToSpeechOn);
        spEdit.apply();
    }

    private void ReturnHome() {

        if(txtTspch != null) { txtTspch.shutdown(); }
        //Save the users changed settings
        SaveSettings();

        Intent ReturnHome = new Intent(OptionsMenu.this, MainActivity.class);
        startActivity(ReturnHome);
    }

    private void OpenSpeakACommand() {

        if(txtTspch != null) { txtTspch.shutdown(); }

        //Save the users changed settings
        SaveSettings();

        Intent SpeakCommands = new Intent(OptionsMenu.this, SpeechText.class);
        startActivity(SpeakCommands);
    }

    private void TextToSpeechInstuctions(){

        //Turning on the the TextToSpeech Object
        txtTspch = new TextToSpeech(getApplicationContext(), i -> {
            // if No error is found then TextToSpeech can perform the translation
            if(i != TextToSpeech.ERROR){
                // To Choose language of speech
                txtTspch.setLanguage(Locale.getDefault());
                //Ask the user to pick an option
                new CountDownTimer(2000, 1000){
                    public void onFinish(){
                        txtTspch.speak("Please make your changes and click save to return to the home screen, or click the 'speak a command' button", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                    @Override
                    public void onTick(long l){}
                }.start();
            }
        });
        txtTspch.shutdown();
    }

}
