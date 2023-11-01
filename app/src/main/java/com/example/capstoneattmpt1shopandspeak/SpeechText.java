package com.example.capstoneattmpt1shopandspeak;

import android.content.Intent;
import android.os.Bundle;
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


public class SpeechText extends AppCompatActivity {

    EditText Textedit;  //A EditText object that stores the string spoken interpreted by Android SpeechRecognizer
    Button GoHome;     //Button
    ImageView mic;      //Clickable Image object
    int CameraConfScore = 0;    //How confident we are in what the user is asking for
    String[] CameraArray = new String[]{"USE", "OPEN", "CAMERA", "SCAN", "SCANNER", "BARCODE"};
    SpeechRecognizer speechRecognizer;  //Speech-To-Text translator
    TextToSpeech textToSpeech, txtTspch, BarcodeTTS;    //Text-To-Speech prompts that will play


    /**
     * Barcode Activity Launcher used with the zxing barcode scanning API
     * <p>
     * This ActivityResultLauncher receives a ScanOption Object that is launched
     * with zxing options for a barcode scanner with flashlight control as well as confirmation beeping
     * (See OpenScanner() method).
     * </p>
     */

    /**
     *
     *    We will be opening a TextEdit field that will print what the user is saying as
     * the SpeechRecognizer attempts to decipher what the user is asking it to do.
     *
     * @param savedInstanceState - Creating the Instance of of the the last known saved state
     *                           in case there was any data that needed to be stored passively, allows
     *                           us to pick up right where we left off.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_text);

        Textedit = findViewById(R.id.TEdit);
        mic = findViewById(R.id.buttonMic);

        mic.setOnClickListener(v -> ToggleMic());

        //Setting btnSend to the button in the xml
        GoHome = findViewById(R.id.HomeButton);

        //When the button is clicked, call the openScanner() function
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
                    for (String s : CameraArray) {
                        if (x.toUpperCase().contains(s)) {
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




