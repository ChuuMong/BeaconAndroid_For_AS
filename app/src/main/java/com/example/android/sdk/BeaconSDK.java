package com.example.android.sdk;


import android.content.Context;
import android.util.Log;

import com.example.android.sdk.domain.BeaconListener;
import com.example.android.sdk.service.ble.BeaconBLEScanService;


public class BeaconSDK {

    private final String TAG = BeaconSDK.class.getSimpleName();
    private BeaconListener mListener = null;
    private Context mContext = null;
    private BeaconBLEScanService mScanService = null;

    /**
     * Initializes the SDK
     *
     * @param listener Listener using the SDK
     */
    public BeaconSDK(BeaconListener listener) {
        setmContext(listener.getContext());
        mListener = listener;
        //instantiate the scanner
        mScanService = new BeaconBLEScanService(mListener.getContext());
        mScanService.initialize(listener);
    }

    /**
     * Begins BLE Beacon Discovery
     */
    public void startDiscovery() {
        if (mScanService == null) {
            Log.e(TAG, "SDK Not Initialized");
        }
        mScanService.startScan();
    }

    /**
     * Stops the discovery
     */
    public void stopDiscovery() {
        if (mScanService == null) {
            Log.e(TAG, "SDK Not Initialized");
        }
        mScanService.stopLeScan();
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
