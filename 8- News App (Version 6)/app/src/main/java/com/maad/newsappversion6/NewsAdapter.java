package com.maad.newsappversion6;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Activity activity;
    private ArrayList<Article> articles;

    public NewsAdapter(Activity activity, ArrayList<Article> articles) {
        this.activity = activity;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity
                .getLayoutInflater()
                .inflate(R.layout.news_list_item, parent, false);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.titleTV.setText(articles.get(position).getTitle());

        String imageLink = articles.get(position).getUrlToImage();

        if (imageLink != null)
            Picasso
                    .get()
                    .load(imageLink)
                    .into(holder.imageIV);
        else
            holder.imageIV.setImageResource(R.drawable.ic_broken_image);

        holder.card.setOnClickListener(v -> {
            Uri articleLink = Uri.parse(articles.get(position).getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, articleLink);
            activity.startActivity(intent);
        });

        holder.dotsIV.setOnClickListener(v -> {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(activity, holder.dotsIV);
            //inflating menu from xml resource
            popup.inflate(R.menu.news_item_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_share:
                        new ShareCompat
                                .IntentBuilder(activity)
                                .setType("text/plain")
                                .setChooserTitle("Share link with: ")
                                .setText(articles.get(position).getUrl())
                                .startChooser();
                        return true;

                    case R.id.item_favorites:
                        DBHelper helper = new DBHelper(activity);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("title", articles.get(position).getTitle());
                        values.put("url", articles.get(position).getUrl());
                        long rowID = db.insert("Favorites", null, values);
                        if (rowID != -1)
                            Toast.makeText(activity, "Added to favorites", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(activity, "Item already exists in favorites"
                                    , Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            });
            //displaying the popup
            popup.show();
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTV;
        private ImageView imageIV;
        private CardView card;
        private ImageView dotsIV;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_title);
            imageIV = itemView.findViewById(R.id.iv_image);
            card = itemView.findViewById(R.id.card_view);
            dotsIV = itemView.findViewById(R.id.iv_dots);
        }
    }

}
