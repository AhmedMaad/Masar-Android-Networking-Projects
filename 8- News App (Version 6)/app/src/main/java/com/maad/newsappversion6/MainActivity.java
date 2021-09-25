package com.maad.newsappversion6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout refresh;
    private String receivedCat;
    private List<FavoritesModel> favorites = new ArrayList<>();
    private ArrayList<Article> articles = new ArrayList<>();
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receivedCat = getIntent().getStringExtra("cat");
        setTitle(receivedCat + " news");

        loadNews();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        refresh = findViewById(R.id.refresh_swipe);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews();
            }
        });

    }

    private void loadNews() {

        progress = findViewById(R.id.progress);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SharedPreferences prefs =
                getSharedPreferences("settings", MODE_PRIVATE);
        String savedCode = prefs.getString("code", "us");

        CallableInterface callable = retrofit.create(CallableInterface.class);
        Call<NewsModel> newsModelCall = callable.getNews(receivedCat, savedCode);
        newsModelCall.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                refresh.setRefreshing(false);
                NewsModel news = response.body();
                articles = news.getArticles();
                readFavorites();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Log.d("trace", "Error: " + t.getLocalizedMessage());
            }
        });

    }

    private void showNews() {
        RecyclerView recyclerView = findViewById(R.id.rv);
        NewsAdapter adapter = new NewsAdapter(this, articles, favorites);
        recyclerView.setAdapter(adapter);
    }

    private void readFavorites() {
        progress.setVisibility(View.INVISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db
                .collection("favorites")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        favorites = queryDocumentSnapshots.toObjects(FavoritesModel.class);
                        showNews();
                    }
                });
    }

}