package com.michael.moviecake;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * GridView adapter; interface for populating movie poster GridView
 */
public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mPosterThumbs = new String[0];

    public MovieAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mPosterThumbs.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setTransitionName("transition:POSTER");
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 500));


            imageView.setPadding(7, 7, 7, 7);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mPosterThumbs[position])
                .placeholder(R.drawable.cake)
                .into(imageView);
        return imageView;
    }

    public void setData(String[] posterData) {
        mPosterThumbs = posterData;
    }
}