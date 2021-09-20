package com.maad.ipapigooglemaps;


import retrofit2.Call;
import retrofit2.http.GET;

public interface CallableInterface {

    @GET("/json")
    Call<IPModel> getData();

}
