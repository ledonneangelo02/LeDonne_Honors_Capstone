package com.example.capstoneattmpt1shopandspeak;

//All imports for API and app functions

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneattmpt1shopandspeak.model.Products;
import com.example.capstoneattmpt1shopandspeak.retrofit.ProductApi;
import com.example.capstoneattmpt1shopandspeak.retrofit.RetrofitService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddProduct extends AppCompatActivity {

    //The UPC Code that needs to be added to the database
    String Upc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Getting the UPC code from the ProductDisplay Activity
        Intent i = getIntent();
        Upc = i.getStringExtra("Upc");
        InitializeProduct();
    }

    private void InitializeProduct(){
        EditText Productname = findViewById(R.id.ItemName);
        EditText ProductServing = findViewById(R.id.SerSize);
        EditText ProductCalories = findViewById(R.id.CalServing);
        EditText ProductServingCount = findViewById(R.id.ServingCount);
        Button SaveProduct = findViewById(R.id.GenerateProductButton);

        RetrofitService retrofitService = new RetrofitService();
        ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);

        SaveProduct.setOnClickListener(v -> {

            String Name = String.valueOf(Productname.getText());
            String Serving = String.valueOf(ProductServing.getText());
            String Calories = String.valueOf(ProductCalories.getText());
            String ServingCount = String.valueOf(ProductServingCount.getText());

            Products product = new Products();
            product.setUpcId(Upc);
            product.setName(Name);
            product.setServingSize(Serving);
            product.setCalories(Calories);
            product.setServingCount(ServingCount);

            productApi.createProduct(product)
                    .enqueue(new Callback<Products>() {
                        @Override
                        public void onResponse(@NonNull Call<Products> call, Response<Products> response) {

                        }
                        @Override
                        public void onFailure(Call<Products> call, Throwable t) {

                        }
                    });
            });
    }
}
