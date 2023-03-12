package com.example.capstoneattmpt1shopandspeak;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechText extends AppCompatActivity {
    EditText Textedit;
    ImageView Mic;  //Clickable image of a microphone to initiate Speech-to-Text
    SpeechRecognizer speechRecognizer;
    String[] CameraArray = new String[]{"USE", "OPEN", "CAMERA", "SCAN", "SCANNER"};
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_text);

        Textedit = findViewById(R.id.TEdit);

        Mic = findViewById(R.id.buttonMic);

        Mic.setOnClickListener(x -> ToggleMic());


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
                Textedit.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

                Textedit.setHint("Hmmmm.... Thinking....");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                int ConfScore = 0;

                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Textedit.setText(data.get(0));

                //Checking to See if the user is asking to open the camera up or not
                for(String x : data){
                    for(int i = 0; i < CameraArray.length; ++i){
                        if(x.toUpperCase().contains(CameraArray[i])){
                            ++ConfScore;
                        }
                    }
                }

                if(ConfScore > 1){

                    //Turning on the the TextToSpeech talker
                    textToSpeech = new TextToSpeech(getApplicationContext(), i -> {

                        // if No error is found then TextToSpeech can perform the translation
                        if(i != TextToSpeech.ERROR){
                            // To Choose language of speech
                            textToSpeech.setLanguage(Locale.getDefault());

                            textToSpeech.speak("Okay, Opening the Camera.", TextToSpeech.QUEUE_FLUSH, null, null);

                            textToSpeech.speak("Please place the barcode of the item in-front of the camera", TextToSpeech.QUEUE_ADD, null, null);
                        }
                    });

                    textToSpeech.shutdown();


                    Intent BacktoMain = new Intent(SpeechText.this, MainActivity.class);
                    startActivity(BacktoMain);
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

}