package com.samar.location;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samar.location.models.House;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ViewHouseDetailsActivity  extends AppCompatActivity {

    private ImageView housecardImage;

    GridView gallery;
    Button contact_owner,chat;
    TextView housecardcity,contactPerson,houseNo,location,street,phone,price,size;
     List<String> imageUrls ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house_details);

        housecardImage = findViewById(R.id.housecardImage);

        gallery = findViewById(R.id.gallery);
        House house = (House) getIntent().getSerializableExtra("house");

        imageUrls=Arrays.asList(
                house.getImage1(),
                house.getImage2(),
                house.getImage3()
        );
        String[] imagesUrl =new String[3];

               imagesUrl[0] = house.getImage1();
               imagesUrl[1] = house.getImage2();
               imagesUrl[2] =  house.getImage3();


        Glide.with(getApplicationContext())
                .load( imagesUrl[0])
                .into(housecardImage);


        contact_owner=findViewById(R.id.call);
        chat = findViewById(R.id.chat);

        housecardcity = findViewById(R.id.housecardcity);
        contactPerson = findViewById(R.id.contactPerson);
        houseNo= findViewById(R.id.houseNo);
        location = findViewById(R.id.location);
        street= findViewById(R.id.street);
        price = findViewById(R.id.price);
        size = findViewById(R.id.size);

        housecardcity.setText(house.getCity());
        contactPerson.setText(house.getContactPerson());
        houseNo.setText(house.getHouseNo());
        location.setText(house.getLocation());

        street.setText(house.getStreet());
        price.setText(house.getPrice() + "DT");
        size.setText(house.getSize());

        contact_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="tel:"+house.getPhone();


                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(phone));

                startActivity(intent);
            }
        });

        //

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DiscussionActivity.class);
                intent.putExtra("ownerEmail",house.getOwnerEmail() );
                startActivity(intent);

            }
        });

        //

        GalleryAdapter adapter = new GalleryAdapter(getApplicationContext(), imagesUrl, housecardImage );
        gallery.setAdapter(adapter);
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


