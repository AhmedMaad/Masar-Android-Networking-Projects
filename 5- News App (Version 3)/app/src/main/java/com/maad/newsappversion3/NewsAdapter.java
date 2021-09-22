package com.maad.newsappversion3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri articleLink = Uri.parse(articles.get(position).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, articleLink);
                activity.startActivity(intent);
            }
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

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_title);
            imageIV = itemView.findViewById(R.id.iv_image);
            card = itemView.findViewById(R.id.card_view);
        }
    }
}
