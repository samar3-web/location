package com.samar.location.privateSpace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.samar.location.BottomNavigationBar.BootomNavBarMain;
import com.samar.location.R;
import com.samar.location.authentication.LoginActivity;
import com.samar.location.homepage.RecyclerViewAdapter;
import com.samar.location.models.House;

import java.util.ArrayList;
import java.util.List;

public class UserSpaceActivity extends AppCompatActivity {
    //Global variables
    private  RecyclerView recyclerView;
    private  UserHousesAdapter adapter;
    List<House> houses;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_space);

        recyclerView = findViewById(R.id.my_rcv);

        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        String currentUserUid=FirebaseAuth.getInstance().getCurrentUser().getUid();





        getHouseData(currentUserUid);


    }




    private void getHouseData(String currentUserUid){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("HouseCollection").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        Log.d("xxxxxdocs", "onComplete: of HouseData fetching "+task.getResult().getDocuments()

                        );
                        houses=new ArrayList<>();
                        //TextView tv = findViewById(R.id.tv);

                        for(DocumentSnapshot doc : task.getResult().getDocuments())

                        {


                            if (doc.get("ownerUid").toString().equals(currentUserUid)){


                                House house=new House();
                                house.setDocId(doc.getId());
                                house.setLocation(doc.get("location").toString());
                                house.setSize(doc.get("size").toString());
                                house.setPrice((doc.get("price")).toString());
                                house.setCity(doc.get("city").toString());
                                house.setContactPerson(doc.get("contactPerson").toString());
                                house.setHouseNo(doc.get("houseNo").toString());
                                house.setStreet(doc.get("street").toString());
                                house.setPost(doc.get("post").toString());

                                Log.d("xxxavavailability", "onComplete: "+(boolean)doc.get("availability"));

                                house.setAvailability( (boolean) doc.get("availability"));
                                house.setPhone(doc.get("phone").toString());
                                if(doc.get("image1")!=null)
                                    house.setImage1(doc.get("image1").toString());
                                if(doc.get("image2")!=null)
                                    house.setImage2(doc.get("image2").toString());
                                if(doc.get("image3")!=null)
                                    house.setImage3(doc.get("image3").toString());
                                if(doc.get("image4")!=null)
                                    house.setImage4(doc.get("image4").toString());
                                if(doc.get("image5")!=null)
                                    house.setImage5(doc.get("image5").toString());

                                houses.add(house);
                            }

                            TextView tv = findViewById(R.id.tv);
                            tv.setText("You have "+houses.size()+" Houses" );

                            // adapter les resultat aux view

                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new UserHousesAdapter(getApplicationContext(), houses, R.layout.user_house_cardview);
                            recyclerView.setAdapter(adapter);

                        }}
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("xxxxx", "onFailure: of HouseData fectching "+e.getLocalizedMessage());
                    }
                });

    }
}