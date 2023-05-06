package com.samar.location;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewHouseDetailsActivity  extends AppCompatActivity {

    private ImageView housecardImage,back;
    Button contact_owner,chat;
    TextView housecardcity,contactPerson,houseNo,location,street,phone,price,size;
     List<String> imageUrls ;
    private static final int CALL_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house_details);

        housecardImage = findViewById(R.id.housecardImage);
        back = findViewById(R.id.back_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));




        House house = (House) getIntent().getSerializableExtra("house");



        Glide.with(getApplicationContext())
                .load( house.getImages().get(0) )
                .into(housecardImage);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(housecardImage);
        pAttacher.update();


        contact_owner=findViewById(R.id.call);
        chat = findViewById(R.id.chat);

        contactPerson = findViewById(R.id.contactPerson);
        houseNo= findViewById(R.id.houseNo);
        location = findViewById(R.id.location);
        street= findViewById(R.id.street);
        price = findViewById(R.id.price);
        size = findViewById(R.id.size);


        contactPerson.setText(house.getContactPerson());
        houseNo.setText(house.getHouseNo());
        location.setText(house.getLocation());

        street.setText(house.getStreet());
        price.setText(house.getPrice() + "DT");
        size.setText(house.getSize());

        contact_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ViewHouseDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
                } else {
                String phone = "tel:" + house.getPhone();


                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(phone));

                startActivity(intent);
            }
            }
        });

        //
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DiscussionActivity.class);
                intent.putExtra("friendEmail",house.getOwnerEmail() );
                startActivity(intent);

            }
        });





        RecyclerViewHorizontalAdapter adapter = new RecyclerViewHorizontalAdapter(house.getImages(),housecardImage );
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission granted to make phone calls", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Permission denied to make phone calls", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


