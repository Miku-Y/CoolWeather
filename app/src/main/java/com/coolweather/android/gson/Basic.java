package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("location")
    public String cityName;//城市名

    @SerializedName("cid")
    public String weatherId;//城市对应的天气的id

    @SerializedName("lat")
    public String cityLat;//城市的经度

    @SerializedName("lon")
    public String cityLon;//城市的纬度

}


