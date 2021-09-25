package com.maad.newsappversion6;

public class FavoritesModel {

    private String userId;
    private String title;
    private String url;

    public FavoritesModel(){}

    public FavoritesModel(String userId, String title, String url) {
        this.userId = userId;
        this.title = title;
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

}
