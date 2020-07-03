# CoolWeather
Cool天气-和风天气API真实数据

## 一、实训内容

> 功能需求及技术可行性分析

> 将代码托管到GitHub上

> 创建数据库和表

> 遍历全国省市县数据

> 显示真实天气信息

> 获取必应每日一图

> 手动更新天气和切换城市

> 后台自动更新天气

> 修改图标和名称

## 二、实训目的及要求

1. 可以查询全国所有省、市、县的数据（列表）

2. 可以查询全国任意城市的天气

3. 可以切换城市查询天气

4. 可以手动更新以及后台自动更新天气


## 三、数据地址

> 城市信息 <http://guolin.tech/api/china>

> 天气信息 <https://free-api.heweather.net/s6/weather?location=城市ID&key=和风天气免费key>

> 必应每日一图 <http://guolin.tech/api/bing_pic>

## 四、思维导图

![R2kH.png](https://img.auxiz.com/R2kH.png)

![R9sJ.png](https://img.auxiz.com/R9sJ.png)

## 五、实训步骤及操作方法

#### 1.创建数据库和表

> 在app/build.gradle中，将项目所需的各种依赖库进行声明

![RUtQ.png](https://img.auxiz.com/RUtQ.png)

> 在com.coolweather.android包下的四个包。其中db包用于存放数据库模型相关代码。gson包用于存放GSON模型相关的代码，service包用于存放服务相关代码，util包用于存放工具相关的代码

![RVra.png](https://img.auxiz.com/RVra.png)

> 在Db包下Province、City、County这三个类。分别用于存放省、市、县的数据信息，LitePal要求所有的实体类都要继承自DataSupport这个类；实体类内容就是声明一些用到的字段，并生成相应的getter和setter方法

> 在assets目录下的litepal.xml文件，将Province、City和County这3个实体类添加到映射列表当中

![R86j.png](https://img.auxiz.com/R86j.png)

#### 2.遍历全国省市县数据

> 全国省市县的数据都是从服务器端获取的，util包下的HttpUtil类，调用sendOkHttpRequest()方法，传入请求地址，并注册一个回调来处理服务器响应

![RIQF.png](https://img.auxiz.com/RIQF.png)

> 由于服务器返回的省市县的数据都是JSON格式，util包下的Utility类分别提供handleProvinceResponse()、handleCityResponse()、handleCountyResponse()这三个方法，用于解析和处理从服务器返回的各级数据。

![RKjC.png](https://img.auxiz.com/RKjC.png)

> 在com.coolweather.android包下的ChooseAreaFragment类继承自Fragment,其中onCreateView()方法和onActivityCreated()方法进行初始化操作，queryProvinces()方法、queryCities()方法和queryCounties()方法分别提供省、市、县数据的查询功能。queryFromServer()方法根据传入的参数从服务器上读取省市县的数据

![RJOl.png](https://img.auxiz.com/RJOl.png)

#### 3.显示真实天气信息

> 在gson包下的Basic类、Forecast类、Now类、Update类中，由于JSON中的一些字段不适合直接用来使用，所以可以使用\@SerializedName()的方式，将JSON字段写在里面，在Weather类中，对Basic类、Forecast类、Now类、Update类进行了引用，由于daily_forecast包含的是一个数组，因此使用List集合来引用Forecast类

![RrDh.png](https://img.auxiz.com/RrDh.png)

> 在Utility类中的handleWeatherResponse()方法用于解析天气JSON数据，WeatherActivity类中的requestWeather()、showWeatherInfo()方法在活动中请求天气数据，以及将数据展示到界面上。

![RzuM.png](https://img.auxiz.com/RzuM.png)

#### 4.获取必应每日一图

>   访问每日一图接口：http://guolin.tech/api/bing_pic，服务器就会返回今日的必应背景图链接，然后再使用Glide去加载这张图片。

![RYkP.png](https://img.auxiz.com/RYkP.png)

#### 5.手动更新天气和切换城市

![Rp77.png](https://img.auxiz.com/Rp77.png)

![RWtp.png](https://img.auxiz.com/RWtp.png)

![Rdzt.png](https://img.auxiz.com/Rdzt.png)

#### 6.后台自动更新天气

![RMHX.png](https://img.auxiz.com/RMHX.png)

![RnQr.png](https://img.auxiz.com/RnQr.png)

#### 7.修改图标和名称

>   准备一张图片来作为软件图标，将这张图片命名为logo.png，放入mipmap开头的目录下,然后修改AndroidManifest.xml中的代码

![Rw8f.png](https://img.auxiz.com/Rw8f.png)

>   打开res/values/sring.xml文件来修改程序名称

![R3S5.png](https://img.auxiz.com/R3S5.png)

![RS21.png](https://img.auxiz.com/RS21.png)

## 六、效果演示

![RODI.gif](https://img.auxiz.com/RODI.gif)