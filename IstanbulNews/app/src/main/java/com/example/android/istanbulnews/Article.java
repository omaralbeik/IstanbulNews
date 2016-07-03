package com.example.android.istanbulnews;

/**
 * Created by omar on 7/3/16.
 */

public class Article {

    private String title;
    private String date;
    private String url;
    private String thumbURL;


    public Article(String title, String date, String url, String thumbURL) {

        this.title = title;
        this.date = date;
        this.url = url;
        this.thumbURL = thumbURL;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbURL() {
        return thumbURL;
    }
}
