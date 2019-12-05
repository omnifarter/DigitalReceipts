package com.example.digitalreceipts.CameraOCR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClearbitResults {

    @SerializedName("logo")
    @Expose
    private String logo;


    @SerializedName("tags")
    @Expose
    private List<String> tags = null;


    public String getLogo() {
        return logo;
    }

    public String getCategory() {
        return tags.get(0);
    }
}
