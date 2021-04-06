package com.yihuitek.mytts.okhttp.timeoutInterCeptor;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TimeoutInterCeptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
//        String questUrl = request.url().toString();
//        if (YHUtils.isNotEmpty(Config.BACKGRPUND_MUSIC_RESLAN)) {
//            boolean isUploadVideoApi = questUrl.contains(Config.BACKGRPUND_MUSIC_RESLAN);
//            if (isUploadVideoApi) {
//                return chain.withConnectTimeout(3, TimeUnit.SECONDS)
//                        .withReadTimeout(3, TimeUnit.SECONDS)
//                        .withWriteTimeout(3, TimeUnit.SECONDS)
//                        .proceed(request);
//            }
//        }
        return chain.proceed(request);

    }
}
