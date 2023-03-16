package com.example.capstoneattmpt1shopandspeak;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechText extends AppCompatActivity {

    EditText Textedit;
    Button btnSend;
    int CameraConfScore = 0;
    String[] CameraArray = new String[]{"USE", "OPEN", "CAMERA", "SCAN", "SCANNER", "BARCODE"};
    SpeechRecognizer speechRecognizer;
    TextToSpeech textToSpeech, txtTspch, BarcodeTTS;
    boolean button_pressed = false;

    /*
    After we scan a barcode, this tells us to pass the data to the other Activities in the app so we
    can use it as needed
*/
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if(txtTspch != null){ txtTspch.shutdown();}
        if(BarcodeTTS != null){ BarcodeTTS.shutdown();}

        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Results");
            builder.setMessage(result.getContents());

            //Passing the UPC string to the other activity that we will use to search our database and display information
            Intent ResPass = new Intent(this, ProductDisplay.class);
            ResPass.putExtra("barcode", result.getContents());
            startActivity(ResPass);

            //This option should never happen, this means data was corrupted hardcore
        } else {
            Log.e("ScannerError", "No Data was found in the scanner");
        }

    });

    /*
    * This Activity Launcher will take the results of the SpeechToText Command
    * given by the user and determine what to do based on the words that were spoken

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == 999) {
                    //TextToSpeech telling the user what they should do next
                    openScanner();
                }
            });
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_text);

        Textedit = findViewById(R.id.TEdit);

        Hello();
        new CountDownTimer(5000, 1000){
            public void onFinish(){
                if(!button_pressed) {
                    ToggleMic();
                }
            }
            @Override
            public void onTick(long l) {} }.start();
        //Setting btnSend to the button in the xml
        btnSend = findViewById(R.id.buttonCam);

        //When the button is clicked, call the openScanner() function
        btnSend.setOnClickListener(v -> openScanner());
    }


    public void ToggleMic() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Textedit.setHint("Listening...");
            }

            @Override
            public void onBeginningOfSpeech() {
                Textedit.setText("");
                if(txtTspch != null){ txtTspch.shutdown();}
                if(textToSpeech != null){ textToSpeech.shutdown();}
            }

            @Override
            public void onRmsChanged(float v) { }

            @Override
            public void onBufferReceived(byte[] bytes) { }

            @Override
            public void onEndOfSpeech() { Textedit.setHint("Hmmmm.... Thinking...."); }

            @Override
            public void onError(int i) { DidntCatchThat(); }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Textedit.setText(data.get(0));

                //Checking to See if the user is asking to open the camera up or not
                for (String x : data) {
                    for (int i = 0; i < CameraArray.length; ++i) {
                        if (x.toUpperCase().contains(CameraArray[i])) {
                            ++CameraConfScore;
                        }
                    }
                }

                if (CameraConfScore > 1) {
                    //Turning on the the TextToSpeech talker
                    BarcodeTTS = new TextToSpeech(getApplicationContext(), i -> {

                        // if No error is found then TextToSpeech can perform the translation
                        if (i != TextToSpeech.ERROR) {
                            // To Choose language of speech
                            BarcodeTTS.setLanguage(Locale.getDefault());

                            //Let the user know what actions are occurring
                            BarcodeTTS.speak("Okay, Opening the Camera.", TextToSpeech.QUEUE_FLUSH, null, null);
                            BarcodeTTS.speak("Please place the barcode of the item in-front of the camera", TextToSpeech.QUEUE_ADD, null, null);
                        }
                    });
                    BarcodeTTS.shutdown();

                    openScanner();
                } else {
                    DidntCatchThat();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        });
        speechRecognizer.startListening(speechRecognizerIntent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    /*
     *    This function will open the Barcode scanner and prepare the API for capture
     *    in this function we will also enable all the options needed to scan the barcodes.
     */
    private void openScanner() {

        button_pressed = true;
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CamActivity.class);
        barLauncher.launch(options);

    }

    /* */
    public void DidntCatchThat(){

        //Turning on the the TextToSpeech talker
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {

            // if No error is found then TextToSpeech can perform the translation
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech.setLanguage(Locale.getDefault());
                //Let the user know what actions are occurring
                textToSpeech.speak("I'm sorry, I didn't understand that, could you please try again?", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        textToSpeech.shutdown();

        new CountDownTimer(5000, 1000){
            public void onFinish(){
                ToggleMic();
            }
            @Override
            public void onTick(long l) {} }.start();
    }

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
                        txtTspch.speak("Hello! Please say a command, or hit the Open Camera button to get started!", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                    @Override
                    public void onTick(long l) {} }.start();
            }
        });
        txtTspch.shutdown();
    }
}



