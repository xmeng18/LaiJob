package com.laioffer.githubexample.remote.response;
import com.google.gson.annotations.SerializedName;
import com.laioffer.githubexample.R;
import com.laioffer.githubexample.ui.comment.Item;

import java.io.Serializable;
import java.util.List;

public class Job extends Item implements Serializable {
    @SerializedName("address")
    public String address;

    @SerializedName("keywords")
    public List<String> keywords;

    @SerializedName("item_id")
    public String itemId;

    @SerializedName("description")
    public String description;

    @SerializedName("type")
    public String type;

    @SerializedName("url")
    public String url;

    @SerializedName("name")
    public String name;

    @SerializedName("company")
    public String company;

    @SerializedName("favorite")
    public boolean favorite;

    @SerializedName("location")
    public Location location;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("time")
    public String time;

    //    @SerializedName("lat")
//    public double lat;
//
//    @SerializedName("lon")
//    public double lon;

}
