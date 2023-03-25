package com.samar.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

        private Context mContext;
        private String[] mImageUrls;
        private ImageView mDisplayer;

        public GalleryAdapter(Context context, String[] imageUrls, ImageView displayer) {
            mContext = context;
            mImageUrls = imageUrls;
            mDisplayer = displayer;
        }

        @Override
        public int getCount() {
            return mImageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageButton imageButton;
            if (convertView == null) {
                imageButton = new ImageButton(mContext);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
                layoutParams.setMargins(10, 0, 10, 0); // Ajoute une marge de 10 pixels à gauche et à droite
                imageButton.setLayoutParams(layoutParams);
                imageButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
                imageButton.setPadding(8, 8, 8, 8);

            } else {
                imageButton = (ImageButton) convertView;
            }


            Glide.with(mContext)
                    .load(mImageUrls[position])
                    .into(imageButton);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(mContext)
                            .load(mImageUrls[position])
                            .into(mDisplayer);
                }
            });

            return imageButton;
        }

        private void displayImage(String url,ImageView displayer){
            Glide.with(mContext)
                    .load(url)
                    .into(displayer);

        }
    }


