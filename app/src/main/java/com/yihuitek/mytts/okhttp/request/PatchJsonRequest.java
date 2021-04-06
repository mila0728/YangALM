package com.yihuitek.mytts.okhttp.request;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by lenovo on 2017/9/11.
 */

public class PatchJsonRequest extends BaseRequest<PatchJsonRequest> {
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json;charset=utf-8");
    private String content;

    @Override
    protected void buildBody(Request.Builder requestbuilder) {
        if (content == null) {
            content = "";
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, content);
        super.request = requestbuilder.url(url).patch(body).build();

    }
    public PatchJsonRequest json(String json) {
        content = json;
        return this;
    }
}
