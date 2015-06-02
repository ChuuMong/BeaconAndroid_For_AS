/**
 * @package_name : com.example.BeaconTest
 * @file_name : ImageTouchActivity.java
 * @date : 2014. 10. 30. 
 * @time : 오후 4:02:59
 * @author : JongHun Lee
 * @Contect :
 */
package com.example.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.adapater.CustomPagerAdapter;
import com.example.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoom
 */
public class ImageTouchActivity extends Activity {

    public ViewPager mViewPager;
    public CustomPagerAdapter adapter;
    private String title;
    private String[] urlList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 자동 생성된 메소드 스텁
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_touch);

        String title = getIntent().getStringExtra("title");
        setTitle(title);

        urlList = getIntent().getStringArrayExtra("url");
        position = getIntent().getIntExtra("position", 0);

        mViewPager = (ViewPager) findViewById(R.id.viewPager_image);
        mViewPager.setPageMargin(50);
        // mViewPager.setOffscreenPageLimit(url.length);

        List<String> tempList = new ArrayList();
        for(String url : urlList){
            tempList.add(url);
        }

        adapter = new CustomPagerAdapter(this, this.title, tempList, false);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
    }

}
