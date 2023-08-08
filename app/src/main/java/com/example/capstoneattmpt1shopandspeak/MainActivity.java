package com.example.capstoneattmpt1shopandspeak;


//All imports needed for API and app functions
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECORD_AUDIO;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

/*
*    This Class is the 'Main' class of the application, it will
*    drive the opening of the camera as well as the barcode scanning API.
*    After the barcode has been scanned, the data will be passed onto the
*    ProductDisplay class where we will continue the reset of the processing.
*/
public class MainActivity extends AppCompatActivity {

    Button BarCodeButton; //Button used to open the camera
    Button OptButton;
    TextToSpeech txtTspch; //TextToSpeech Object so we can allow the app to talk to the user


    //'When this Activity opens'
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check to make sure we have all the permissions needed to perform the action inside of the application
        if(!checkPermission()){
            requestPermission();
        }

        //Initiate the TextToSpeech Object, and begin speaking to the user to instruct them what to do
        Hello();

        //Listen for the button to be clicked and we can move passed the Main page
        OptButton = findViewById(R.id.OptionButton);
        BarCodeButton = findViewById(R.id.BCButton);
        BarCodeButton.setOnClickListener(v -> AppWelcome());
        OptButton.setOnClickListener(v -> OpenOptionsMenu());

    }
    /*
     * This method is Responsible for Checking the Device Permissions before attempting to use them
     *
     * @return boolean : True if all the permissions we need are granted, False if we are missing any
     *
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return
                result == PackageManager.PERMISSION_GRANTED &&
                        result1 == PackageManager.PERMISSION_GRANTED &&
                        result2 == PackageManager.PERMISSION_GRANTED &&
                        result3 == PackageManager.PERMISSION_GRANTED;
    }

    /*
     * This method is responsible for requesting the permissions we don't currently have.
    */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, INTERNET, RECORD_AUDIO}, 1);
    }

    /*
     * This method is simple, it passes the work onto the SpeechToText Activity since that's where we
     *      would like the user to interact with the app interface
     *
     * This function triggers a TextToSpeech action that will welcome the person to the app to let
     *      the user know they are at the welcome screen and we are ready for them to interact
     */
    public void Hello(){
        //Turning on the the TextToSpeech talker
        txtTspch = new TextToSpeech(getApplicationContext(), i -> {
            // if No error is found then TextToSpeech can perform the translation
            if(i != TextToSpeech.ERROR){

                // To Choose language of speech
                txtTspch.setLanguage(Locale.getDefault());
                //Ask the user to pick an option
                new CountDownTimer(3000, 1000){
                    public void onFinish(){
                        txtTspch.speak("Welcome to Shop and Speak!", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                    @Override
                    public void onTick(long l){}

                }.start();
            }
        });
        txtTspch.shutdown();
    }


    public void AppWelcome(){

        if(txtTspch != null ){ txtTspch.shutdown(); }

        Intent SpeechToText = new Intent(MainActivity.this, SpeechText.class);
        startActivity(SpeechToText);
    }


    public void OpenOptionsMenu(){
        Intent OpenOptions = new Intent(MainActivity.this, OptionsMenu.class);
        startActivity(OpenOptions);
    }

}

