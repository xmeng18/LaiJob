package com.laioffer.githubexample.remote.response;

import com.google.gson.annotations.SerializedName;

public class Education{
    @SerializedName("schoolName")
    public String schoolName;
    @SerializedName("schoolStartData")
    public String startDate;
    @SerializedName("schoolEndData")
    public String endDate;
}
