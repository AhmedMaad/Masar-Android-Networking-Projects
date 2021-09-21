package com.maad.newsappversion1;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CallableInterface {

    @GET("/v2/top-headlines?country=de&category=business&apiKey=d03d6de4436b43598fa814f34409a515")
    Call<NewsModel> getNews();

}
