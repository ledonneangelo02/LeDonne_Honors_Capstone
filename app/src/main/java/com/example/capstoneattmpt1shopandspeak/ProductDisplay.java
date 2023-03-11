package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
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



/*
    PUT A INDEPTH INFO HERE FOR THIS ACTIVITY
 */
public class ProductDisplay extends AppCompatActivity {

    //Used for converting speech to text
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    //The string that holds the UPC data as number string
    String Rez;
    //A string Array that will hold the record with the matched UPC code for splitting and parsing
    String [] recordCols;
    //The bool flag used for determining weather the record is found in the database or not (TXT file for now)
    boolean found_flag = false;
    //The TextToSpeech object needed to speak strings that are passed to it
    TextToSpeech textToSpeech;

    //When the activity appears on the screen, we perform these actions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        //String that holds the text files path so we can find it and read it easier
        String fileST = getFilesDir() + "/" + "Records.txt";

        //creating an intent so we can use the getStringExtra that we passed on the previous Activity with the UPC string
        Intent i = getIntent();
        Rez = i.getStringExtra("barcode");

        //Defining the buttons and the TextView used in the XML for the layout
        Button thisButton = findViewById(R.id.buttonReturn);
        TextView MyTextView = findViewById(R.id.BarcodeID);
        MyTextView.setText(Rez);

        //reading the file with all item records in it
        readInternal(fileST);


        //if we didn't find the product in the file, we will pass control to the ResultDisp Activity
        //      So we can add it to our 'database' text file (THIS WILL BE REPLACED WITH WEB SCRAPPING)
        //
        //Else, we must have found the item in the file, and we can TextToSpeech the data and wait for
        //      the user to decide what they would like to do next
        if(!found_flag){
            Intent NewItem = new Intent(this, ResultDisp.class);
            NewItem.putExtra("bcode", Rez);
            startActivity(NewItem);

        }else{

            //Turning on the the TextToSpeech talker
            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

                public void onInit(int i) {

                    // if No error is found then TextToSpeech can perform the translation
                    if(i != TextToSpeech.ERROR){
                        // To Choose language of speech
                        textToSpeech.setLanguage(Locale.getDefault());

                        //This speaks the name of the item scanned
                        textToSpeech.speak(recordCols[1], TextToSpeech.QUEUE_FLUSH, null, null);

                        //This speaks the searving size of the item
                        textToSpeech.speak("The Serving Size is ", TextToSpeech.QUEUE_ADD, null, null);
                        textToSpeech.speak(recordCols[2], TextToSpeech.QUEUE_ADD, null, null);

                        //This speaks the calories per serving
                        textToSpeech.speak("And, each Serving has ", TextToSpeech.QUEUE_ADD, null, null);
                        textToSpeech.speak(recordCols[3], TextToSpeech.QUEUE_ADD, null, null);
                        textToSpeech.speak(" Calories", TextToSpeech.QUEUE_ADD, null, null);

                        //waits for the user to process what was just spoke to them and then lets them know they can press the
                        //button to do it with another item
                        new CountDownTimer(13000, 1000){
                            public void onFinish(){
                                textToSpeech.speak("@string/Return_to_Camera_Req", TextToSpeech.QUEUE_ADD, null, null);
                            }

                            @Override
                            public void onTick(long l) {

                            }
                        }.start();



                    }
                }
            });

            textToSpeech.shutdown();

            //Waiting for the user to click the button to restart the process back at the MainActivity
            thisButton.setOnClickListener(view -> {

                Intent x = new Intent(this, MainActivity.class);
                startActivity(x);

            });
        }

    }


    //This is the function used to read the internal file with the item records
    private void readInternal(String fileST) {

        //Define a file that we will write
        File file = new File(fileST);


        if (file.exists()) {

            FileInputStream fis;
            String textContent;

            try {

                //Creating an Instream reader
                fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new FileReader(fileST));

                //Priming read of the file
                textContent = br.readLine();

                //While there is still data in the file... keep checking if the UPC is in it
                while (textContent != null) {


                    //If the barcode is found in the txt file... then we found the item & it's info
                    //Objects.equals protect against == null case
                    if (Objects.equals(Rez, textContent.substring(0, Rez.length()))) {

                        //set the flag to true since we found the record
                        found_flag = true;

                        //Split the record into substrings and use a comma as the delimitor
                        recordCols = textContent.split(",");

                        //Set the TextView in the activiy to display the item name
                        TextView ItemName = findViewById(R.id.PrintedName);
                        ItemName.setText("Item: " + recordCols[1]);

                        //Set the TextView in the activiy to display the serving size
                        TextView ServingSize = findViewById(R.id.PrintedServingSize);
                        ServingSize.setText("Serving Size: " + recordCols[2]);


                        //Set the TextView in the activiy to display the calories
                        TextView Cals = findViewById(R.id.PrintedCals);
                        Cals.setText(recordCols[3] + " Calories");

                        fis.close();

                    }

                    //Reading the next record in the file
                    textContent = br.readLine();

                }

                //Close the file
                fis.close();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}


