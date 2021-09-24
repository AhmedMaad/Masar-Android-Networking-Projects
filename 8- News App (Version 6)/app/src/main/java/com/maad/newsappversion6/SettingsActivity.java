package com.maad.newsappversion6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        RadioGroup group = findViewById(R.id.rg);

        SharedPreferences prefs =
                getSharedPreferences("settings", MODE_PRIVATE);
        String savedCode = prefs.getString("code", "us");
        switch (savedCode) {
            case "us":
                group.check(R.id.rb_us);
                break;

            case "de":
                group.check(R.id.rb_de);
                break;

            case "jp":
                group.check(R.id.rb_jp);
                break;
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_us:
                        saveSelectedCountry("us");
                        break;
                    case R.id.rb_de:
                        saveSelectedCountry("de");
                        break;
                    case R.id.rb_jp:
                        saveSelectedCountry("jp");
                }
            }
        });

    }

    private void saveSelectedCountry(String countryCode) {
        SharedPreferences.Editor editor =
                getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("code", countryCode);
        editor.apply();
        Toast.makeText(this, "Country Changed Successfully", Toast.LENGTH_SHORT).show();
    }

}