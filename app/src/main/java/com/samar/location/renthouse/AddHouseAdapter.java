package com.samar.location.renthouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samar.location.R;

import java.util.List;


public class AddHouseAdapter extends RecyclerView.Adapter<AddHouseAdapter.HouseHolder> {
    Context context;
    List selectedImages;

    public AddHouseAdapter(Context context, List selectedImages) {
        this.context = context;
        this.selectedImages = selectedImages;

    }


    @Override
    public HouseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.imageview_addhouse_custom, parent, false);
        HouseHolder houseHolder = new HouseHolder(view);
        return houseHolder;
    }

    @Override
    public void onBindViewHolder(HouseHolder holder, int position) {
        /*  holder.houseImage.setImageDrawable(context.getResources().getDrawable((Integer) selectedImages.get(position)));*/
        Glide.with(context)
                .load(selectedImages.get(position).toString())
                .into(holder.houseImage);
    }

    @Override
    public int getItemCount() {
        return selectedImages.size();
    }

    public class HouseHolder extends RecyclerView.ViewHolder {
        ImageView houseImage;

        public HouseHolder(View itemView) {
            super(itemView);
            houseImage = itemView.findViewById(R.id.houseImage);

        }
    }
}
