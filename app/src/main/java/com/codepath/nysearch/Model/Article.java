package com.codepath.nysearch.Model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JaneChung on 2/9/16.
 */
@Parcel
public class Article {
    private String webUrl;
    private List<Multimedium> multimedia = new ArrayList<Multimedium>();
    private Headline headline;

    public String getWebUrl() {
        return webUrl;
    }

    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }


    public Article() {

    }

//    public Article(JSONObject jsonObject) {
//        try {
//
//            this.webUrl = jsonObject.getString("web_url");
//            this.headline = jsonObject.getJSONObject("headline").getString("main");
//
//            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
//            if (multimedia.length() <= 0) {
//                this.thumbnailUrl = "";
//            } else {
//                JSONObject media = multimedia.getJSONObject(0);
//                this.thumbnailUrl = "http://www.nytimes.com/" + media.getString("url");
//            }
//        } catch (JSONException e) {
//
//        }
//    }
//
//    public static ArrayList<Article> fromJSONArray(JSONArray array) {
//        ArrayList<Article> results = new ArrayList<>();
//
//        for (int i = 0; i < array.length(); i++) {
//            try {
//                results.add(new Article(array.getJSONObject(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return results;
//    }
}
