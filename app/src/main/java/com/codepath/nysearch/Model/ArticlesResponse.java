package com.codepath.nysearch.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by JaneChung on 2/14/16.
 */
public class ArticlesResponse {
    ArrayList<Article> articles;

    public ArticlesResponse() {
        articles = new ArrayList<>();
    }

    public static ArrayList<Article> parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        ArrayList<Article> articlesResponse = gson.fromJson(response, ArrayList.class);
        return articlesResponse;
    }
}
