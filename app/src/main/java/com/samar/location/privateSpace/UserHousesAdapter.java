package com.samar.location.privateSpace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.razorpay.Checkout;
import com.samar.location.R;
import com.samar.location.ViewHouseDetailsActivity;
import com.samar.location.ViewHouseUserDetailsActivity;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.models.CustomGalleryAdapter;
import com.samar.location.models.House;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserHousesAdapter extends RecyclerView.Adapter<UserHousesAdapter.ViewHolder>
        implements Filterable {
    Context context;
    int customlayout_id;
    List<House> houses; //list
    List<House> listFull;
    CustomGalleryAdapter cga;
    List images;

    public UserHousesAdapter(Context context, List houses, int customlayout_id) {
        this.context = context;
        this.houses = houses;
        this.customlayout_id = customlayout_id;
        listFull=new ArrayList<>(houses);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = LayoutInflater.from(parent.getContext())
                .inflate(customlayout_id, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHousesAdapter.ViewHolder holder, int position) {
        House house = houses.get(position);
        images=new ArrayList();
        for(int i = 0; i<house.getImages().size();i++)
            images.add(house.getImages().get(i));

        Glide.with(context).load(house.getImages().get(0)).into(holder.housecardImage);
        cga=new CustomGalleryAdapter(context,images);







        holder.housecardCity.setText(house.getCity().toUpperCase()+",TUNISIA");

        holder.housecardSize.setText(house.getSize());
        holder.housecardPrice.setText(house.getPrice()+"TND");

        //holder.collapseable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }




    @Override
    public int getItemCount() {

        return houses.size();
    }

    @Override
    public Filter getFilter() {
        return filterHouse;
    }

    private  Filter filterHouse=new Filter() {
        //FilterResults filterResults;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchText=constraint.toString().toLowerCase();
            List<House> tempList=new ArrayList<>();
            if(searchText.length()==0 || searchText.isEmpty())
            {
                tempList.addAll(listFull);
            }
            else if(searchText!=null)
            {
                for(House house:listFull)
                {
                    if(house.getCity().toLowerCase().contains(searchText))
                    {
                        tempList.add(house);
                    }

                }
            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=tempList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            houses.clear();
            houses.addAll((Collection<? extends House>) results.values);
            notifyDataSetChanged();
        }
    };





    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView housecardImage;
        Button housecardSize, housecardPrice,details,btn_delete,rentit;
        TextView housecardCity, housecardAddress;
        MaterialCardView cardView;
        LinearLayout collapseable;
        Gallery gallery;

        FirebaseDB firebaseDB = new FirebaseDB();


        public ViewHolder(View view) {
            super(view);
            //Getting all the views
            housecardImage = view.findViewById(R.id.housecardImage);
            housecardSize = view.findViewById(R.id.housecardsize);
            housecardPrice = view.findViewById(R.id.housecardprice);
            housecardCity = view.findViewById(R.id.housecardcity);

            cardView = view.findViewById(R.id.card);
            collapseable = view.findViewById(R.id.collapsable);

            btn_delete=view.findViewById(R.id.btn_delete);
            rentit=view.findViewById(R.id.rentit);


            Checkout.preload(context);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    House house = houses.get(getAdapterPosition());
                    house.setExpanded(!house.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    //  List<SlideModel> slideModelList = new ArrayList<>();

                }
            });
            housecardCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    House house = houses.get(getAdapterPosition());
                    house.setExpanded(!house.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });



            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    House house= houses.get(getAdapterPosition());
                    String documentId = house.getDocId();

                    //Delete

                    firebaseDB.deleteHouse(documentId,context);


                }
            });

            /*rentit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    House house= houses.get(getAdapterPosition());

                    //  Intent intent=new Intent(context, PaymentActivity.class);
//                    Bundle bundle=new Bundle();
                    // intent.putExtra("house",house);
                    //   context.startActivity(intent);
                    // startPayment();
                    //unregisterReceiver();
                }
            });*/
            //details.setPaintFlags(details.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            housecardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, ViewHouseUserDetailsActivity.class);

                    String houseDocId= houses.get(getAdapterPosition()).getDocId();
                    intent.putExtra("houseDocId",houseDocId);
                    v.getContext().startActivity(intent);



                }
            });


        }







    }


}
