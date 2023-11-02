package com.example.capstoneattmpt1shopandspeak;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class SpeechText extends AppCompatActivity {

    SharedPreferences sp;
    EditText Textedit;  //A EditText object that stores the string spoken interpreted by Android SpeechRecognizer
    Button GoHome;     //Button
    ImageView mic;      //Clickable Image object
    String[] CameraArray = new String[]{"USE", "OPEN", "CAMERA", "SCAN", "SCANNER", "BARCODE"}; //Camera Keywords
    String[] OptionsArray = new String[]{"OPEN", "OPTIONS", "SETTINGS", "MENU"}; //Options Menu Keywords
    String[] OptionsChange = new String[]{"CHANGE", "TEXT", "TO", "SPEECH", "TURN", "OFF", "ON", "TOGGLE"};
    SpeechRecognizer speechRecognizer;  //Speech-To-Text translator
    TextToSpeech textToSpeech, txtTspch, OpenCameraTTS, OpenOptionsTTS, ChangeOptionsTTS;    //Text-To-Speech prompts that will play
    boolean TextToSpeechOnOFF = true; //TextToSpeech Option (default: TTS is ON)
    String CurrentTheme;

    //Barcode Init
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if(txtTspch != null){ txtTspch.shutdown();}

        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Results");
            builder.setMessage(result.getContents());

            Intent ResPass = new Intent(this, ProductDisplay.class);
            ResPass.putExtra("barcode", result.getContents());
            startActivity(ResPass);
            Log.i("Barcode Scan Result",result.getContents());
        } else {
            Log.e("<!><!> Scanning Error <!><!>", "No Data was found in the scanner");
        }

    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_text);

        Textedit = findViewById(R.id.TEdit);
        mic = findViewById(R.id.MicButton);

        mic.setOnClickListener(v -> ToggleMic());

        //Setting btnSend to the button in the xml
        GoHome = findViewById(R.id.HomeButton);

        //When the button is clicked, call the openScanner() function
    }
    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences fetchSP = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        TextToSpeechOnOFF = fetchSP.getBoolean("TTS", true);
        CurrentTheme = fetchSP.getString("Theme", "");

    }


    /*
     * This function will toggle the Speech To Text Functionality
     *    and begin listening to the user for a command
     */
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
            public void onEndOfSpeech() { Textedit.setHint("Thinking...."); }

            @Override
            public void onError(int i) { DidntCatchThat(); }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Textedit.setText(data.get(0));

                ExecutorService executorService = Executors.newFixedThreadPool(3);

                //How confident we are in what the user is asking for
                AtomicInteger cameraConfScore = new AtomicInteger(0);
                AtomicInteger optionsConfScore = new AtomicInteger(0);
                AtomicInteger optChangeConfScore = new AtomicInteger(0);

                //Checking to See if the user is asking to open the camera up or not
                for (String x : data) {
                    executorService.submit(() ->{
                        for (String s : CameraArray) {
                            if (x.toUpperCase().contains(s)) {
                                cameraConfScore.incrementAndGet();
                            }
                        }
                    });
                    executorService.submit(()->{
                        for (String y : OptionsArray){
                            if(x.toUpperCase().contains(y)){
                                optionsConfScore.incrementAndGet();
                            }
                        }
                    });
                    executorService.submit(()->{
                       for(String z : OptionsChange){
                           if(x.toUpperCase().contains(z)){
                               optChangeConfScore.incrementAndGet();
                           }
                       }
                    });
                }
                // Shutdown the executor when all tasks are completed
                executorService.shutdown();

                // Wait for all threads to finish
                try {
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    // Handle the exception if needed
                }

                // Retrieve the final scores
                int finalCameraConfScore = cameraConfScore.get();
                int finalOptionsConfScore = optionsConfScore.get();
                int finalOptConfScore = optChangeConfScore.get();

                if (finalCameraConfScore > finalOptionsConfScore && finalCameraConfScore > finalOptConfScore) {
                    if(TextToSpeechOnOFF){
                        CameraOpeningTextToSpeech();
                    }

                }else if(finalOptionsConfScore > finalCameraConfScore && finalOptionsConfScore > finalOptConfScore){
                    if(TextToSpeechOnOFF){
                        OptionsOpeningTextToSpeech();
                    }
                }else if(finalOptConfScore > finalCameraConfScore && finalOptConfScore > finalOptionsConfScore){
                    if(TextToSpeechOnOFF){
                        TextToSpeechOnOFF = false;
                        ChangeOptionsTextToSpeech(true);
                    }else{
                        TextToSpeechOnOFF = true;
                        ChangeOptionsTextToSpeech(false);
                    }
                }else{
                    DidntCatchThat();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {}
            @Override
            public void onEvent(int i, Bundle bundle) {}

        });
        speechRecognizer.startListening(speechRecognizerIntent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void SaveSettings() {

        sp = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean("TTS", TextToSpeechOnOFF);
        spEdit.apply();
    }


    public void OptionsOpeningTextToSpeech(){
        //Turning on the the TextToSpeech talker
        OpenOptionsTTS = new TextToSpeech(getApplicationContext(), i -> {

            // if No error is found then TextToSpeech can perform the translation
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                OpenOptionsTTS.setLanguage(Locale.getDefault());

                //Let the user know what actions are occurring
                OpenOptionsTTS.speak("Okay, Opening the option menu", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        OpenOptionsTTS.shutdown();
        new CountDownTimer(2000, 1000){
            public void onFinish(){
                OpenOptionsMenu();
            }
            @Override
            public void onTick(long l){}
        }.start();
    }

    public void ChangeOptionsTextToSpeech(boolean alreadyOn){
        ChangeOptionsTTS = new TextToSpeech(getApplicationContext(), i ->{
            // if No error is found then TextToSpeech can perform the translation
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                ChangeOptionsTTS.setLanguage(Locale.getDefault());

                //Let the user know what actions are occurring
                if(alreadyOn){
                    SaveSettings();
                    ChangeOptionsTTS.speak("Okay, I have toggled your text to speech off", TextToSpeech.QUEUE_FLUSH, null, null);
                }else{
                    SaveSettings();
                    ChangeOptionsTTS.speak("Okay, I have toggled your text to speech on", TextToSpeech.QUEUE_FLUSH, null, null);
                }

            }
        });
        ChangeOptionsTTS.shutdown();

        new CountDownTimer(2000, 1000){
            public void onFinish(){
                OpenMainMenu();
            }
            @Override
            public void onTick(long l){}
        }.start();
    }
    /*
     * This function will create the TTS object and tell the user
     *     we are now going to open the camera
     */
    public void CameraOpeningTextToSpeech(){
        //Turning on the the TextToSpeech talker
        OpenCameraTTS = new TextToSpeech(getApplicationContext(), i -> {

            // if No error is found then TextToSpeech can perform the translation
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                OpenCameraTTS.setLanguage(Locale.getDefault());

                //Let the user know what actions are occurring
                OpenCameraTTS.speak("Okay, Opening the Camera. Please place the barcode of the item in-front of the camera", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        OpenCameraTTS.shutdown();

        openScanner();
    }


    /*
     * This function will be called if the spoken command couldn't be understood
     */
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

    /*
     * This function opens the Barcode Scanning Activity
     */
    private void openScanner() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CamActivity.class);
        barLauncher.launch(options);
    }

    /*
     * Opening the Options menu
     */
    public void OpenOptionsMenu(){
        Intent OptionsMenu = new Intent(SpeechText.this, OptionsMenu.class);
        startActivity(OptionsMenu);
    }

    public void OpenMainMenu(){
        Intent MainMenu = new Intent(SpeechText.this, MainActivity.class);
        startActivity(MainMenu);
    }

}




