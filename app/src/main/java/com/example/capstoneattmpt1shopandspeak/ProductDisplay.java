package com.example.capstoneattmpt1shopandspeak;

//Imports needed for API and app functions

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import sqlj.runtime.profile.util.CustomizerHarness;


public class ProductDisplay extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        TextView queryResultTextView = findViewById(R.id.queryResultTextView);
        TextView ProductName = findViewById(R.id.PrintedName);
        TextView PrintedServingSize = findViewById(R.id.PrintedServingSize);
        TextView ProductCalories = findViewById(R.id.PrintedCals);

        Intent i = getIntent();
        String upcResults = i.getStringExtra("barcode");
        loadProduct(upcResults, ProductName, PrintedServingSize, ProductCalories);

        queryResultTextView.setText(upcResults);


    }
    private void loadProduct(String upcResult, TextView ProductName, TextView PrintedServingSize, TextView ProductCalories){

        RetrofitService retrofitService = new RetrofitService();
        ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);

        productApi.getProductbyUPC(upcResult)
                .enqueue(new Callback<List<Products>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Products>> call, @NonNull Response<List<Products>> response) {
                        assert response.body() != null;
                        String x = response.body().get(0).getName();

                        ProductName.setText(response.body().get(0).getName());
                        PrintedServingSize.setText(response.body().get(0).getServingSize());
                        ProductCalories.setText(response.body().get(0).getCalories());
                        Log.i("Response Data name: ", x);
                        //populateListView(response.body());
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Products>> call, @NonNull Throwable t) {
                        Toast.makeText(ProductDisplay.this, "Failed to load product", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Products> body) {

    }

}


