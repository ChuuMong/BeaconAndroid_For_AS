package com.example.beaconandroid;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.Exception.CustomException;
import com.example.constant.HttpUrl;
import com.example.http.HttpQueue;
import com.example.http.HttpQueueListener;
import com.example.model.Beacon;
import com.example.response.ResponseBeacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DummyItem implements HttpQueueListener {

    private final Context context;
    private Beacon beacon;
    String beaconUUID;

    public DummyItem(Context context, String UUID) {
        this.context = context;
        this.beaconUUID = UUID;
    }

    /**
     * @package_name : com.example.BeaconTest
     * @file_name : DummyItem.java
     * @date : 2014. 10. 30.
     * @time : 오후 2:51:45
     * @author : JongHun Lee
     * @method_name : startBeaconDataFromServer
     * @return_type void
     * @Contect : 서버에 비콘 UUID를 전달하고 해당 데이터를 받아옴
     */
    public void startBeaconDataFromServer() {
        Map<String, String> params = new HashMap();
        params.put("uuid", beaconUUID.toLowerCase());
        HttpQueue httpQueue = new HttpQueue.Builder().context(context).setObj(this).listener(this).methodType(HttpQueue.POST).url(HttpUrl.GET_BEACON_DATA).setParameter
                (params)
                .returnCode(100).build();
        try {
            httpQueue.execute();

        }
        catch (CustomException e) {
            e.printStackTrace();
        }

    }

    public Beacon getBeacon() {
        return beacon;
    }

    @Override
    public void request_finished(int returnCode, String result) {
        if (returnCode == 100) {
            ResponseBeacon value = new Gson().fromJson(result, ResponseBeacon.class);
            if (value.code == 200) {
                beacon = value.beacon;
                Intent i = new Intent("com.example.beaconandroid.BeaconBroadCast");
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            }
        }
    }

    @Override
    public void request_failed(int returnCode, String result) {

    }
}
