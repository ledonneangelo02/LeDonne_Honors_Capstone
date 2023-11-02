package com.example.capstoneattmpt1shopandspeak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.capstoneattmpt1shopandspeak.model.Products;
import com.example.capstoneattmpt1shopandspeak.retrofit.ProductApi;
import com.example.capstoneattmpt1shopandspeak.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingScreen extends AppCompatActivity {

    String upcResults = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);


        Intent i = getIntent();
        upcResults = i.getStringExtra("barcode");
        loadProduct();
    }

    /*
     * This function will load the results from the query into the Display fields in the Activity
     */
    private void loadProduct(){

        RetrofitService retrofitService = new RetrofitService();
        ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);

        productApi.getProductbyUPC(upcResults)
                .enqueue(new Callback<List<Products>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Products>> call, @NonNull Response<List<Products>> response) {
                        assert response.body() != null;
                        Log.i("Response Body:", response.body().toString());
                        if(response.body().isEmpty()) {
                            InsertNewProduct();
                        }else {
                            DisplayProduct(response.body().get(0));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Products>> call, @NonNull Throwable t) {
                        Toast.makeText(LoadingScreen.this, "Failed to load product", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
     * If the UPC code that was scanned is not found, then open the Add Product Activity
     */
    public void InsertNewProduct(){
        Intent InsertNewProd = new Intent(LoadingScreen.this, AddProduct.class);
        InsertNewProd.putExtra("Upc", upcResults);
        startActivity(InsertNewProd);
    }

    public void DisplayProduct(Products products){
        Intent DisplayProd = new Intent(LoadingScreen.this, ProductDisplay.class);
        DisplayProd.putExtra("upcId",products.getUpcId());
        DisplayProd.putExtra("ProdName", products.getName());
        DisplayProd.putExtra("ProdServingSize", products.getServingSize());
        DisplayProd.putExtra("ProdCalories",products.getCalories());
        DisplayProd.putExtra("ProdServingCount",products.getServingCount());
        startActivity(DisplayProd);
    }
}