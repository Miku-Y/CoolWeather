package com.coolweather.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;

    private Button navButton;

    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;

    private ScrollView weatherLayout;//滚动视图对象

    private TextView titleCity;//基本信息--城市名

    private TextView titleUpdateTime;//基本信息--更新时间

    private TextView titleLat;//基本信息--经度

    private TextView titleLon;//基本信息--纬度

    private TextView weatherInfoText;//实时天气信息--天气信息

    private TextView flText;//实时天气信息--体感温度

    private TextView humText;//实时天气信息--相对湿度

    private TextView pcpnText;//实时天气信息--降水量

    private TextView presText;//实时天气信息--大气压强

    private TextView tmpText;//实时天气信息--温度

    private TextView visText;//实时天气信息--能见度

    private TextView dirText;//实时天气信息--风向

    private TextView scText;//实时天气信息--风力

    private TextView spdText;//实时天气信息--风速

    private LinearLayout forecastLayout;//线性布局对象--预报天气

    private ImageView bingPicImg;//背景图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //初始化各控件
        bingPicImg = findViewById(R.id.bing_pic_img);//背景图片
        weatherLayout = findViewById(R.id.weather_layout);//滚动视图对象
        titleCity = findViewById(R.id.title_city);//基本信息--城市
        titleUpdateTime = findViewById(R.id.title_update_time);//基本信息--更新时间
        titleLat = findViewById(R.id.lat_text);//基本信息--经度
        titleLon = findViewById(R.id.lon_text);//基本信息--纬度
        weatherInfoText = findViewById(R.id.weather_info_text);//实时天气信息--天气信息
        flText = findViewById(R.id.fl_text) ;//实时天气信息--体感温度
        humText = findViewById(R.id.hum_text);//实时天气信息--相对湿度
        pcpnText = findViewById(R.id.pcpn_text);//实时天气信息--降水量
        presText = findViewById(R.id.pres_text); //实时天气信息--大气压强
        tmpText = findViewById(R.id.tmp_text);//实时天气信息--温度
        visText = findViewById(R.id.vis_text);//实时天气信息--能见度
        dirText = findViewById(R.id.dir_text);//实时天气信息--风向
        scText = findViewById(R.id.sc_text);//实时天气信息--风力
        spdText = findViewById(R.id.spd_text); //实时天气信息--风速
        forecastLayout = findViewById(R.id.forecast_layout);//线性布局对象--预报天气
        drawerLayout = findViewById(R.id.drawer_layout);//DrawerLayout抽屉布局实例
        navButton = findViewById(R.id.nav_button);//Button实例

        swipeRefresh = findViewById(R.id.swipe_refresh);//获取SwipeRefreshLayout的实例
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//调用setColorSchemeResources()方法来设置下拉刷新进度条的颜色
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if(weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            //无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra("weather_id");
            //在请求数据的时候将ScrollView()隐藏
            weatherLayout.setVisibility(View.INVISIBLE);
            //从服务器请求天气数据
            requestWeather(mWeatherId);
        }
        //设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);//请求天气信息
            }
        });
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic != null){
            //如果有缓存数据就直接使用Glide来加载这张图片
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            //如果没有缓存数据就调用loadBingPic()方法去请求今日的必应背景图
            loadBingPic();//加载每日一图
        }
        //请求新选择城市的天气信息
        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    /**
     * 根据天气ID请求城市天气信息
     */
    public void requestWeather(final String weatherId){
        //组装接口地址
        //guolin模拟数据
        //String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=8518f3bef50144e39994370699b08d5e";
        //和风天气API
        String weatherUrl = "https://free-api.heweather.net/s6/weather?location="+weatherId+"&key=55db6c6a7e974af281ed7d679f74abae";
        //向组装好的地址发送请求，服务器会将相应城市的天气信息以JSON()格式返回
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                //将返回的JSON数据转换成Weather对象
                final Weather weather = Utility.handleWeatherResponse(responseText);
                //将当前线程切换到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){//请求天气成功
                            //将返回的数据缓存到SharedPreferences当中
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            //调用showWeatherInfo()方法进行内容显示
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);//刷新事件结束，将进度条隐藏起来
                    }
                });
                //每次请求天气信息的时候也会刷新背景图片
                loadBingPic();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);//刷新事件结束，将进度条隐藏起来
                    }
                });
            }
        });
        loadBingPic();//加载每日一图
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        //调用HttpUtil.sendOkHttpRequest()方法获取必应背景图的链接
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                //将链接缓存到SharedPreferences当中
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                //将线程切换到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //使用Glide来加载图片
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }


    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String updateTime = "更新时间:"+weather.update.updateTime.split(" ")[1];
        String lat = "经度:"+weather.basic.cityLat;
        String lon = "纬度:"+weather.basic.cityLon;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        titleLat.setText(lat);
        titleLon.setText(lon);
        String weatherInfo = weather.now.info;
        String flInfo = weather.now.fl + "℃";
        String humInfo = weather.now.hum;
        String pcpnInfo = weather.now.pcpn + "mm";
        String presInfo = weather.now.pres + "Pa";
        String tmpInfo = weather.now.temperature + "℃";
        String visInfo = weather.now.vis;
        String dirInfo = weather.now.dir;
        String scInfo = weather.now.sc;
        String spdInfo = weather.now.spd + "m/s";
        weatherInfoText.setText(weatherInfo);
        flText.setText(flInfo);
        humText.setText(humInfo);
        pcpnText.setText(pcpnInfo);
        presText.setText(presInfo);
        tmpText.setText(tmpInfo);
        visText.setText(visInfo);
        dirText.setText(dirInfo);
        scText.setText(scInfo);
        spdText.setText(spdInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            TextView dirText = view.findViewById(R.id.dir_text);
            TextView scText = view.findViewById(R.id.sc_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.info);
            maxText.setText(forecast.max);
            minText.setText(forecast.min);
            dirText.setText(forecast.dir);
            scText.setText(forecast.sc);
            forecastLayout.addView(view);
        }
        //在设置完所有数据后，再将ScrollView设为可见
        weatherLayout.setVisibility(View.VISIBLE);
        //激活AutoUpdateService这个服务，只要选中了某个城市并成功更新天气之后，
        // AutoUpdateService就会一直在后台运行，并保证每8个小时更新一次天气
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
