package com.samar.location.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.R;
import com.samar.location.ViewHouseDetailsActivity;
import com.samar.location.ViewHouseUserDetailsActivity;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.models.CustomGalleryAdapter;
import com.samar.location.models.House;
//import com.samar.location.payment.PaymentActivity;
import com.google.android.material.card.MaterialCardView;
import com.razorpay.Checkout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
        implements Filterable {
    Context context;
    int customlayout_id;
    List<House> houses; //list
    List<House> listFull;

    List images;
    ImageSlider imageSlider;

    ArrayList imageList;

    String user;

    FirebaseDB firebaseDB;


    public RecyclerViewAdapter(Context context, List houses, int customlayout_id) {
        this.context = context;
        this.houses = houses;
        this.customlayout_id = customlayout_id;
        listFull=new ArrayList<>(houses);
        this.user =  FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public void filter(String text) {
        List<House> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(listFull);
        } else {
            String searchText = text.toLowerCase();
            for (House house : listFull) {
                if (house.getLocation().toLowerCase().contains(searchText) || house.getSize().toLowerCase().contains(searchText)
                        || house.getPrice().toLowerCase().contains(searchText) || house.getCity().toLowerCase().contains(searchText)
                        || house.getContactPerson().toLowerCase().contains(searchText) || house.getHouseNo().toLowerCase().contains(searchText)
                        || house.getStreet().toLowerCase().contains(searchText) || house.getPost().toLowerCase().contains(searchText)) {
                    filteredList.add(house);
                }
            }
        }
        houses.clear();
        houses.addAll(filteredList);
        notifyDataSetChanged();
    }


    public void filter() {
        houses.clear();
        houses.addAll(listFull);
        notifyDataSetChanged();
    }
    public void sortData(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "available":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {
                        // Compare the available status of the houses
                        if (o1.isAvailability() && !o2.isAvailability()) {
                            return -1;
                        } else if (!o1.isAvailability() && o2.isAvailability()) {
                            return 1;
                        } else {
                            // If both houses have the same availability status, compare their prices
                            return o1.getPrice().compareTo(o2.getPrice());
                        }
                    }
                });
                break;
            case "price":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {

                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
                break;
            case "views":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {
                         if(o1.getViews()   > o2.getViews() )
                            return -1;

                         return  1;
                    }
                });
                break;

            case "type":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {
                        return o1.getSize().compareTo(o2.getSize());
                    }
                });
                break;
            case "lastModifiedDate":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {
                        return o1.getLastModifiedDate().compareTo(o2.getLastModifiedDate());
                    }
                });
                break;

            case "additionDate":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {
                        return o1.getAdditionDate().compareTo(o2.getAdditionDate());
                    }
                });

                break;
            case "none":
                houses.clear();
                houses.addAll(listFull);
                break;
            default:
                throw new IllegalArgumentException("Invalid sortBy argument: " + sortBy);
        }
        notifyDataSetChanged();
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(customlayout_id, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        House house = houses.get(position);
        images = house.getImages();
        imageList = new ArrayList<SlideModel>();
        for(int i=0;i<images.size();i++){
            if(images.get(i) !=null)
                imageList.add(new SlideModel( (String) images.get(i), null, null));
        }




        imageSlider.startSliding(5000); // with new period
        imageSlider.setImageList(imageList);


        holder.housecardCity.setText(house.getCity().toUpperCase()+", TUNISIA");
        holder.housecardType.setText(house.getSize());
        holder.housecardPrice.setText(house.getPrice()+".TND");
        holder.housecardlastDateModified.setText(  house.getLastModifiedDate());
        holder.housecardviews.setText( Long.toString(house.getViews()) );

        isFavorite(house.getDocId(), holder.favorite);

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
        ViewPager viewPager;
        //Button details,rentit;
        TextView housecardPrice,housecardType,housecardviews;
        TextView housecardCity;
        TextView housecardlastDateModified;
        MaterialCardView cardView;
        LinearLayout collapseable;

        ImageView favorite;



        public ViewHolder(View view) {
            super(view);
            //Getting all the views
            viewPager = view.findViewById(R.id.housecardImage);
            housecardType = view.findViewById(R.id.housecardsize);
            housecardPrice = view.findViewById(R.id.housecardprice);
            housecardCity = view.findViewById(R.id.housecardcity);
            housecardviews = view.findViewById(R.id.housecardviews);
            housecardlastDateModified = view.findViewById(R.id.housecardlastDateModified);
            favorite = view.findViewById(R.id.favorite);

            cardView = view.findViewById(R.id.card);
            collapseable = view.findViewById(R.id.collapsable);


            imageSlider = view.findViewById(R.id.image_slider);

            Checkout.preload(context);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    House house= houses.get(getAdapterPosition());
                    //check if this house is for the current user

                    Intent intent;
                    if ( Objects.equals(user, house.getOwnerEmail())   ){
                        intent = new Intent(context, ViewHouseUserDetailsActivity.class);
                        intent.putExtra("houseDocId",house.getDocId());

                    }else {


                        //update views number
                        house.setViews(house.getViews() + 1 );
                        firebaseDB = new FirebaseDB();
                        firebaseDB.updateHouseData(house.getDocId(),house,context);

                        intent = new Intent(context, ViewHouseDetailsActivity.class);
                        intent.putExtra("house", house);
                    }
                    context.startActivity(intent);


                }
            });

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //favorite.setImageResource(R.drawable.favorite);
                    //updatesData
                    House house= houses.get(getAdapterPosition());
                    updateFavoriteHouseData( house.getDocId(),favorite );
                }
            });

        }

        
    }

    private void isFavorite(String docId, ImageView favorite){


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String currentUserEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference= firestore.collection("USERDATA").document(currentUserEmail);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("xxxxcustomer", "onComplete: customer notification " + task.getResult().getData().toString());

                    List<String> requests = (List<String>) task.getResult().get("requests");

                    if (requests != null && requests.contains(docId) ) {
                        favorite.setImageResource(R.drawable.favorite);
                    }
                    else{
                        favorite.setImageResource(R.drawable.no_favorite);
                    }


                }
            }
        });
    }

    private void updateFavoriteHouseData(String docId,ImageView favorite) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = firestore.collection("USERDATA").document(currentUserEmail);

        // Requête pour récupérer les données de l'utilisateur actuel
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Récupérer les valeurs actuelles de requests
                    List<String> requests = (List<String>) documentSnapshot.get("requests");

                    if (requests != null && requests.contains(docId)) {
                        // docId existe déjà dans requests, le supprimer
                        documentReference.update("requests", FieldValue.arrayRemove(docId))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        favorite.setImageResource(R.drawable.no_favorite);

                                    }
                                });
                    } else {
                        // docId n'existe pas dans requests, l'ajouter
                        documentReference.update("requests", FieldValue.arrayUnion(docId))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        favorite.setImageResource(R.drawable.favorite);
                                    }
                                });
                    }
                }
            }
        });
    }

}
