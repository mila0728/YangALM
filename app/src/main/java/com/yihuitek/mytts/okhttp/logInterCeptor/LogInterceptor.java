package com.yihuitek.mytts.okhttp.logInterCeptor;


import android.text.TextUtils;
import android.util.Log;

import com.yihuitek.mytts.okhttp.utils.JsonFormat;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 日志打印类了request response body
 * Created by jlccl on 2017/2/17.
 */

public class LogInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long requestTime = System.nanoTime();
        logForRequest(request);
        Response response = chain.proceed(request);
        long responseTime = System.nanoTime();
        long time = TimeUnit.NANOSECONDS.toMillis(responseTime - requestTime);
        MediaType mediaType = response.body().contentType();
        String content= response.body().string();
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
//        return logForResponse(response, time);
    }


    private void logForRequest(Request request) {
        try {

            Log.d("calm","============request start===============");
            Log.d("calm","url:" + request.url());
            Log.d("calm","method:" + request.method());
            Log.d("calm","timuout:" +request.toString());
            if (request.headers() != null && request.headers().size() > 0){
                Headers headers = request.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("calm","headers"+headers.name(i)+"---"+headers.value(i));
                }
            }
            Log.d("calm","============request end=================");
        } catch (Exception e) {
            Log.d("calm","log request has something worng!!");
        }
    }

    private Response logForResponse(Response response, long time) {
        try {
            Log.d("calm","============response start==============");
            //response.body().string()只能调用一次 body()就会关掉
            //每次使用前都clone一份使用保证原来的body没有被关掉
            Response copy = response.newBuilder().build();
            Log.d("calm","total time:" + time);
            if (!TextUtils.isEmpty(copy.message()))
                Log.d("calm","message:" + copy.message());
            if (copy.headers() != null && copy.headers().size() > 0) {
                Headers headers = copy.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("calm","\t" + headers.name(i) + ": " + headers.value(i));
                }
            }
            ResponseBody body = copy.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    Log.d("calm","============response body===============");
                    if (isText(mediaType)) {
                        String content = body.string();
                        Log.d("calm",JsonFormat.formatJson(content));
                        Log.d("calm","============response body===============");
                        return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
                    } else {
                        Log.d("calm"," maybe response content too large too print , ignored!");
                    }
                }
            } else {
                Log.d("calm"," body is null , ignored!");
            }
        } catch (Exception e) {
            Log.d("calm","log response has something worng!!");
        }
        return response;
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }
}
