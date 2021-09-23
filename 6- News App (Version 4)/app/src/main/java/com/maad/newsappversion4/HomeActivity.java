package com.maad.newsappversion4;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.item_favorites:
                Intent i = new Intent(this, FavoritesActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}