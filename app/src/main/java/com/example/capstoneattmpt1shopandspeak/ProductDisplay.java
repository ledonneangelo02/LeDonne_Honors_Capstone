package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions

import android.content.Intent;
import android.os.Bundle;
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
        upcResults = i.getStringExtra("barcode");
        loadProduct(upcResults, ProdName, PrintedServingSize, ProdCalories, ProdServingCount);
    }

    /*
     * This function will load the results from the query into the Display fields in the Activity
     */
    private void loadProduct(String upcResult, TextView ProductName, TextView PrintedServingSize, TextView ProductCalories, TextView ProductServingCount){

        RetrofitService retrofitService = new RetrofitService();
        ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);

        productApi.getProductbyUPC(upcResult)
                .enqueue(new Callback<List<Products>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Products>> call, @NonNull Response<List<Products>> response) {
                        assert response.body() != null;
                        if(response.body().isEmpty()) {
                            InsertNewProduct();
                        }else {
                            ProductName.setText(response.body().get(0).getName());
                            PrintedServingSize.setText(response.body().get(0).getServingSize());
                            ProductCalories.setText(response.body().get(0).getCalories());
                            ProductServingCount.setText(response.body().get(0).getServingCount());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Products>> call, @NonNull Throwable t) {
                        Toast.makeText(ProductDisplay.this, "Failed to load product", Toast.LENGTH_SHORT).show();
                    }
                });
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

    /*
     * If the UPC code that was scanned is not found, then open the Add Product Activity
     */
    public void InsertNewProduct(){
        Intent InsertNewProd = new Intent(ProductDisplay.this, AddProduct.class);
        InsertNewProd.putExtra("Upc", upcResults);
    }

}


