package com.example.beaconandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

//import static com.example.BeaconTest.CustomListView.getUrlImages;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String title;
    private List<String> image;
    public static int position;
    private boolean mNextActivityFlag;

    public CustomPagerAdapter(Context c, String title, List<String> image, boolean nextActivityFlag) {
        this.mContext = c;
        this.title = title;
        this.image = image;
        this.mNextActivityFlag = nextActivityFlag;

    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(mContext);

        if (mNextActivityFlag) {
            Picasso.with(mContext).load(image.get(position)).fit().centerCrop().into(imageView);
            container.addView(imageView, 0);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 자동 생성된 메소드 스텁
                    Intent intent = new Intent(mContext, ImageTouchActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("url", image.get(position));
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });
        }
        else {
            imageView.setAdjustViewBounds(true);
            Picasso.with(mContext).load(image.get(position)).fit().centerCrop().into(imageView);
            container.addView(imageView, 0);
        }

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
