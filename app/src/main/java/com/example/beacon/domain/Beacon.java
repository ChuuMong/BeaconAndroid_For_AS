package com.example.beacon.domain;

import java.util.Date;

public class Beacon {

    private final String TAG = Beacon.class.getSimpleName();
    private String trueName, name, address, identifier, btClass, manufacturer, uuids;
    private Date dateDiscovered;
    private int major, minor, rssi;

    public String getTAG() {
        return TAG;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBtClass() {
        return btClass;
    }

    public void setBtClass(String btClass) {
        this.btClass = btClass;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getUuids() {
        return uuids;
    }

    public void setUuids(String uuids) {
        this.uuids = uuids;
    }

    public Date getDateDiscovered() {
        return dateDiscovered;
    }

    public void setDateDiscovered(Date dateDiscovered) {
        this.dateDiscovered = dateDiscovered;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
