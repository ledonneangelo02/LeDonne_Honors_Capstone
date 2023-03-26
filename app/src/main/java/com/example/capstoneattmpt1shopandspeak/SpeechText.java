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
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This activity was created to act as the main interface that interacts with the user via TTS and STT
 *   Textedit = A EditText object that stores the string spoken interpreted by Android SpeechRecognizer
 *
 *   btnSend = The button that allows the user to open the camera manually
 *
 *   CameraConfScore = The "Confidence Score" that I am giving to the spoken string from the user
 *                       and comparing it to a string of words that might mean "open camera"
 *
 *   CameraArray = A string array that holds trigger words that might mean "open the camera"
 *
 *   speechRecognizer = a SpeechRecognizer that Android handles internally
 *
 *   TextToSpeech = Different TextToSpeech objects that are used to speak information to the user at different times
 *
 *   button_pressed = defaulting it to false since the button is pressed yet, but used to trigger the textTospeech off
 *                       when the button is pressed
 */
public class SpeechText extends AppCompatActivity {

    EditText Textedit;
    Button btnSend;
    ImageView mic;
    int CameraConfScore = 0;
    String[] CameraArray = new String[]{"USE", "OPEN", "CAMERA", "SCAN", "SCANNER", "BARCODE"};
    SpeechRecognizer speechRecognizer;
    TextToSpeech textToSpeech, txtTspch, BarcodeTTS;
    boolean button_pressed = false;


    /**
     * Barcode Activity Launcher used with the zxing barcode scanning API
     *
     * This ActivityResultLauncher receives a ScanOption Object that is launched
     * with zxing options for a barcode scanner with flashlight control as well as confirmation beeping
     * (See OpenScanner() method).
     */
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        /** Shut down any TTS object that might be talking. If the object is not talking yet, we won't touch it */
        if(txtTspch != null){ txtTspch.shutdown();}
        if(BarcodeTTS != null){ BarcodeTTS.shutdown();}

        /** using the .getContents() method from the ActivityResult Launch input object that we are currently defining.
         *      IF we have something inside of our input, then we can proceed to the next step of attempting to analyze the data */
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Results");
            builder.setMessage(result.getContents());

            /** Passing the UPC string to the other activity that we will use to search our database and display information */
            Intent ResPass = new Intent(this, ProductDisplay.class);
            ResPass.putExtra("barcode", result.getContents());
            startActivity(ResPass);

            /** This option should never happen, this means data was corrupted hardcore */
        } else {
            Log.e("ScannerError", "No Data was found in the scanner");
        }

    });

    /**
     *
     * @param savedInstanceState - Creating the Instance of of the the last known saved state
     *                           in case there was any data that needed to be stored passively, allows
     *                           us to pick up right where we left off.
     *
     *  We will be opening a TextEdit field that will print what the user is saying as
     *    the SpeechRecognizer attempts to decipher what the user is asking it to do.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_text);

        Textedit = findViewById(R.id.TEdit);
        mic = findViewById(R.id.buttonMic);

        mic.setOnClickListener(v -> ToggleMic());

        //Setting btnSend to the button in the xml
        btnSend = findViewById(R.id.buttonCam);

        //When the button is clicked, call the openScanner() function
        btnSend.setOnClickListener(v -> openScanner());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void ToggleMic() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

                Textedit.setHint("Ready...");
            }

            @Override
            public void onBeginningOfSpeech() {

                Textedit.setHint("Listening...");
                Textedit.setText("");

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

    /**
     *    This function will open the Barcode scanner and prepare the API for capture
     *    in this function we will also enable all the options needed to scan the barcodes.
     */
    private void openScanner() {

        if(txtTspch != null) { txtTspch.stop(); }
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
        Textedit.setText("");

    }
}




