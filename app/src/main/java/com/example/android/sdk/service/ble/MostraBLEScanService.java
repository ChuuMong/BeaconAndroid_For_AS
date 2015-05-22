package com.example.android.sdk.service.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.android.sdk.domain.MostraBeacon;
import com.example.android.sdk.domain.MostraListener;
import com.example.android.sdk.util.MostraConstants;

/**
 * Mostra Android SDK 2.0.1 CQ Beacon Discovery Class. Works with Android BLE library to scan and retrieve BLE devices.
 *
 * @Copyright Mostra, LLC 2014
 */
public class MostraBLEScanService {

    private static final String TAG = MostraBLEScanService.class.getSimpleName();
    private long mScanInterval = MostraConstants.BLE_SCAN_INTERVAL;
    private long mScanStopPeriod = MostraConstants.BLE_SCAN_STOP_AFTER_PERIOD;

    private Context mContext = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BLEScanThread mBLEScanThread;
    private int mCountDownCounterSeconds;

    private boolean mIsInitialized = false;

    private Map<String, MostraBeacon> mBeaconContainer = null;
    private MostraListener mMostraListener = null;

    /**
     * New instance of CQSearchService
     */
    public MostraBLEScanService(Context c) {
        Log.d(TAG, String.format("CQSearchService: new instance"));

        mContext = c;

        mBeaconContainer = new HashMap<String, MostraBeacon>();
    }

    public Map<String, MostraBeacon> getBeacons() {
        Log.d(TAG, String.format("getBeacons: Total=%d", mBeaconContainer.size()));
        return mBeaconContainer;
    }

    /**
     * Initializes the service
     */
    public void initialize(MostraListener a) {
        mMostraListener = a;
        mIsInitialized = true;

        final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

    }

    /**
     * Starts the scan
     */
    public void startScan() {
        if (!mIsInitialized) {
            Log.e(TAG, "BLE Scan Service hasn't been initialized..make sure to call CQBLEScanService.initialize prior to this call.");
            Toast.makeText(mContext, MostraConstants.BLE_NOT_INITIALIZED, Toast.LENGTH_SHORT).show();
            return;
        }

        mBeaconContainer = new HashMap<String, MostraBeacon>();
        mCountDownCounterSeconds = (int) mScanInterval / 1000;
        if (mBLEScanThread == null) {
            mBLEScanThread = new BLEScanThread();
        }

        try {
            mBLEScanThread.start();
        }
        catch (Exception ex) {
            Log.e(TAG, String.format("Exception Starting BLE Scan Thread: %s", ex));
        }
    }

