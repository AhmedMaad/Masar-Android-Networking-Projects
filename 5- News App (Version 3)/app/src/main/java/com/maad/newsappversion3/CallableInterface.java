package com.maad.newsappversion3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallableInterface {

    //TODO: Replace "API_KEY" part with your actual api key.
    @GET("/v2/top-headlines?country=de&apiKey=API_KEY")
    Call<NewsModel> getNews(@Query("category") String cat);

}
