package com.maad.newsappversion6;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Activity activity;
    private ArrayList<Article> articles;
    private List<FavoritesModel> favorites;

    public NewsAdapter(Activity activity, ArrayList<Article> articles) {
        this.activity = activity;
        this.articles = articles;
    }

    public void setFavorites(List<FavoritesModel> favorites) {
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity
                .getLayoutInflater()
                .inflate(R.layout.news_list_item, parent, false);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
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

        holder.dotsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(activity, holder.dotsIV);
                //inflating menu from xml resource
                popup.inflate(R.menu.news_list_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_share:
                                new ShareCompat
                                        .IntentBuilder(activity)
                                        .setType("text/plain")
                                        .setChooserTitle("Share News Link with: ")
                                        .setText(articles.get(position).getUrl())
                                        .startChooser();
                                return true;

                            case R.id.item_favorites:
                                boolean isArticleExist = false;
                                String title = articles.get(position).getTitle();
                                String url = articles.get(position).getUrl();
                                FavoritesModel model =
                                        new FavoritesModel(Constants.USER_ID, title, url);

                                for (FavoritesModel element : favorites) {
                                    if (title.equals(element.getTitle())
                                            && element.getUserId().equals(Constants.USER_ID)) {
                                        Toast.makeText(activity
                                                , "Item exists in favorites"
                                                , Toast.LENGTH_SHORT).show();
                                        isArticleExist = true;
                                        break;
                                    }

                                }
                                if (!isArticleExist){
                                    favorites.add(model);
                                    FirebaseFirestore
                                            .getInstance()
                                            .collection("favorites")
                                            .add(model)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful())
                                                        Toast.makeText(activity
                                                                , "Article Added to Favorites"
                                                                , Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }


                                return true;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
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