    /*
     * Stops the LE scan
     */
    public void stopLeScan() {
        if (!mIsInitialized) {
            Log.e(TAG, "BLE Scan Service hasn't been initialized..make sure to call CQBLEScanService.initialize prior to this call.");
            Toast.makeText(mContext, MostraConstants.BLE_NOT_INITIALIZED, Toast.LENGTH_SHORT).show();
            return;
        }

        // stop the scan
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    /*
     * Starts the LE scan
     */
    public void startLeScan() {
        if (!mIsInitialized) {
            Log.e(TAG, "BLE Scan Service hasn't been initialized..make sure to call CQBLEScanService.initialize prior to this call.");
            Toast.makeText(mContext, MostraConstants.BLE_NOT_INITIALIZED, Toast.LENGTH_SHORT).show();
            return;
        }

        // init the hashmap
        mBeaconContainer = new HashMap<String, MostraBeacon>();

        // stop the scan
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    /**
     * Adds Generic BLE beacon to the container
     */
    private void addBLEBeacon(BluetoothDevice device, String identifier, int rssi, byte[] scanRecord) {
        if (device == null) {
            Log.e(TAG, "BLE Device cannot be null.");
            return;
        }
        ArrayList<String> scanData = ScanRecodeParsing(scanRecord);
        MostraBeacon beacon = new MostraBeacon();

        //beacon.setAddress(device.getAddress());
        //beacon.setName(device.getName());
        beacon.setRSSI(rssi);
        //beacon.setIdentifier(identifier);
        //beacon.setIsCQBLE(false);
        //beacon.setBTClass(device.getBluetoothClass().toString());
        //beacon.setTimeDiscovered(new Date());

        if (scanData != null) {
            beacon.setUUID(scanData.get(0));
            //beacon.setMajor(scanData.get(1));
            //beacon.setMinor(scanData.get(2));
        }

        if (!mBeaconContainer.containsKey(identifier)) {
            Log.d(TAG, String.format("Found BLE Device=%s @ %d", identifier, rssi));
            mBeaconContainer.put(identifier, beacon);
            // call the listener and pass Generic Beacon info
            mMostraListener.handleGenericBeaconDiscovery(beacon);
        }
    }

    /**
     * @package_name : com.mostra.android.sdk.service.ble
     * @file_name : MostraBLEScanService.java
     * @date : 2014. 10. 23.
     * @time : 오후 6:43:58
     * @author : JongHun Lee
     * @method_name : ScanRecodeParsing
     * @return_type ArrayList<String>
     * @Contect : Bluetooth 데이터 파싱
     */
    private ArrayList<String> ScanRecodeParsing(byte[] scanRecord) {
        ArrayList<String> data = new ArrayList<String>();
        if (scanRecord.length > 30) {
            if ((scanRecord[5] == (byte) 0x4c) && (scanRecord[6] == (byte) 0x00) && (scanRecord[7] == (byte) 0x02) && (scanRecord[8] == (byte) 0x15)) {
                data.add(IntToHex2(scanRecord[9] & 0xff) + IntToHex2(scanRecord[10] & 0xff) + IntToHex2(scanRecord[11] & 0xff) + IntToHex2(scanRecord[12] & 0xff) + "-" +
                         IntToHex2(scanRecord[13] & 0xff) + IntToHex2(scanRecord[14] & 0xff) + "-" + IntToHex2(scanRecord[15] & 0xff) + IntToHex2(scanRecord[16] & 0xff) +
                         "-" + IntToHex2(scanRecord[17] & 0xff) + IntToHex2(scanRecord[18] & 0xff) + "-" + IntToHex2(scanRecord[19] & 0xff) + IntToHex2(
                        scanRecord[20] & 0xff) + IntToHex2(scanRecord[21] & 0xff) + IntToHex2(scanRecord[22] & 0xff) + IntToHex2(scanRecord[23] & 0xff) + IntToHex2(
                        scanRecord[24] & 0xff)); // UUID

                data.add(IntToHex2(scanRecord[25] & 0xff) + IntToHex2(scanRecord[26] & 0xff)); // major
                data.add(IntToHex2(scanRecord[27] & 0xff) + IntToHex2(scanRecord[28] & 0xff)); // minor
            }
        }

        return data;
    }

    /**
     * @package_name : com.mostra.android.sdk.service.ble
     * @file_name : MostraBLEScanService.java
     * @date : 2014. 10. 23.
     * @time : 오후 6:44:42
     * @author : JongHun Lee
     * @method_name : IntToHex2
     * @return_type String
     * @Contect : UUID를 위해 int -> Hex 변환 함수
     */
    private String IntToHex2(int Value) {
        char HEX2[] = {Character.forDigit((Value >> 4) & 0x0F, 16), Character.forDigit(Value & 0x0F, 16)};
        String Hex2Str = new String(HEX2);
        return Hex2Str.toUpperCase();
    }

    // thread to scan for devices
    private class BLEScanThread extends Thread {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(final Message msgs) {

            }
        };

        public void run() {
            Log.d(TAG, "BEGIN BLE Search Thread" + this);

            // run once
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopLeScan();
                        }
                    }, mScanStopPeriod);
                    startLeScan();
                }
            }).start();

            // then run every X many seconds
            while (mCountDownCounterSeconds > 0) {
                mCountDownCounterSeconds--;
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {

                }

                if (mCountDownCounterSeconds == 0) {
                    Log.d(TAG, "Performing BLE scan");
                    mCountDownCounterSeconds = (int) mScanInterval / 1000;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stopLeScan();
                                }
                            }, mScanStopPeriod);
                            startLeScan();
                        }
                    }).start();
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
            Log.i(TAG, String.format("New LE Device: %s (%s) @ %d", device.getName(), device.getAddress(), rssi));

            addBLEBeacon(device, device.getAddress().toLowerCase().replaceAll(":", ""), rssi, scanRecord);
        }
    };
}
