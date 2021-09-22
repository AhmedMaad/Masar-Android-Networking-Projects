package com.maad.newsappversion2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadNews();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void loadNews() {

        ProgressBar progress = findViewById(R.id.progress);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CallableInterface callable = retrofit.create(CallableInterface.class);
        Call<NewsModel> newsModelCall = callable.getNews();
        newsModelCall.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                progress.setVisibility(View.INVISIBLE);
                NewsModel news = response.body();
                ArrayList<Article> articles = news.getArticles();
                showNews(articles);
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Log.d("trace", "Error: " + t.getLocalizedMessage());
            }
        });

    }

    private void showNews(ArrayList<Article> articles){
        RecyclerView recyclerView = findViewById(R.id.rv);
        NewsAdapter adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

}