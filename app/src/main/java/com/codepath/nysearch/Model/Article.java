package com.codepath.nysearch.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JaneChung on 2/9/16.
 */
public class Article implements Serializable{

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    String webUrl;
    String headline;
    String thumbnailUrl;

    public Article(JSONObject jsonObject) {
        try {

            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() <= 0) {
                this.thumbnailUrl = "";
            } else {
                JSONObject media = multimedia.getJSONObject(0);
                this.thumbnailUrl = "http://www.nytimes.com/" + media.getString("url");
            }
        } catch (JSONException e) {

        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
