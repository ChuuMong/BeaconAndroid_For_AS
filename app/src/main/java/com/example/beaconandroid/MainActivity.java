package com.example.beaconandroid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sdk.BeaconSDK;
import com.example.android.sdk.domain.Beacon;
import com.example.android.sdk.domain.BeaconListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

    private BeaconSDK mBeaconSDK;
    private BluetoothAdapter mBluetoothAdapter;

    private ListView mListView;
    private TextView mBeaconNoSearchText;
    public BeaconListView mAdapter;
    public ArrayList<String> beaconList;

    public List<com.example.model.Beacon> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Art Hall");

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mBeaconNoSearchText = (TextView) findViewById(R.id.beacon_no_searchText);
        mBeaconNoSearchText.setText("비콘이 탐지 되지 않았습니다.\n\n 전시물 앞으로 가주세요.");

        mListView = (ListView) findViewById(R.id.listView1);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "비콘을 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        beaconList = new ArrayList<String>();
        listData = new ArrayList<com.example.model.Beacon>();

        mAdapter = new BeaconListView(this, listData);
        mListView.setAdapter(mAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("com.example.beaconandroid.BeaconBroadCast"));
    }

    @Override
    protected void onResume() {
        // TODO 자동 생성된 메소드 스텁
        super.onResume();
        if (mBluetoothAdapter.isEnabled()) {
            mBeaconSDK = new BeaconSDK(mSDKListener);
            mBeaconSDK.startDiscovery();
        }
    }

    private BeaconListener mSDKListener = new BeaconListener() {

        @Override
        public void handleGenericBeaconDiscovery(Beacon beacon) {
            if (beacon == null) {
                return;
            }

            if (beacon.getRSSI() > -70) {
                for (int i = 0; i < beaconList.size(); i++) {
                    if (beaconList.get(i).equals(beacon.getUUID())) {
                        return;
                    }
                }
                beaconList.add(beacon.getUUID().trim());
                Log.i("MainActivity", beacon.getUUID().trim());
                setListViewData(beacon);
            }
        }

        @Override
        public Context getContext() {
            return getApplicationContext();
        }
    };

    DummyItem di;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.example.beaconandroid.BeaconBroadCast")) {
                if (di != null) {
                    final com.example.model.Beacon beacon = di.getBeacon();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO 자동 생성된 메소드 스텁
                            mBeaconNoSearchText.setText("");
                            listData.add(beacon);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        // TODO 자동 생성된 catch 블록
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public void setListViewData(Beacon beacon) {
        di = new DummyItem(this, beacon.getUUID());
        di.startBeaconDataFromServer();
    }
}
