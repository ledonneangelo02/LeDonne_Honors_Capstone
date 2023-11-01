package com.example.capstoneattmpt1shopandspeak.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initalizeRetrofit();
    }

    private void initalizeRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://capstoneapi.jollyglacier-2cb4ad63.eastus.azurecontainerapps.io")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
}
