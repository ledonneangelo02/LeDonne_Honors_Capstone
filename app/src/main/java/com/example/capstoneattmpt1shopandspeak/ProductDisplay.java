package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneattmpt1shopandspeak.model.Products;
import com.example.capstoneattmpt1shopandspeak.retrofit.ProductApi;
import com.example.capstoneattmpt1shopandspeak.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDisplay extends AppCompatActivity {

    String upcResults = "";
    String ProductName = "";
    String ProductServingSize = "";
    String ProductCalories = "";
    String ProductServingCount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);


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

    /*
     * This function will load the results from the query into the Display fields in the Activity
     */
    private void loadProduct(TextView ProdName, TextView ProdServingSize, TextView ProdCalories, TextView ProdServingCount){

        ProdName.setText(ProductName);
        ProdServingSize.setText(ProductServingSize);
        ProdCalories.setText(ProductCalories);
        ProdServingCount.setText(ProductServingCount);

    }

    /*
     * This function will return to the main activity if the 'Return Home' button is clicked
     */
    public void ReturnToHome(){
        Intent GoHome = new Intent(ProductDisplay.this, MainActivity.class);
        startActivity(GoHome);
    }

    /*
     * This function will Open the scanner activity if the 'Open Camera' button is clicked
     */
    public void ReturnToCamera(){

    }


}


