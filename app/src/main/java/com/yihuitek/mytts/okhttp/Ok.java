package com.yihuitek.mytts.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;



import com.yihuitek.mytts.okhttp.headerparams.Param;
import com.yihuitek.mytts.okhttp.request.FileRequest;
import com.yihuitek.mytts.okhttp.request.GetRequest;
import com.yihuitek.mytts.okhttp.request.PatchJsonRequest;
import com.yihuitek.mytts.okhttp.request.PostFileRequest;
import com.yihuitek.mytts.okhttp.request.PostJsonRequest;
import com.yihuitek.mytts.okhttp.request.PostRequest;
import com.yihuitek.mytts.okhttp.request.PutJsonRequest;
import com.yihuitek.mytts.okhttp.utils.HeadersUtils;
import com.yihuitek.mytts.okhttp.utils.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by jlccl on 2017/2/17.
 */

public class Ok {
    private static OkHttpClient.Builder builder;
    private static OkHttpClient okHttpClient;
    private static Map<String, String> headers;
    private static List<Param> params;
    private static Handler mHandler;
    private static Context mContext;

    private Ok(Builder builder) {
        this.builder = builder.builder;
    }

    public static Builder init(Context context) {
        mHandler = new Handler(Looper.getMainLooper());
        mContext = context;
        return new Builder();
    }

    public static Handler getmHandler() {
        return mHandler;
    }

    public static OkHttpClient getInstance() {
        if (okHttpClient == null)
            return builder.build();
        else return okHttpClient;
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * ok构建
     */
    public static class Builder {

        private OkHttpClient.Builder builder;

        public Builder() {
            builder = new OkHttpClient.Builder();
            builder.connectTimeout(10000L, TimeUnit.MILLISECONDS);
            builder.readTimeout(10000L, TimeUnit.MILLISECONDS);
/**          HTTPS 请求
            try {
                X509TrustManager trustManager = null;
                SSLSocketFactory sslSocketFactory = null;
                trustManager = YHUtils.trustManagerForCertificates(mContext);//以流的方式读入证书
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory, trustManager);
                builder .hostnameVerifier((s, sslSession) -> true);
            } catch (Exception e) {
                LogUtil.d("Builder  " + e.getMessage());
                e.printStackTrace();
            }
            */
            if (headers == null) headers = new LinkedHashMap<>();
            headers.put("Accept-Language", HeadersUtils.getAcceptLanguage());
            headers.put("User-Agent", HeadersUtils.getUserAgent());
        }

        public Builder connectTimeout(long timeout, TimeUnit unit) {
            builder.connectTimeout(timeout, unit);
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit unit) {
            builder.readTimeout(timeout, unit);
            return this;
        }

        public Builder NetWorkInterceptor(String tag, Interceptor interceptor) {
            Log.TAG = tag;
            builder.addNetworkInterceptor(interceptor);
            return this;
        }

        public Builder AppInterceptor(String tag, Interceptor interceptor) {
            Log.TAG = tag;
            builder.addInterceptor(interceptor);
            return this;
        }

        public Builder CookieJar( CookieJar cookieJar) {
            builder.cookieJar(cookieJar);
            return this;
        }

        public Builder commonHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder commonParams(String key, String value) {
            if (params == null) params = new ArrayList<>();
            params.add(new Param(key, value));
            return this;
        }

        public void build() {
            new Ok(this);
        }
    }

    /**
     * 获得公共请求头
     */
    public static Map<String, String> getCommonHeaders() {
        return headers;
    }

    /**
     * 获得公共请求参数
     */
    public static List<Param> getCommonParams() {
        return params;
    }


    /**
     * get
     */
    public static GetRequest get() {
        return new GetRequest();
    }

    /**
     * post
     */
    public static PostRequest post() {
        return new PostRequest();
    }

    /**
     * postjson
     */
    public static PostJsonRequest postJson() {
        return new PostJsonRequest();
    }

    /**
     * putjson
     */
    public static PutJsonRequest putJson(){
        return new PutJsonRequest();
    }

    /**
     * putjson
     */
    public static PatchJsonRequest patchJson(){
        return new PatchJsonRequest();
    }

    /**
     * postfile
     */
    public static PostFileRequest postFile() {
        return new PostFileRequest();
    }

    /**
     * download
     */
    public static FileRequest download() {
        return new FileRequest();
    }

    /**
     * 根據tag來取消請求 如果請求已經完成不能取消
     * cancleTag
     */
    public static void cancle(Object object) {
        if (object == null) {
            return;
        }
        //隊列中的call
        for (Call call : getInstance().dispatcher().queuedCalls()) {
            if (object.equals(call.request().tag())) {
                call.cancel();
            }
        }
        //運行中的call
        for (Call call : getInstance().dispatcher().runningCalls()) {
            if (object.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有請求
     */
    public static void cancleAll() {
        getInstance().dispatcher().cancelAll();
    }
}
