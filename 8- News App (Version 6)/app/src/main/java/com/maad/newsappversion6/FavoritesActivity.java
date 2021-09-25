package com.maad.newsappversion6;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setTitle("Favorites");

        FirebaseFirestore
                .getInstance()
                .collection("favorites")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<FavoritesModel> favorites =
                                queryDocumentSnapshots.toObjects(FavoritesModel.class);
                        ArrayList<String> titles = new ArrayList<>();
                        ArrayList<String> urls = new ArrayList<>();
                        for (FavoritesModel element : favorites) {
                            if (element.getUserId().equals(Constants.USER_ID)) {
                                titles.add(element.getTitle());
                                urls.add(element.getUrl());
                            }
                        }
                        ArrayAdapter adapter =
                                new ArrayAdapter(FavoritesActivity.this
                                        , android.R.layout.simple_list_item_1, titles);
                        ListView list = findViewById(R.id.lv);
                        list.setAdapter(adapter);

                        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                //label is there only for developers, not for app user
                                ClipData clip = ClipData.newPlainText("N/A", urls.get(position));
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(FavoritesActivity.this, "URL Copied to Clipboard"
                                        , Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
                    }
                });

    }

}