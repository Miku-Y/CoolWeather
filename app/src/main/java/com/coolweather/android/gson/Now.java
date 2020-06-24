package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("cond_txt")
    public String info;//天气信息
    @SerializedName("fl")
    public String fl;//体感温度
    @SerializedName("hum")
    public String hum;//相对湿度
    @SerializedName("pcpn")
    public String pcpn;//降水量
    @SerializedName("pres")
    public String pres;//大气压强
    @SerializedName("tmp")
    public String temperature;//温度
    @SerializedName("vis")
    public String vis;//能见度
    @SerializedName("wind_dir")
    public String dir;//风向
    @SerializedName("wind_sc")
    public String sc;//风力
    @SerializedName("wind_spd")
    public String spd;//风速


}

