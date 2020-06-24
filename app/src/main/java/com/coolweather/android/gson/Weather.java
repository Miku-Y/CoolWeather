package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    //Weather类作为总的实例类来引用以上各个实体类

    public String status;

    public Basic basic;

    public Update update;

    public Now now;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
