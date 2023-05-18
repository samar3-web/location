package com.samar.location.privateSpace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.samar.location.R;
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
    private final Filter filterHouse = new Filter() {
        //FilterResults filterResults;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchText = constraint.toString().toLowerCase();
            List<House> tempList = new ArrayList<>();
            if (searchText.length() == 0 || searchText.isEmpty()) {
                tempList.addAll(listFull);
            } else if (searchText != null) {
                for (House house : listFull) {
                    if (house.getCity().toLowerCase().contains(searchText)) {
                        tempList.add(house);
                    }

                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = tempList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            houses.clear();
            houses.addAll((Collection<? extends House>) results.values);
            notifyDataSetChanged();
        }
    };
    CustomGalleryAdapter cga;
    List images;

    public UserHousesAdapter(Context context, List houses, int customlayout_id) {
        this.context = context;
        this.houses = houses;
        this.customlayout_id = customlayout_id;
        listFull = new ArrayList<>(houses);
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
        images = new ArrayList();
        for (int i = 0; i < house.getImages().size(); i++)
            images.add(house.getImages().get(i));

        Glide.with(context).load(house.getImages().get(0)).into(holder.housecardImage);
        cga = new CustomGalleryAdapter(context, images);


        if (house.getCity() != null)
            holder.housecardCity.setText(house.getCity().toUpperCase() + ",TUNISIA");
        if (house.getSize() != null)
            holder.housecardSize.setText(house.getSize());
        if (house.getPrice() != null)
            holder.housecardPrice.setText(house.getPrice() + "TND");
        //if(Objects.equals(house.getPrice(), "800"))
        if (!house.isAuthorized()) {
            Log.d("nnnnnnnnnnnnnnnn", "house.isAuthorized() " + house.isAuthorized());
            holder.linearCard.setAlpha(0.07f);
        }
        /*for(int j=0;j<houses.size();j++){
            if(houses.get(j).isAuthorized()== true)
            { Log.d("nnnnnnnnnnnnnnnn","house.isAuthorized() "+house.isAuthorized());
                holder.linearCard.setAlpha(0.1f);}
        }*/

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView housecardImage;
        Button housecardSize, housecardPrice, details, btn_delete, rentit;
        TextView housecardCity, housecardAddress;
        MaterialCardView cardView;
        LinearLayout collapseable, linearCard;
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

            btn_delete = view.findViewById(R.id.btn_delete);
            rentit = view.findViewById(R.id.rentit);

            linearCard = view.findViewById(R.id.linearCard);


            Checkout.preload(context);


            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("You are sure you want to delete this House?")
                            .setCancelable(false)
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String documentId = houses.get(getAdapterPosition()).getDocId();

                                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                    firebaseFirestore.collection("HouseCollection").document(documentId)
                                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override


                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Toast.makeText(context, "House Deleting Successful", Toast.LENGTH_SHORT).show();

                                                    //Redirection to UserSpaceActivity with FLAG_ACTIVITY_CLEAR_TOP
                                                    Activity parentActivity = (Activity) v.getContext();
                                                    parentActivity.finish();
                                                    parentActivity.startActivity(new Intent(parentActivity, UserSpaceActivity.class));

                                                }
                                            });


                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
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

                    String houseDocId = houses.get(getAdapterPosition()).getDocId();
                    intent.putExtra("houseDocId", houseDocId);
                    v.getContext().startActivity(intent);


                }
            });


        }


    }


}
