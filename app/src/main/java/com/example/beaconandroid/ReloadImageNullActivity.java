/**
 * @package_name : com.example.BeaconTest
 * @file_name : ReloadActivity.java
 * @date : 2014. 10. 29. 
 * @time : 오전 10:02:20
 * @author : JongHun Lee
 * @Contect :
 */
package com.example.beaconandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Hoom
 */
public class ReloadImageNullActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 자동 생성된 메소드 스텁
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload_image_null);

        TextView title = (TextView) findViewById(R.id.reloadImageNullTitle);
        TextView content = (TextView) findViewById(R.id.reloadImageNullContent);

        title.setText(getIntent().getStringExtra("title"));
        content.setText(getIntent().getStringExtra("content"));
    }
}
