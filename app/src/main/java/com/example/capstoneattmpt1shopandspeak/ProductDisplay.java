package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneattmpt1shopandspeak.model.Products;
import com.example.capstoneattmpt1shopandspeak.retrofit.ProductApi;
import com.example.capstoneattmpt1shopandspeak.retrofit.RetrofitService;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDisplay extends AppCompatActivity {

    String upcResults = "";
    String ProductName = "";
    String ProductServingSize = "";
    String ProductCalories = "";
    String ProductServingCount = "";
    TextToSpeech textToSpeech;
    boolean TextToSpeechOnOFF = true;
    //Barcode Init
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Results");
            builder.setMessage(result.getContents());

            Intent ResPass = new Intent(this, LoadingScreen.class);
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
        setContentView(R.layout.activity_product_display);

        SharedPreferences fetchSP = this.getSharedPreferences("AppSettings", MODE_PRIVATE);
        TextToSpeechOnOFF = fetchSP.getBoolean("TTS", true);

        TextView ProdName = findViewById(R.id.ProductName);
        TextView PrintedServingSize = findViewById(R.id.PrintedServingSize);
        TextView ProdCalories = findViewById(R.id.ProductCalories);
        TextView ProdServingCount = findViewById(R.id.ProductServingCount);
        Button ReturnHomeButton = findViewById(R.id.ReturnHomeButton);
        ReturnHomeButton.setOnClickListener(v -> ReturnToHome());
        Button ReturnToCameraButton = findViewById(R.id.CamReturnButton);
        ReturnToCameraButton.setOnClickListener(v -> ReturnToCamera());

        Intent i = getIntent();
        upcResults = i.getStringExtra("upcId");
        ProductName = i.getStringExtra("ProdName");
        ProductServingSize = i.getStringExtra("ProdServingSize");
        ProductCalories = i.getStringExtra("ProdCalories");
        ProductServingCount = i.getStringExtra("ProdServingCount");
        loadProduct(ProdName, PrintedServingSize, ProdCalories, ProdServingCount);

    }


    @Override
    protected void onStart(){
        super.onStart();

    }
    /*
     * This function will load the results from the query into the Display fields in the Activity
     */
    private void loadProduct(TextView ProdName, TextView ProdServingSize, TextView ProdCalories, TextView ProdServingCount){

        ProdName.setText(ProductName);
        ProdServingSize.setText(ProductServingSize);
        ProdCalories.setText(ProductCalories);
        ProdServingCount.setText(ProductServingCount);

        if(TextToSpeechOnOFF) {
            textToSpeech = new TextToSpeech(getApplicationContext(), i -> {

                // if No error is found then TextToSpeech can perform the translation
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.getDefault());
                    //Let the user know what actions are occurring
                    textToSpeech.speak("The product you scanned is " + ProductName, TextToSpeech.QUEUE_FLUSH, null, null);
                    textToSpeech.speak("The serving size is " + ProductServingSize + ".", TextToSpeech.QUEUE_ADD, null, null);
                    textToSpeech.speak("And it has " + ProductCalories + " calories per serving.", TextToSpeech.QUEUE_ADD, null, null);
                    textToSpeech.speak("There are " + ProductServingCount + " servings per container", TextToSpeech.QUEUE_ADD, null, null);
                }
            });
            textToSpeech.shutdown();
        }
    }

    /*
     * This function will return to the main activity if the 'Return Home' button is clicked
     */
    public void ReturnToHome(){
        if(textToSpeech != null) { textToSpeech.stop(); }
        Intent GoHome = new Intent(ProductDisplay.this, MainActivity.class);
        startActivity(GoHome);
    }

    /*
     * This function will Open the scanner activity if the 'Open Camera' button is clicked
     */
    public void ReturnToCamera(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CamActivity.class);
        barLauncher.launch(options);
    }


}


