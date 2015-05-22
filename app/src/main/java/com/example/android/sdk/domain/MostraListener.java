package com.example.android.sdk.domain;

import android.content.Context;

/**
 * Mostra Android SDK 2.0.1 Beacon listener definition. A client app would implement these calls and handle beacon discovery & data download
 *
 * @Copyright Mostra, LLC 2014
 */
public interface MostraListener {

    /**
     * Called when a non-CQ BLE beacon has been discovered
     *
     * @param beacon MostraBeacon
     */
    void handleGenericBeaconDiscovery(MostraBeacon beacon);

    /**
     * Current activity application context
     *
     * @return Context
     */
    Context getContext();
}
