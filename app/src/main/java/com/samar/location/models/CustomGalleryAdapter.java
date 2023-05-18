package com.samar.location.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.List;

public class CustomGalleryAdapter extends BaseAdapter {
    private final Context context;
    private final List imageUrl;

    public CustomGalleryAdapter(Context context, List imageUrl) {
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(context);
        //Glide.with(context)
        // .load(imageUrl.get(position))
        //    .into(imageView);
        imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));


        return imageView;
    }
}
