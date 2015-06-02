/**
 * @package_name : com.example.BeaconTest
 * @file_name : IntroActivity.java
 * @date : 2014. 11. 7. 
 * @time : 오전 12:25:40
 * @author : JongHun Lee
 * @Contect :
 */
package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.view.R;

/**
 * @author Hoom
 */
public class IntroActivity extends Activity {

    private Button mGoMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 자동 생성된 메소드 스텁
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        mGoMainActivity = (Button) findViewById(R.id.go_main_but);
        mGoMainActivity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 자동 생성된 메소드 스텁
                Intent intent = new Intent(getApplicationContext(), GalleryAcitiviy.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
