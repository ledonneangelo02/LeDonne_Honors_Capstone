package com.example.capstoneattmpt1shopandspeak;


//All imports needed for API and app functions

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECORD_AUDIO;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


/*
    This Class is the 'Main' class of the application, it will
    drive the opening of the camera as well as the barcode scanning API.
    After the barcode has been scanned, the data will be passed onto the
    ProductDisplay class where we will continue the reset of the processing.
 */
public class MainActivity extends AppCompatActivity {

    Button btnSend; //Button used to open the camera
    ImageView Pass;

    //'When this Activity opens' - begin listening for a button click
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Check to make sure we have all the permissions needed to perform the actions
        //inside of the application
        if(!checkPermission()){
            requestPermission();
        }

        Pass = findViewById(R.id.buttonMic);

        Pass.setOnClickListener(view -> {
            Intent SpeechToText = new Intent(MainActivity.this, SpeechText.class);
            startActivity(SpeechToText);
        });

        //Setting btnSend to the button in the xml
        btnSend = findViewById(R.id.buttonCam);

        //When the button is clicked, call the openScanner() function
        btnSend.setOnClickListener(v -> openScanner());
    }

    /*
    This function will check to make sure we have all the permissions needed to run the app
    and it will return a bool of true or false, if we have ALL the permissions then we'll return
    true, otherwise we will return false.
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return
        result == PackageManager.PERMISSION_GRANTED &&
        result1 == PackageManager.PERMISSION_GRANTED &&
        result2 == PackageManager.PERMISSION_GRANTED &&
        result3 == PackageManager.PERMISSION_GRANTED;
    }

    /*
    This function is responsible for requesting the permissions we don't currently have.
     */
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, INTERNET, RECORD_AUDIO}, 1);

    }

    /*
    This function will open the Barcode scanner and prepare the API for capture
        in this function we will also enable all the options needed to scan the barcodes.
     */
    private void openScanner() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CamActivity.class);
        barLauncher.launch(options);

    }

    //After we scan a barcode, this tells us to pass the data to the other Activities in the app so we
    // can use it as needed
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Results");
            builder.setMessage(result.getContents());

            //Passing the UPC string to the other activity that we will use to search our database and display information
            Intent ResPass = new Intent(this, ProductDisplay.class);
            ResPass.putExtra("barcode", result.getContents());
            startActivity(ResPass);


            //This option should never happen, this means data was corrupted hardcore
        }else{
            Log.e("ScannerError", "No Data was found in the scanner");
        }

    });

}


