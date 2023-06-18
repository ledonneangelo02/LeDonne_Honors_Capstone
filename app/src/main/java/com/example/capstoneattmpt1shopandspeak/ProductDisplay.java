package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Locale;
import java.util.Objects;



public class ProductDisplay extends AppCompatActivity {

    String Rez;
    String [] recordCols;
    boolean found_flag = false;
    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);


        String fileST = getFilesDir() + "/" + "Records.txt";


        Intent i = getIntent();
        Rez = i.getStringExtra("barcode");


        Button thisButton = findViewById(R.id.buttonReturn);
        TextView MyTextView = findViewById(R.id.BarcodeID);
        MyTextView.setText(Rez);


        readInternal(fileST);


        textToSpeech = new TextToSpeech(getApplicationContext(), i1 -> {

               if(i1 != TextToSpeech.ERROR){

                  textToSpeech.setLanguage(Locale.getDefault());


                  textToSpeech.speak(recordCols[1], TextToSpeech.QUEUE_FLUSH, null, null);


                  textToSpeech.speak("The Serving Size is ", TextToSpeech.QUEUE_ADD, null, null);
                  textToSpeech.speak(recordCols[2], TextToSpeech.QUEUE_ADD, null, null);


                  textToSpeech.speak("And, each Serving has ", TextToSpeech.QUEUE_ADD, null, null);
                  textToSpeech.speak(recordCols[3], TextToSpeech.QUEUE_ADD, null, null);
                  textToSpeech.speak(" Calories", TextToSpeech.QUEUE_ADD, null, null);

                  new CountDownTimer(13000, 1000){
                      public void onFinish(){
                          textToSpeech.speak("Please use the Scan More Items button if you wish to continue", TextToSpeech.QUEUE_ADD, null, null);
                      }
                      @Override
                      public void onTick(long l) {}
                  }.start();
                }
            });
            textToSpeech.shutdown();

            thisButton.setOnClickListener(view -> {
                textToSpeech.stop();
                textToSpeech.shutdown();
                Intent x = new Intent(this, SpeechText.class);
                startActivity(x);
            });

    }



    private void readInternal(String fileST) {

        File file = new File(fileST);

        if (file.exists()) {

            FileInputStream fis;
            String textContent;
            try {


                fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new FileReader(fileST));


                textContent = br.readLine();


                while (textContent != null) {

                    if (Objects.equals(Rez, textContent.substring(0, Rez.length()))) {

                        found_flag = true;
                        String Rec1 = "Item: " + recordCols[1];
                        String Rec2 = "Serving Size: " +recordCols[2];
                        String Rec3 = recordCols[3] + "Calories";

                        recordCols = textContent.split(",");

                        TextView ItemName = findViewById(R.id.PrintedName);
                        ItemName.setText(Rec1);


                        TextView ServingSize = findViewById(R.id.PrintedServingSize);
                        ServingSize.setText(Rec2);


                        TextView Cals = findViewById(R.id.PrintedCals);
                        Cals.setText(Rec3);

                        fis.close();
                    }

                    textContent = br.readLine();
                }

                fis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}


