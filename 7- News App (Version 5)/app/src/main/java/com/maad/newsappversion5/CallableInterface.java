package com.maad.newsappversion5;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallableInterface {

    //TODO: Replace "API_KEY" part with your actual api key.
    @GET("/v2/top-headlines?apiKey=d03d6de4436b43598fa814f34409a515")
    Call<NewsModel> getNews(@Query("category") String cat, @Query("country") String code);

}
