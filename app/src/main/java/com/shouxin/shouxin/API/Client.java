package com.shouxin.shouxin.API;

import java.security.Provider;
import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

//    public static final String BASE_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/";
    public static final String BASE_URL = "http://192.168.1.108:8080";

    public static Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build();
}
