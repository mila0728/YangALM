package com.yihuitek.mytts.okhttp;

import android.util.Log;

import com.yihuitek.mytts.okhttp.callback.CallBack;
import com.yihuitek.mytts.util.GsonUtils;
import com.yihuitek.mytts.weather.WeatherBeen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OkUtils {
    public static void getWeather() {
        Ok.get().url("http://wthrcdn.etouch.cn/weather_mini?citykey=101020200")
                .build().call(new CallBack() {
            @Override
            public void fail(Exception e) {

            }

            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String jsonArray = jsonObject1.getString("forecast");
                    List<WeatherBeen> weatherBeens=GsonUtils.GsonToList(jsonArray, WeatherBeen.class);

                    Log.e("calm",weatherBeens.toString()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
