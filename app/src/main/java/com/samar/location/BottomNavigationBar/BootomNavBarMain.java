package com.samar.location.BottomNavigationBar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.R;
import com.samar.location.authentication.LoginActivity;
import com.samar.location.homepage.RecyclerViewAdapter;

import java.util.List;


public class BootomNavBarMain extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerViewAdapter recyclerViewAdapter;

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
        homeFragment = new HomeFragment();
        //By default selected item and fragment when app starts
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame,
                homeFragment, "home_fragment").commit();


//        getSupportFragmentManager().beginTransaction().add(new MovieFragment(),"movie").commit();

        //Here we are getting account type and setting data
        getAccountType(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        //Adding badges to icons

        getRequestsCount();


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit the application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
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

                                getRequestsCount();

                                if (item.getItemId() == R.id.homee) {
                                    //  getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new HomeFragment(), "home_fragment").commit();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.bottomnavitem_frame, homeFragment, "home_fragment")
                                            //  .addToBackStack(null)
                                            .commit();


                                } else if (item.getItemId() == R.id.favourite) {
                                    if (accType.equals("CUSTOMER")) {

                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new Customer_Request_List())
                                                //.addToBackStack(null)
                                                .commit();


                                    } else {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new Customer_Request_List())
                                                //  .addToBackStack(null)
                                                .commit();
                                    }

                                } else if (item.getItemId() == R.id.chat) {

                                    {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new ChatFragment())
                                                // .addToBackStack(null)
                                                .commit();
                                    }
                                } else {
                                    if (accType.equals("CUSTOMER")) {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new Customer_Account_Fragment())
                                                //  .addToBackStack(null)
                                                .commit();
                                    } else {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomnavitem_frame, new Owner_Account_Fragment())
                                                //.addToBackStack(null)
                                                .commit();
                                    }

                                }


                                return true;
                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "NO DATA FOUND FOR THIS USER !!!", Toast.LENGTH_LONG).show();

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                        Log.d("xxxxxx", "onComplete: NO DATA");
                    }
                }
            }
        });

    }


    private void getRequestsCount() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = firestore.collection("USERDATA").document(currentUserEmail);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> requests = (List<String>) document.get("requests");
                        if (requests != null) {
                            int requestsCount = requests.size();

                            BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.favourite);
                            badge.setNumber(requestsCount);

                        }
                    }
                }
            }
        });
    }


}