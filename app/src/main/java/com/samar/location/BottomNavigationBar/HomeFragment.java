package com.samar.location.BottomNavigationBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.samar.location.R;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.homepage.HomeTabs_Adpater;
import com.google.android.material.tabs.TabLayout;
import com.samar.location.homepage.RecyclerViewAdapter;
import com.samar.location.models.House;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //Global variables
    RecyclerView my_rcv;
    ImageView userFace;
    RecyclerViewAdapter recyclerViewAdapter;
    List<House> houses;
    public Bundle bundle;

    /*



    TabLayout home_tabs;
    ViewPager2 home_viewpager;
    HomeTabs_Adpater homeTabs_adpater;



     */

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_house_list_tab, container, false);

        my_rcv = view.findViewById(R.id.my_rcv);

        userFace = view.findViewById(R.id.userFace);

        displayUserFace();


        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnavbar);

        //Getting the house data from database and showing on houseList tab

        getHouseData();






        my_rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
                       /*
                           super.onScrollStateChanged(recyclerView, newState);
                           if (newState == RecyclerView.SCROLL_STATE_IDLE)
                           {

                               bottomNavigationView.setVisibility(View.VISIBLE);



                           }

                        */

            }

            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
                           /*
                           super.onScrolled(recyclerView, dx, dy);


                           if (dy > 0 ||dy<0 )
                           {

                               bottomNavigationView.setVisibility(View.GONE);


                           }

                            */

            }
        });



        //Old version with Map
         /*
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        home_tabs = view.findViewById(R.id.my_tablayout);
        home_viewpager=view.findViewById(R.id.my_viewpager);



        //Tabs are created
        home_tabs.addTab(home_tabs.newTab().setText("LIST"));
        home_tabs.addTab(home_tabs.newTab().setText("MAP"));

        //now we need to create adpater class to provide fragmnet view object
        homeTabs_adpater = new HomeTabs_Adpater(getActivity().getSupportFragmentManager(), getLifecycle());


        //set adapter in viewpage to get the object of fragment view
        home_viewpager.setAdapter(homeTabs_adpater);

        //click on tabs to show tab
        home_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                home_viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //slide horixontal to show tab
        home_viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                home_tabs.selectTab(home_tabs.getTabAt(position));
            }
        });

         */

        return view;



    }


    private void getHouseData(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("HouseCollection").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        Log.d("xxxxxdocs", "onComplete: of HouseData fetching "+task.getResult().getDocuments());
                        houses=new ArrayList<>();
                        for(DocumentSnapshot doc : task.getResult().getDocuments())
                        {
                            if ((boolean)doc.get("availability") == true){
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
                                house.setOwnerUid(doc.get("ownerUid").toString());
                                if(doc.get("ownerEmail")!=null){
                                    house.setOwnerEmail( doc.get("ownerEmail").toString() );
                                }


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


                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),houses, R.layout.houses_cardview);
                            //setting adapter to recycler view
                            my_rcv.setAdapter(recyclerViewAdapter);
                            //LayoutManager for recycler view
                            my_rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

                        }}
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("xxxxx", "onFailure: of HouseData fectching "+e.getLocalizedMessage());
                    }
                });

    }



    private void displayUserFace() {
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document(currentUserEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {
                        if (snapshot.get("profileUrl") != null) {
                            String profileUrl = snapshot.get("profileUrl").toString();
                            //dispay image
                            setProfile(profileUrl);
                        }

                    } catch (Exception e) {

                    }


                }
            }
        });
    }

    private void setProfile(String profileUrl) {
        Glide.with(getActivity())
                .load(profileUrl)
                .into(userFace);
    }
}