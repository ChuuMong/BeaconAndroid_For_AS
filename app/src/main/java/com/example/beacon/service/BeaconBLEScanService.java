package com.example.beacon.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.beacon.domain.Beacon;
import com.example.beacon.domain.BeaconListener;
import com.example.beacon.util.BeaconConstants;

import java.util.HashMap;
import java.util.Map;

public class BeaconBLEScanService {

    private static final String TAG = BeaconBLEScanService.class.getSimpleName();

    private final Context context;
    private final BeaconListener mBeaconListener;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BLEScanThread mBLEScanThread;
    private int mCountDownCounterSeconds;

    private boolean mIsInitialized = false;

    private Map<String, Beacon> mBeaconContainer = null;


    /**
     * New instance of CQSearchService
     */
    public BeaconBLEScanService(Context context, BeaconListener beaconListener) {
        this.context = context;
        this.mBeaconListener = beaconListener;
        mBeaconContainer = new HashMap<>();
        init();
    }

    /**
     * Initializes the service
     */
    public void init() {
        mIsInitialized = true;

        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    /**
     * Starts the scan
     */
    public void startScan() {
        if (!mIsInitialized) {
            Log.i(TAG, "BLE Scan Service hasn't been initialized..make sure to call initialize prior to this call.");
            Toast.makeText(context, BeaconConstants.BLE_NOT_INITIALIZED, Toast.LENGTH_SHORT).show();
            return;
        }

        mCountDownCounterSeconds = (int) BeaconConstants.BLE_SCAN_INTERVAL / 1000;
        if (mBLEScanThread == null) {
            mBLEScanThread = new BLEScanThread();
        }

        try {
            mBLEScanThread.start();
        }
        catch (Exception ex) {
            Log.i(TAG, String.format("Exception Starting BLE Scan Thread: %s", ex));
        }
    }

    public void stopScan() {
        // Scan Stop
        mCountDownCounterSeconds = -1;
        stopLeScan();
    }

    public void stopLeScan() {
        if (!mIsInitialized) {
            Toast.makeText(context, BeaconConstants.BLE_NOT_INITIALIZED, Toast.LENGTH_SHORT).show();
            return;
        }

        // stop the scan
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    public void startLeScan() {
        if (!mIsInitialized) {
            Toast.makeText(context, BeaconConstants.BLE_NOT_INITIALIZED, Toast.LENGTH_SHORT).show();
            return;
        }

        // stop the scan
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    /**
     * Adds Generic BLE beacon to the container
     */
    private void addBLEBeacon(BluetoothDevice device, String identifier, int rssi, byte[] scanRecord) {
        if (device == null) {
            Log.i(TAG, "BLE Device cannot be null.");
            return;
        }

        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            // 비콘 식별자 && 올바른 데이터 길이 검사
            // 0x02 is a secondary ID that denotes a proximity beacon, which is used by all iBeacons.
            // 0x15 defines the remaining length to be 21 bytes (16+2+2+1).
            if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {
                patternFound = true;
                break;
            }
            startByte++;
        }

        if (patternFound) {
            //Convert to hex String
            byte[] uuidBytes = new byte[16];

            // scanRecord에서 uuid 만큼 Copy
            System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
            String hexString = bytesToHex(uuidBytes);

            //Here is your UUID
            String uuid = hexString.substring(0, 8) + "-" +
                          hexString.substring(8, 12) + "-" +
                          hexString.substring(12, 16) + "-" +
                          hexString.substring(16, 20) + "-" +
                          hexString.substring(20, 32);

            //Here is your Major value
            int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

            //Here is your Minor value
            int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

            Beacon beacon = new Beacon();
            beacon.setAddress(device.getAddress());
            beacon.setRssi(rssi);
            beacon.setUuids(uuid);
            beacon.setMajor(major);
            beacon.setMinor(minor);

            if (!mBeaconContainer.containsKey(identifier)) {
                Log.i(TAG, "Scan Success - " + identifier);
                mBeaconContainer.put(identifier, beacon);
                mBeaconListener.handleGenericBeaconDiscovery(beacon);
            }
        }
    }

    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // thread to scan for devices
    private class BLEScanThread extends Thread {

        Handler mHandler = new Handler(Looper.getMainLooper());

        public void run() {
            // run once
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopLeScan();
                }
            }, BeaconConstants.BLE_SCAN_STOP_AFTER_PERIOD);

            startLeScan();

            // then run every X many seconds
            while (mCountDownCounterSeconds > 0) {
                mCountDownCounterSeconds--;
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                }

                if (mCountDownCounterSeconds == 0) {
                    Log.i(TAG, "Performing BLE scan");
                    mCountDownCounterSeconds = (int) BeaconConstants.BLE_SCAN_INTERVAL / 1000;

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopLeScan();
                        }
                    }, BeaconConstants.BLE_SCAN_STOP_AFTER_PERIOD);
                    startLeScan();
                }
            }
        }
    }

    /**
     * Device callback
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            addBLEBeacon(device, device.getAddress().toLowerCase().replaceAll(":", ""), rssi, scanRecord);
        }
    };
}