package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;


public class Forecast {
    @SerializedName("cond_txt_d")
    public String info;//白天天气状况描述
    @SerializedName("date")
    public String date;//预报日期
    @SerializedName("tmp_max")
    public String max;//最高温度
    @SerializedName("tmp_min")
    public String min;//最低温度
    @SerializedName("wind_dir")
    public String dir;//风向
    @SerializedName("wind_sc")
    public String sc;//风力
}



