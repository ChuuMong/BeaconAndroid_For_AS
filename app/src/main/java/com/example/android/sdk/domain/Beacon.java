package com.example.android.sdk.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Beacon {

    private final String TAG = Beacon.class.getSimpleName();
    private String trueName, name, address, identifier, btClass, manufacturer, major, minor, uuids;
    private Date dateDiscovered;
    private int rssi;

    public Beacon() {

    }

    public void setRSSI(int i) {
        rssi = i;
    }

    public void setIdentifier(String s) {
        identifier = s;
    }

    public void setAddress(String s) {
        address = s;
    }

    public void setManufacturer(String s) {
        manufacturer = s;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setName(String s) {
        name = s;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String s) {
        trueName = s;
    }

    public String getBTClass() {
        return btClass;
    }

    public void setBTClass(String s) {
        btClass = s;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getAddres() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getRSSI() {
        return rssi;
    }

    public String getTimeDiscovered() {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return dateFormat.format(dateDiscovered);
    }

    public void setTimeDiscovered(Date date) {
        dateDiscovered = date;
    }

    public String getTAG() {
        return TAG;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getUUID() {
        return uuids;
    }

    public void setUUID(String uuids) {
        this.uuids = uuids;
    }
}
