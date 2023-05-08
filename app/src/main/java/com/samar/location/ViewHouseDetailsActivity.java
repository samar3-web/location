package com.samar.location;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapView;
import com.samar.location.models.House;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewHouseDetailsActivity  extends AppCompatActivity {

    private ImageView housecardImage,back;
    Button contact_owner,chat;
    TextView housecardcity,contactPerson,houseNo,location,street,phone,price,size;
     List<String> imageUrls ;
    private static final int CALL_PERMISSION_REQUEST_CODE = 1;
    private MapView mapView;
    private FloatingActionButton mFab;
    private MapElementLayer mPinLayer;
    private MapImage mPinImage;
    private int mUntitledPushpinCount = 0;
    private Geopoint geopoint;
    private static final String MY_API_KEY = "AlwLTKgevIemLkhFY8wA2oDQwpxY8SBBAR8a5dXymXDFKTmfGWKkXnJGQkGzXUMM";
    private House house;
    RecyclerView recyclerView;
    public static String houseDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house_details);
        houseDocId =  getIntent().getStringExtra("houseDocId");

        housecardImage = findViewById(R.id.housecardImage);
        back = findViewById(R.id.back_home);

        recyclerView = findViewById(R.id.recyclerViewHorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        contact_owner=findViewById(R.id.call);
        chat = findViewById(R.id.chat);

        contactPerson = findViewById(R.id.contactPerson);
        houseNo= findViewById(R.id.houseNo);
        location = findViewById(R.id.location);
        street= findViewById(R.id.street);
        price = findViewById(R.id.price);
        size = findViewById(R.id.size);
        //
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mapView = findViewById(R.id.mapView);
        mapView.setCredentialsKey(MY_API_KEY);
        mapView.onCreate(savedInstanceState);
        showHouseDetails(houseDocId);




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

    private void showHouseDetails(String houseDocId) {


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("HouseCollection").document(houseDocId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getId());
                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getData());
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {
                         house=new House();
                         house.setDocId(task.getResult().getId());
                        house.setLocation(snapshot.get("location").toString());
                        house.setSize(snapshot.get("size").toString());
                        house.setPrice((snapshot.get("price")).toString());
                        house.setCity(snapshot.get("city").toString());
                        house.setContactPerson(snapshot.get("contactPerson").toString());
                        house.setHouseNo(snapshot.get("houseNo").toString());
                        house.setStreet(snapshot.get("street").toString());
                        house.setPost(snapshot.get("post").toString());

                        house.setAvailability( (boolean) snapshot.get("availability"));

                        house.setPhone(snapshot.get("phone").toString());

                        house.setOwnerEmail( snapshot.get("ownerEmail").toString() );

                        house.setAuthorized((boolean) snapshot.get("authorized") );

                        house.setViews((long) snapshot.get("views") );

                        house.setAddedDate( (Timestamp)  snapshot.get("addedDate"));

                        house.setLastModifiedDate( (Timestamp) snapshot.get("lastModifiedDate") );

                        if(snapshot.get("latitude")  != null){
                            house.setLatitude((double)  snapshot.get("latitude") );
                        }
                        if(snapshot.get("longitude")  != null){
                            house.setLongitude((double)  snapshot.get("longitude") );
                        }

                        if(snapshot.get("images") != null){
                            List<String> images = (List<String>) snapshot.get("images");
                            house.setImages(images);

                        }

                        Glide.with(getApplicationContext())
                                .load( house.getImages().get(0) )
                                .into(housecardImage);
                        PhotoViewAttacher pAttacher;
                        pAttacher = new PhotoViewAttacher(housecardImage);
                        pAttacher.update();

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


                        mPinLayer = new MapElementLayer();
                        mapView.getLayers().add(mPinLayer);
                        mPinImage = getPinImage();
                        geopoint = new Geopoint(house.getLatitude(), house.getLongitude());
                        addPin(geopoint, house.getContactPerson()+"'s House");

                    } catch (Exception e) {
                        Log.d("xxxxxxx", "onComplete Exception in setting data to house : " + e.getLocalizedMessage());
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("xxxx", "onFailure: " + e.getLocalizedMessage());
            }
        });
    }
    private MapImage getPinImage() {
        // Create a pin image from a drawable resource
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin, null);

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return new MapImage(bitmap);
    }

    private void addPin(Geopoint location, String title) {
        // Add a pin to the map at the given location
        MapIcon pushpin = new MapIcon();
        pushpin.setLocation(location);
        pushpin.setTitle(title);
        pushpin.setImage(mPinImage);

        pushpin.setNormalizedAnchorPoint(new PointF(0.5f, 1f));
        if (title.isEmpty()) {
            pushpin.setContentDescription(String.format(
                    Locale.ROOT,
                    "Untitled pushpin %d",
                    ++mUntitledPushpinCount));
        }
        mPinLayer.getElements().add(pushpin);
    }
}


