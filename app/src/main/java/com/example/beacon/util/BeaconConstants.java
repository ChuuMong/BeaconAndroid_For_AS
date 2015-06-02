package com.example.beacon.util;


public class BeaconConstants {

    /**
     * Perform scan seconds
     */
    public static final long BLE_SCAN_INTERVAL = 10000;
    /**
     * Stop scanning after 5 seconds
     */
    public static final long BLE_SCAN_STOP_AFTER_PERIOD = 1000;
    /**
     * Not supported message
     */
    public static final String BLE_NOT_SUPPORTED = "BLE is not supported on this device.";
    /**
     * Service not initialized message
     */
    public static final String BLE_NOT_INITIALIZED = "BLE Scan Service has not been initialized.";

}

