package com.example.capstoneattmpt1shopandspeak;

//All imports for API and app functions
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import java.io.FileOutputStream;
import java.io.IOException;



/*
    This class holds the ResultDisp Activity which is responsible for
    allowing the user to input a new item into our 'database' internal txt file
 */
public class ResultDisp extends AppCompatActivity {

    //Defining our EditText fields from the XML file
    EditText name,serving,cals;
    String UpcCode; // String to hold the UPC number string

    //Once the Activity is created, do the following
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_disp);

        //Create an intent so we can pull the barcode from the StringExtra passed in the other Activity
        Intent i = getIntent();
        UpcCode = i.getStringExtra("bcode");

        //Allocating which EditText field corresponds to the location in the XML layout
        Button thisButton = findViewById(R.id.write);
        name = findViewById(R.id.ItName);
        serving = findViewById(R.id.SerSize) ;
        cals = findViewById((R.id.CalServing));

        //Defaulting serving and cals, in case the item is not grocery
        serving.setText("N/A");
        cals.setText("N/A");

        //Sets the displayed Result at the top of the screen to the UPC code we are entering data about
        TextView MyBarcode = findViewById(R.id.PrintedResults);
        MyBarcode.setText(UpcCode);

        //Listen for the button to be clicked by the user
        thisButton.setOnClickListener(view -> {

            //Create strings of the inputted data so we can write it to the txt file
            UpcCode = UpcCode + ",";
            String Item = name.getText().toString() + ",";
            String Serv = serving.getText().toString() + ",";
            String Clrs = cals.getText().toString();


            //Call our WriteFile function to write the record to the end of the file
            WriteFile(Item,Serv,Clrs);

            //Creating an intent to pass control back to the MainActivity once we wrote our record
            Intent x = new Intent(this, MainActivity.class);
            startActivity(x);

        });
    }

    /*
        This function is responsible for writing the record we created into a 132 byte array
        which will then be written directly to a txt file called "Records.txt" in the internal 
        storage of the device, it will append to the end of the file or create a new file if it 
        doesn't already exist 
     */
        public void WriteFile(String Itname, String serv, String calrs){

            //Record buffer to write out to the file
            byte [] Record = new byte[132];

            //Write all of the strings to the file separated by their commas and no space between
            System.arraycopy(UpcCode.getBytes(), 0, Record, 0, UpcCode.length());
            System.arraycopy(Itname.getBytes(), 0, Record, UpcCode.length(), Itname.length());
            System.arraycopy(serv.getBytes(), 0, Record, UpcCode.length()+Itname.length(), serv.length());
            System.arraycopy(calrs.getBytes(), 0, Record, UpcCode.length()+Itname.length()+serv.length(), calrs.length());


            //Creating an output stream to use for appending to the file
            FileOutputStream fileOutputStream = null;

            try {

                //try to open our 'Records.txt' file in append mode
                fileOutputStream = openFileOutput("Records.txt", Context.MODE_APPEND);

                //write the data to the file followed by a new line for the next record
                //Then we can flush the stream before exiting
                fileOutputStream.write(Record);
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.flush();


                name.setText("");
                serving.setText("");
                cals.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();

            //CLosing our file and error checking to make sure everything went as planned
            } finally {
                try {
                    assert fileOutputStream != null;
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
