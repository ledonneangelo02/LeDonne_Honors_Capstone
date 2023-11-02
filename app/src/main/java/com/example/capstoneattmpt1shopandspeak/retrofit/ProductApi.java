package com.example.capstoneattmpt1shopandspeak.retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

import com.example.capstoneattmpt1shopandspeak.model.Products;

import java.util.List;

public interface ProductApi {

    @GET("/products/search")
    Call<List<Products>> getProductbyUPC(@Query("upcId") String upcCode);

    @POST("/products/create")
    Call<Products> createProduct(@Body Products product);
}
