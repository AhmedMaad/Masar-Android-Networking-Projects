package com.maad.ipapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    }

    public void getLocation(View view) {

        ProgressBar progress = findViewById(R.id.progress);
        Button btn = findViewById(R.id.btn);

        progress.setVisibility(View.VISIBLE);
        btn.setVisibility(View.INVISIBLE);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://ip-api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CallableInterface callable = retrofit.create(CallableInterface.class);
        Call<IPModel> ipModelCall = callable.getData();
        ipModelCall.enqueue(new Callback<IPModel>() {
            @Override
            public void onResponse(Call<IPModel> call, Response<IPModel> response) {
                progress.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);
                IPModel ip = response.body();
                String country = ip.getCountry();
                String city = ip.getCity();
                Toast.makeText(MainActivity.this, (city + ", " + country + ".")
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IPModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);
                Log.d("trace", "Error: " + t.getLocalizedMessage());
            }
        });

    }

}