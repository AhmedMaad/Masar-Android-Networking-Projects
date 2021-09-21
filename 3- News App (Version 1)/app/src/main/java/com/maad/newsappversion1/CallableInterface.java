package com.maad.newsappversion1;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CallableInterface {

    //TODO: Replace "API_KEY" part with your actual api key.
    @GET("/v2/top-headlines?country=de&category=business&apiKey=API_KEY")
    Call<NewsModel> getNews();

}
