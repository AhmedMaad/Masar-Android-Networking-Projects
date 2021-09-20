package com.maad.ipapigooglemaps;

import com.google.gson.annotations.SerializedName;

public class IPModel {

    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
