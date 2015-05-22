package com.example.android.sdk.domain;

import android.content.Context;

public interface BeaconListener {

    void handleGenericBeaconDiscovery(Beacon beacon);
    Context getContext();
}
