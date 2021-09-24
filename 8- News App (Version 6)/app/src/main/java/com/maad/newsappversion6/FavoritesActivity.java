package com.maad.newsappversion6;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setTitle("Favorites");

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Favorites", null);

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();

        while (cursor.moveToNext()) {
            titles.add(cursor.getString(0));
            urls.add(cursor.getString(1));
        }

        ArrayAdapter adapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        ListView list = findViewById(R.id.lv);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //label is there only for developers, not for app user
                ClipData clip = ClipData.newPlainText("N/A", urls.get(position));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(FavoritesActivity.this, "Title Copied to Clipboard"
                        , Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

}