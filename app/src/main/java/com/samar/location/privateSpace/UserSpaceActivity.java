package com.samar.location.privateSpace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.samar.location.R;
import com.samar.location.models.House;
import com.samar.location.renthouse.AddHouseActivity;

import java.util.ArrayList;
import java.util.List;

public class UserSpaceActivity extends AppCompatActivity {
    Button add_btn;
    List<House> houses;
    FirebaseAuth firebaseAuth;
    //Global variables
    private RecyclerView recyclerView;
    private UserHousesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_space);

        recyclerView = findViewById(R.id.my_rcv);
        add_btn = findViewById(R.id.add_btn);

        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHouseActivity.class);
                intent.putExtra("currentUserUid", currentUserUid);
                startActivity(intent);
                //finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        getHouseData(currentUserEmail);


    }


    private void getHouseData(String currentUserEmail) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("HouseCollection").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        Log.d("xxxxxdocs", "onComplete: of HouseData fetching " + task.getResult().getDocuments()

                        );
                        houses = new ArrayList<>();
                        //TextView tv = findViewById(R.id.tv);

                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {


                            if (doc.get("ownerEmail").toString().equals(currentUserEmail)) {


                                House house = new House();
                                house.setDocId(doc.getId());
                                if (doc.get("location") != null)
                                    house.setLocation(doc.get("location").toString());

                                if (doc.get("size") != null)
                                    house.setSize(doc.get("size").toString());
                                if (doc.get("price") != null)
                                    house.setPrice((doc.get("price")).toString());
                                if (doc.get("city") != null)
                                    house.setCity(doc.get("city").toString());
                                if (doc.get("contactPerson") != null)
                                    house.setContactPerson(doc.get("contactPerson").toString());
                                if (doc.get("houseNo") != null)
                                    house.setHouseNo(doc.get("houseNo").toString());
                                if (doc.get("street") != null)
                                    house.setStreet(doc.get("street").toString());
                                if (doc.get("post") != null)
                                    house.setPost(doc.get("post").toString());

                                Log.d("xxxavavailability", "onComplete: " + (boolean) doc.get("availability"));

                                if (doc.get("availability") != null)
                                    house.setAvailability((boolean) doc.get("availability"));
                                if (doc.get("authorized") != null)
                                    house.setAuthorized((boolean) doc.get("authorized"));
                                if (doc.get("phone") != null)
                                    house.setPhone(doc.get("phone").toString());

                                if (doc.get("lastModifiedDate") != null)
//                                    house.setLastModifiedDate( (Timestamp) doc.get("lastModifiedDate") );
                                    if (doc.get("additionDate") != null)
                                        //  house.setAddedDate( (Timestamp) doc.get("additionDate") );
                                        if (doc.get("longitude") != null)
                                            house.setLongitude((double) doc.get("longitude"));
                                if (doc.get("latitude") != null)
                                    house.setLatitude((double) doc.get("latitude"));

                                if (doc.get("images") != null) {
                                    house.setImages((List<String>) doc.get("images"));
                                }

                                houses.add(house);
                            }

                            TextView tv = findViewById(R.id.tv);
                            tv.setText("You have " + houses.size() + " Houses");

                            // adapter les resultat aux view

                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new UserHousesAdapter(getApplicationContext(), houses, R.layout.user_house_cardview);
                            recyclerView.setAdapter(adapter);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("xxxxx", "onFailure: of HouseData fectching " + e.getLocalizedMessage());
                    }
                });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        System.gc();
    }


}