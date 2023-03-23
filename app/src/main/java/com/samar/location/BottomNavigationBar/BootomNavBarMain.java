package com.samar.location.BottomNavigationBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.R;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.homepage.RecyclerViewAdapter;


public class BootomNavBarMain extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerViewAdapter recyclerViewAdapter;
    FirebaseDB firebaseDB;
    String accountType;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomnavbar_main);
        Intent intent = getIntent();
        String accountType = intent.getStringExtra("accountType");
        Log.d("xxxxx", "getAccountType(): " + accountType);


        //Getting bootom nav bar
        bottomNavigationView = findViewById(R.id.bottomnavbar);

        //Making the labels to be visible forever
        // bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
         homeFragment=new HomeFragment();
        //By default selected item and fragment when app starts
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame,
                homeFragment, "home_fragment").commit();


//        getSupportFragmentManager().beginTransaction().add(new MovieFragment(),"movie").commit();

        //Here we are getting account type and setting data
        getAccountType(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        //Adding badges to icons

        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.favourite);


        badge.setNumber(1);


        /*val badgeDrawable = bottomNavigation.getBadge(menuItemId)
        if (badgeDrawable != null) {
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }*/


    }

    public void getAccountType(String currentUserEmail) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document(currentUserEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {

                        Log.d("xxxxx", "onComplete: 1" + snapshot.get("accountType").toString());
                        String accType = snapshot.get("accountType").toString();

                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem item) {

                                if (item.getItemId() == R.id.homee) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new HomeFragment(), "home_fragment").commit();

                                }  else if (item.getItemId() == R.id.favourite) {
                                    if (accType.equals("CUSTOMER"))
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new Customer_Request_List()).commit();
                                    else
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new FavouriteFragment()).commit();

                                } else {
                                    if (accType.equals("CUSTOMER"))
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new Customer_Account_Fragment()).commit();
                                    else
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new Owner_Account()).commit();
                                }
                                return true;
                            }
                        });


                    } else {
                        Log.d("xxxxxx", "onComplete: NO DATA");
                    }
                }
            }
        });

    }

}