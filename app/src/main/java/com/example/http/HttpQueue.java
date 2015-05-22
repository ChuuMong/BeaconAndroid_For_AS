package com.example.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.Exception.CustomException;
import com.example.constant.HttpUrl;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PutBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JongHunLee on 2015-04-27.
 */
public class HttpQueue implements Response.Listener<String>, Response.ErrorListener {

    private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;

    //Constants
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;

    //Request Parameters
    private final Context context;
    private final Object obj;
    private final HttpQueueListener listener;
    private final int returnCode;
    private final int methodType;
    private final String url;
    private final Map<String, String> paramsMap;
    private final Map<String, File> fileMap;

    private static RequestQueue requestQueue;

    private HttpQueue(Builder builder) {
        this.context = builder.context;
        this.obj = builder.obj;
        if (builder.returnCode == 0) {
            this.returnCode = 0;
        }
        else {
            this.returnCode = builder.returnCode;
        }

        if (builder.methodType == 0) {
            this.methodType = 0;
        }
        else {
            this.methodType = builder.methodType;
        }
        this.url = builder.url;
        this.paramsMap = builder.paramsMap;
        this.fileMap = builder.fileMap;
        this.listener = builder.listener;

        if (requestQueue == null) {
            initVolley();
            initVolleyer();
        }
    }

    private void initVolley() {
        requestQueue = DefaultRequestQueueFactory.create(context);
        requestQueue.start();
    }

    private void initVolleyer() {
        volleyer(requestQueue).settings().setAsDefault().done();
    }

    public static class Builder {

        private Context context;
        private int returnCode;
        private int methodType;
        private String url;
        private Map<String, String> paramsMap;
        private Map<String, File> fileMap;
        private HttpQueueListener listener;
        private Object obj;

        public Builder() {
            paramsMap = new HashMap<>();
            fileMap = new HashMap<>();
            returnCode = 0;
            methodType = 0;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder setObj(Object obj) {
            this.obj = obj;
            return this;
        }

        public Builder returnCode(int returnCode) {
            this.returnCode = returnCode;
            return this;
        }

        public Builder methodType(int value) {
            methodType = value;
            return this;
        }

        public Builder url(String value) {
            url = value;
            return this;
        }

        public Builder addParameter(String key, String value) {
            paramsMap.put(key, value);
            return this;
        }

        public Builder setParameter(Map<String, String> value) {
            paramsMap.clear();
            paramsMap.putAll(value);
            return this;
        }

        public Builder addFile(String key, File value) {
            fileMap.put(key, value);
            return this;
        }

        public Builder listener(HttpQueueListener listener) {
            this.listener = listener;
            return this;
        }

        public HttpQueue build() {
            return new HttpQueue(this);
        }
    }

    public HttpQueue execute() throws CustomException {
        dataValidationCheck();
        if (methodType == GET) {
            get();
        }
        else if (methodType == POST) {
            post();
        }
        else if (methodType == PUT) {
            put();
        }
        else if (methodType == DELETE) {
            delete();
        }

        return this;
    }

    private void dataValidationCheck() throws CustomException {
        if (context == null) {
            new CustomException("Context is null");
        }
        else if (obj == null) {
            new CustomException("Object is null");
        }
        else if (listener == null) {
            new CustomException("Listener is null");
        }
        else if (returnCode == 0) {
            new CustomException("Return Code is null");
        }
        else if (methodType == 0) {
            new CustomException("Method Type is null");
        }
    }

    private static final String STR_ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";

    private void get() {
        volleyer().get(url).addHeader(STR_ACCEPT, APPLICATION_JSON).withListener(this).withErrorListener(this).execute();
    }

    private void post() {
        PostBuilder postBuilder = volleyer().post(url);

        if (!paramsMap.isEmpty()) {
            for (String key : paramsMap.keySet()) {
                postBuilder.addStringPart(key, paramsMap.get(key));
            }
        }

        if (!fileMap.isEmpty()) {
            for (String key : fileMap.keySet()) {
                postBuilder.addFilePart(key, fileMap.get(key));
            }
        }

        postBuilder.withListener(this).withErrorListener(this).execute();
    }

    private void put() {
        PutBuilder putBuilder = volleyer().put(url);

        if (!paramsMap.isEmpty()) {
            for (String key : paramsMap.keySet()) {
                putBuilder.addStringPart(key, paramsMap.get(key));
            }
        }

        if (!fileMap.isEmpty()) {
            for (String key : fileMap.keySet()) {
                putBuilder.addFilePart(key, fileMap.get(key));
            }
        }

        putBuilder.withListener(this).withErrorListener(this).execute();
    }

    private void delete() {
        volleyer().delete(url).addHeader(STR_ACCEPT, APPLICATION_JSON).withListener(this).withErrorListener(this).execute();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (obj != null) {
            Log.i("HttpQueue", "API " + String.valueOf(returnCode) + "번 Error " + url.substring(HttpUrl.DEFAULT_URL.length(), url.length()) + "(" +
                               obj.getClass().getSimpleName() + ")" + " >> " + error.getMessage());
        }
        else {
            Log.i("HttpQueue", "API " + String.valueOf(returnCode) + "번 Error " + url.substring(HttpUrl.DEFAULT_URL.length(), url.length()) + " >> " +
                               error.getMessage());
        }
        if (listener != null) {
            listener.request_failed(this.returnCode, error.getMessage());
        }
    }

    @Override
    public void onResponse(String response) {
        if (obj != null) {
            Log.i("HttpQueue", "API " + String.valueOf(returnCode) + "번 " + url.substring(HttpUrl.DEFAULT_URL.length(), url.length()) + "(" +
                               obj.getClass().getSimpleName() + ") >> " + response);
        }
        else {
            Log.i("HttpQueue", "API " + String.valueOf(returnCode) + "번 " + url.substring(HttpUrl.DEFAULT_URL.length(), url.length()) + " >> " + response);
        }

        if (listener != null) {
            listener.request_finished(this.returnCode, response);
        }
    }
}