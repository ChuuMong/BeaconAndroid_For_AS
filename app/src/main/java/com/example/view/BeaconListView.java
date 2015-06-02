package com.example.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.activity.ReloadActivity;
import com.example.activity.ReloadImageNullActivity;
import com.example.adapater.CustomPagerAdapter;
import com.example.model.Beacon;

public class BeaconListView extends BaseAdapter {

    private final Context mContext;
    private ViewPager mViewPager;
    private CustomPagerAdapter adapter;

    private TextView title;
    private TextView content;

    private List<Beacon> beaconList;

    public BeaconListView(Context context, List<Beacon> beaconList) {
        this.mContext = context;
        this.beaconList = beaconList;
    }

    @Override
    public int getCount() {
        return beaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return beaconList.get(0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = null;
        final Beacon beacon = beaconList.get(position);
        if (v == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
        }
        else {
            view = v;
        }

        if (beacon.getContent().length() >= 50) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_reload, null);
        }
        else {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
        }

        if (beacon.getPicture().get(0) == "null") {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_if_image_null, null);

            if (beacon.getContent().length() >= 50) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_if_image_null_reload, null);
            }
        }

        if (beacon != null) {
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            title.setText(beacon.getTitle());

            if (beacon.getContent().length() > 50) {
                content.setText(beacon.getContent().substring(0, 50) + "......");
                TextView reload = (TextView) view.findViewById(R.id.reload);
                reload.setText(Html.fromHtml("<u>" + "계속 읽기" + "</u>"));
                reload.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 자동 생성된 메소드 스텁
                        if (beacon.getPicture().get(0) == "null") {
                            Intent intent = new Intent(mContext, ReloadImageNullActivity.class);
                            intent.putExtra("title", beacon.getTitle());
                            intent.putExtra("content", beacon.getContent());
                            mContext.startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(mContext, ReloadActivity.class);
                            intent.putExtra("title", beacon.getTitle());
                            intent.putExtra("content", beacon.getContent());
                            intent.putExtra("url", beacon.getPictureArray());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
            else {
                content.setText(beacon.getContent());
            }

            if (beacon.getPicture().get(0) != "null") {
                mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
                mViewPager.setPageMargin(50);
                mViewPager.setOffscreenPageLimit(beacon.getPicture().size());
                adapter = new CustomPagerAdapter(mContext, beacon.getTitle(), beacon.getPicture(), true);
                mViewPager.setAdapter(adapter);
            }
        }
        return view;
    }
}
