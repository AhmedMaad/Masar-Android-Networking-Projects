package com.maad.newsappversion3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
    }

    public void openGeneralNews(View view) {
        openNewsActivity("general");
    }

    public void openHealthNews(View view) {
        openNewsActivity("health");
    }

    public void openSportsNews(View view) {
        openNewsActivity("sports");
    }

    public void openTechnologyNews(View view) {
        openNewsActivity("technology");
    }

    private void openNewsActivity(String category) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cat", category);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.setCancelable(false);
        exitDialog.show(getSupportFragmentManager(), null);
    }
}