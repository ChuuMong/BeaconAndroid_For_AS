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
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoom
 */
public class ReloadActivity extends Activity {

    public ViewPager mViewPager;
    public CustomPagerAdapter adapter;
    private String[] urlList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 자동 생성된 메소드 스텁
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload);

        TextView tvTitle = (TextView) findViewById(R.id.reloadTitle);
        TextView tvContent = (TextView) findViewById(R.id.reloadContent);

        urlList = getIntent().getStringArrayExtra("url");
        String title = getIntent().getStringExtra("title");
        String sContent = getIntent().getStringExtra("content");

        String[] arrayString = sContent.split("\\. ");

        String content = "";

        for (int i = 0; i < arrayString.length - 1; i++) {
            content = content + arrayString[i] + ".\n";
        }

        content += arrayString[arrayString.length - 1];

        tvTitle.setText(title);
        tvContent.setText(content);
        mViewPager = (ViewPager) findViewById(R.id.reloadViewPager);
        mViewPager.setPageMargin(50);
        mViewPager.setOffscreenPageLimit(urlList.length);

        List<String> tempList = new ArrayList();
        for(String url : urlList){
            tempList.add(url);
        }

        adapter = new CustomPagerAdapter(this, title, tempList, true);
        mViewPager.setAdapter(adapter);
    }
}
