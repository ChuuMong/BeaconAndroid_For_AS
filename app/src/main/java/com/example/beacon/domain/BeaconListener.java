package com.example.beacon.domain;

public interface BeaconListener {

    void handleGenericBeaconDiscovery(Beacon beacon);
}
