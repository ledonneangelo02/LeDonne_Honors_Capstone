package com.example.capstoneattmpt1shopandspeak;

//All imports for API and app functions

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;



/**
    This class holds the ResultDisp Activity which is responsible for
    allowing the user to input a new item into our 'database' internal txt file
 */
public class ResultDisp extends AppCompatActivity {

    String Upc, Name, Cals, Servings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_disp);



        Intent i = getIntent();
        Upc = i.getStringExtra("bcode");
        Name = i.getStringExtra("nameOfItem");
        Servings = i.getStringExtra("Servings");
        Cals = i.getStringExtra("CaloriesPerServing");


        String tempUPC = Upc;
        Upc = Upc + ",";
        String Item = Name + ",";
        String Serv = Servings + ",";
        String Clrs = Cals;


        WriteFile(Item,Serv,Clrs);


        Intent x = new Intent(ResultDisp.this, ProductDisplay.class);
        x.putExtra("barcode", tempUPC);
        startActivity(x);

    }

    /**
        This method is responsible for writing the record we created into a 132 byte array
        which will then be written directly to a txt file called "Records.txt" in the internal 
        storage of the device, it will append to the end of the file or create a new file if it 
        doesn't already exist 
     */
        public void WriteFile(String Itname, String serv, String calrs){

            byte [] Record = new byte[132];

            System.arraycopy(Upc.getBytes(), 0, Record, 0, Upc.length());
            System.arraycopy(Itname.getBytes(), 0, Record, Upc.length(), Itname.length());
            System.arraycopy(serv.getBytes(), 0, Record, Upc.length()+Itname.length(), serv.length());
            System.arraycopy(calrs.getBytes(), 0, Record, Upc.length()+Itname.length()+serv.length(), calrs.length());


            FileOutputStream fileOutputStream = null;

            try {

                fileOutputStream = openFileOutput("Records.txt", Context.MODE_APPEND);

                fileOutputStream.write(Record);
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();


            } catch (Exception ex) {
                ex.printStackTrace();

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
