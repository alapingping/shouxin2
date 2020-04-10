package com.shouxin.shouxin.API;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @Headers("Content-Type:application/json")
    @POST("{modelName}")
    Call<ResponseBody> UploadPicture(@Path ("modelName") String modelName, @Query("access_token") String access_token, @Body RequestBody info);

    @GET("/api/user/login")
    Call<ResponseBody> login(@Query("username") String username, @Query("password") String password);

    @GET("/api/message/getAll")
    Call<ResponseBody> getAllMessages();

    @POST("/api/message/publishMessage")
    Call<ResponseBody> publishMessage(@Body RequestBody body);

    @GET("/api/word/getAll")
    Call<ResponseBody> getAllWords();

    @GET("/api/word/getWords")
    Call<ResponseBody> getWordsByCategory(@Query("category") String category);

    @GET("/api/word/getCategories")
    Call<ResponseBody> getCategories();

}
