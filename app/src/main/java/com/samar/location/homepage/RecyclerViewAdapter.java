package com.samar.location.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.samar.location.R;
import com.samar.location.ViewHouseDetailsActivity;
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
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
        implements Filterable {
    Context context;
    int customlayout_id;
    List<House> houses; //list
    List<House> listFull;
    CustomGalleryAdapter cga;
    List images;
    ImageSlider imageSlider;

    ArrayList imageList;
    public RecyclerViewAdapter(Context context, List houses, int customlayout_id) {
        this.context = context;
        this.houses = houses;
        this.customlayout_id = customlayout_id;
        listFull=new ArrayList<>(houses);
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
    public void sortData(String sortBy) {
        switch (sortBy.toLowerCase()) {
            /*case "date":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
                break;*/
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
            case "size":
                Collections.sort(houses, new Comparator<House>() {
                    @Override
                    public int compare(House o1, House o2) {
                        return o1.getSize().compareTo(o2.getSize());
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
            images= house.getImages();
        imageList = new ArrayList<SlideModel>();
        for(int i=0;i<images.size();i++){
            if(images.get(i) !=null)
                imageList.add(new SlideModel( (String) images.get(i), null, null));
        }


        imageSlider.startSliding(2000); // with new period

       // imageSlider = (ImageSlider) findViewById(R.id.image_slider);

        /*for (String i : site.getImgs()) {
            imageList.add(new SlideModel(i, null, null));

        }*/
       /* imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                // Vous pouvez ajouter votre code ici
                Toast.makeText(context, "hello",Toast.LENGTH_SHORT);
            }
        });*/
        imageSlider.setImageList(imageList);
       /* imageSlider.setItemClickListener( i -> {
            Toast.makeText(imageSlider.getContext(), "hello "+i,Toast.LENGTH_SHORT);
            System.out.println("Clicked on " + i);
        });*/
              /*
            Glide.with(context).load(house.getImage1()).into(holder.housecardImage);
            cga=new CustomGalleryAdapter(context,images);
            holder.gallery.setAdapter(cga);
            holder.gallery.setSpacing(5);

            holder.gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Glide.with(context).load(images.get(position)).into(holder.housecardImage);

                }
            });

            boolean isExpanded=house.isExpanded();

               */

        /*ImageAdapter adapter = new ImageAdapter(context, images);
        holder. viewPager.setAdapter(adapter);*/


            holder.housecardCity.setText(house.getCity().toUpperCase()+", TUNISIA");

            holder.housecardSize.setText(house.getSize());
            holder.housecardPrice.setText(house.getPrice()+".TND");

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
        ViewPager viewPager;
        //Button details,rentit;
        TextView housecardPrice,housecardSize;
        TextView housecardCity, housecardAddress;
        MaterialCardView cardView;
        LinearLayout collapseable;



        public ViewHolder(View view) {
            super(view);
            //Getting all the views
            viewPager = view.findViewById(R.id.housecardImage);
            housecardSize = view.findViewById(R.id.housecardsize);
            housecardPrice = view.findViewById(R.id.housecardprice);
            housecardCity = view.findViewById(R.id.housecardcity);

            cardView = view.findViewById(R.id.card);
            collapseable = view.findViewById(R.id.collapsable);


            imageSlider = view.findViewById(R.id.image_slider);

            Checkout.preload(context);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewHouseDetailsActivity.class);

                    House house= houses.get(getAdapterPosition());
                    intent.putExtra("house",house);
                    context.startActivity(intent);

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



        }






        
    }

    private static class ImageAdapter extends PagerAdapter {

        private Context context;
        private List<String> imageUrls;

        public ImageAdapter(Context context, List<String> imageUrls) {
            this.context = context;
            this.imageUrls = imageUrls;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(imageUrls.get(position)).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }


}
