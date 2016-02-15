package com.codepath.nysearch.Model;

import org.parceler.Parcel;

/**
 * Created by JaneChung on 2/14/16.
 */
@Parcel
public class Multimedium {

    private int width;
    private String url;
    private int height;

    public Multimedium() {

    }

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return "http://www.nytimes.com/" + url;
    }

    public int getHeight() {
        return height;
    }
}
