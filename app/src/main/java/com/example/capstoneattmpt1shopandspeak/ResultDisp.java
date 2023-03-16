package com.example.capstoneattmpt1shopandspeak;

//All imports for API and app functions
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    String Upc, Name, Cals, Servings;
    //Once the Activity is created, do the following
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_disp);


        //Create an intent so we can pull the barcode from the StringExtra passed in the other Activity
        Intent i = getIntent();
        Upc = i.getStringExtra("bcode");
        Name = i.getStringExtra("nameOfItem");
        Servings = i.getStringExtra("Servings");
        Cals = i.getStringExtra("CaloriesPerServing");



        //Create strings of the inputted data so we can write it to the txt file
        Upc = Upc + ",";
        String Item = Name + ",";
        String Serv = Servings + ",";
        String Clrs = Cals;


        //Call our WriteFile function to write the record to the end of the file
        WriteFile(Item,Serv,Clrs);


        Intent x = new Intent(ResultDisp.this, ProductDisplay.class);
        x.putExtra("barcode", Upc);
        startActivity(x);

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
            System.arraycopy(Upc.getBytes(), 0, Record, 0, Upc.length());
            System.arraycopy(Itname.getBytes(), 0, Record, Upc.length(), Itname.length());
            System.arraycopy(serv.getBytes(), 0, Record, Upc.length()+Itname.length(), serv.length());
            System.arraycopy(calrs.getBytes(), 0, Record, Upc.length()+Itname.length()+serv.length(), calrs.length());


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
                fileOutputStream.close();


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
