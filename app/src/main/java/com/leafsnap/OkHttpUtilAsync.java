package com.leafsnap;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpUtilAsync {

    private final static String URL = "http://api.openweathermap.org/data/2.5/weather?q=malerkotla&appid=b340dc9bd5c7d02984b0e297b52b440f&units=imperial/posts/1"; //1 object
    private final static String TAG = OkHttpUtilAsync.class.getSimpleName();

    private OkHttpClient okHttpClient;
    private Request request;
    private String result = "";
    private ResponseBody responseBody;
    private MediaType mediaType;


    public OkHttpUtilAsync() {
        setUpClientAndRequest();
    }

    public Object fetchNetwork() {
        Log.d(TAG, "fetchNetwork()");
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure()");
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse()");
                processResponse(response);


            }
        });

        return null;
    }

    private void setUpClientAndRequest() {
        Log.w(TAG, "setUpClientAndRequest()");

        okHttpClient = new OkHttpClient();
        request = new Request.Builder().url(URL).build();
    }

    private void processResponse(Response response) {
        Log.i(TAG, "processResponse()");

        if (response != null) {
            Log.w(TAG, "Response not Null");

            if (response.isSuccessful()) {
                Log.wtf(TAG, "Response is Successful : " + response.isSuccessful());

                //                     responseBody = response.body(); has the RESULT
                Log.v(TAG, " result = " + response.body());


            } else {
                Log.wtf(TAG, "Response is not Successful : " + response.isSuccessful());
            }
            //close the Response
            response.close();

        } else {
            Log.w(TAG, "Response ==== Null");
        }
    }
}
