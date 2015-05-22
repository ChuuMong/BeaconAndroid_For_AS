/**
 * @package_name : com.example.BeaconTest
 * @file_name : LodingActivity.java
 * @date : 2014. 11. 6. 
 * @time : 오후 8:32:10
 * @author : JongHun Lee
 * @Contect :
 */
package com.example.beaconandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class LodingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 자동 생성된 메소드 스텁
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loding_activity);

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, (long) ((Math.random() * (500 + 1)) + 1000));
    }
}
