package com.example.capstoneattmpt1shopandspeak;


//All imports needed for API and app functions

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECORD_AUDIO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Locale;

/*
*    This Class is the 'Main' class of the application, it will
*    drive the opening of the camera as well as the barcode scanning API.
*    After the barcode has been scanned, the data will be passed onto the
*    ProductDisplay class where we will continue the reset of the processing.
*/
public class MainActivity extends AppCompatActivity {


    Button BarCodeButton,OptButton, SpeakCommandButton; //Button used to open the camera
    TextToSpeech txtTspch; //TextToSpeech Object so we can allow the app to talk to the user
    String CurrentTheme;
    boolean TextToSpeechOnOFF = true; //Text to speech is on by default

    //Barcode Init
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if(txtTspch != null){ txtTspch.shutdown();}

        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Results");
            builder.setMessage(result.getContents());

            Intent ResPass = new Intent(this, LoadingScreen.class);
            ResPass.putExtra("barcode", result.getContents());
            startActivity(ResPass);
            Log.i("Barcode Scan Result",result.getContents());
        } else {
            Log.e("<!><!> Scanning Error <!><!>", "No Data was found in the scanner");
        }

    });

    //'When this Activity opens'
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check to make sure we have all the permissions needed to perform the action inside of the application
        if(!checkPermission()){
            requestPermission();
        }

        //Listen for the button to be clicked and we can move passed the Main page
        OptButton = findViewById(R.id.OptionsButton);
        BarCodeButton = findViewById(R.id.BCButton);
        SpeakCommandButton = findViewById(R.id.SpeakCommandButton);
        SpeakCommandButton.setOnClickListener(v -> OpenSpeechToText());
        BarCodeButton.setOnClickListener(v -> openScanner());
        OptButton.setOnClickListener(v -> OpenOptionsMenu());

    }

    /*
     *  When we reopen this application we will want to check if we have some stored preferences (AKA User options)
     */
    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences fetchSP = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        TextToSpeechOnOFF = fetchSP.getBoolean("TTS", true);
        CurrentTheme = fetchSP.getString("Theme", "");

        //Begin to Speak to the user only if the TTS setting is active (True)
        if(TextToSpeechOnOFF){
            Hello();
        }
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

        return  result  == PackageManager.PERMISSION_GRANTED &&
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


    /*
     * This function opens the Barcode Scanning Activity
     */
    private void openScanner() {

        if(txtTspch != null) { txtTspch.stop(); }

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CamActivity.class);
        barLauncher.launch(options);

    }

    /*
     * This function Opens the Options Menu
     */
    public void OpenOptionsMenu(){
        Intent OpenOptions = new Intent(MainActivity.this, OptionsMenu.class);
        startActivity(OpenOptions);
    }

    /*
     * This Function Opens the SpeechText Activity
     */
    public void OpenSpeechToText(){
        Intent OpenSpeechText = new Intent(MainActivity.this, SpeechText.class);
        startActivity(OpenSpeechText);
    }


}

