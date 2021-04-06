package com.yihuitek.mytts;

import android.app.Application;
import android.content.Context;

import com.yihuitek.mytts.okhttp.Ok;
import com.yihuitek.mytts.okhttp.logInterCeptor.LogInterceptor;
import com.yihuitek.mytts.okhttp.persistentcookiejar.PersistentCookieJar;
import com.yihuitek.mytts.okhttp.persistentcookiejar.cache.SetCookieCache;
import com.yihuitek.mytts.okhttp.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yihuitek.mytts.okhttp.timeoutInterCeptor.TimeoutInterCeptor;

import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Ok.init(this)
                .connectTimeout(6, TimeUnit.SECONDS)
                .readTimeout(6, TimeUnit.SECONDS)
                .AppInterceptor("timeout",new TimeoutInterCeptor())
                .AppInterceptor("eason", new LogInterceptor())
                .CookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.this)))
                .build();
    }
}
