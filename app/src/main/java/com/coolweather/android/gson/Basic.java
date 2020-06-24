package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("city")
    public String cityName;//城市名

    @SerializedName("id")
    public String weatherId;//城市对应的天气的id

    public Update update;

    public class Update{

        @SerializedName("loc")
        public String updateTime;//接口更新时间

    }

}

