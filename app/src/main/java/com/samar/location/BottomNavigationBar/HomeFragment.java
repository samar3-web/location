package com.samar.location.BottomNavigationBar;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.samar.location.R;
import com.samar.location.homepage.RecyclerViewAdapter;
import com.samar.location.models.House;

import java.time.LocalDateTime;
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

    TabLayout tabLayout ;

    final String[] tabs = {"All","Tendance","More View", "Luxe"};

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
    private ImageView filtreBtn;
    private TextView searchEt;
    private PopupMenu popupMenu;
    private SwipeRefreshLayout swipeRefreshLayout;

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


        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Mettez à jour votre liste ou vue de recyclage ici
                // Appeler setRefreshing(false) lorsque vous avez fini de rafraîchir la vue
                getHouseData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        my_rcv = view.findViewById(R.id.my_rcv);

        userFace = view.findViewById(R.id.userFace);

        tabLayout = view.findViewById(R.id.tab_layout);
        for(String tab:  tabs){
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }


        displayUserFace();


        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnavbar);

        //Getting the house data from database and showing on houseList tab

        getHouseData();

        searchEt = view.findViewById(R.id.searchEt);
        filtreBtn = view.findViewById(R.id.filtreBtn);

        popupMenu = new PopupMenu(getContext(), filtreBtn);
        popupMenu.getMenu().add(1,1,1,"No filter");
        popupMenu.getMenu().add(1,1,2,"Filter by Availablilty");
        popupMenu.getMenu().add(1,1,3,"Filter by Price");
        popupMenu.getMenu().add(1,1,4,"Filter by Size");
        popupMenu.getMenu().add(1,1,5,"Filter by Surface");
        popupMenu.getMenu().add(1,1,6,"Filter by Creation date");
        popupMenu.getMenu().add(1,1,7,"Filter by Updating date");

        filtreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();

            }
        });
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int order = menuItem.getOrder();
                switch (order) {
                    case 1:
                        // effectuer l'action pour supprimer le filtre
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("none");
                        }
                        break;
                    case 2:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("available");
                        }
                        break;
                    case 3:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("price");
                        }
                        break;
                    case 4:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("size");
                        }
                        break;
                    case 5:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("surface");
                        }
                        break;
                    case 6:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("lastModifiedDate");
                        }
                        break;
                    case 7:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("addedDate");
                        }
                        break;
                        


                }
                return true;
            }
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Action à effectuer lorsqu'un onglet est sélectionné

                int selectedIndex = tab.getPosition();
                //Toast.makeText(view.getContext(), "Onglet sélectionné : " + selectedIndex, Toast.LENGTH_SHORT).show();

                switch (selectedIndex) {
                    case 0:
                        // effectuer l'action pour supprimer le filtre
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.filter();
                        }
                        break;


                    case 1:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("available");
                        }
                        break;

                    case 2:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("views");
                        }
                        break;
                    case 3:
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.sortData("price");
                        }
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Action à effectuer lorsqu'un onglet est désélectionné

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Action à effectuer lorsqu'un onglet est sélectionné pour la deuxième fois
            }
        });



// Ajouter le TextWatcher
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Mettre à jour les éléments de RecyclerView en fonction de la recherche
                 //recyclerViewAdapter.getFilter().filter(s.toString());
                if (recyclerViewAdapter != null) {
                    recyclerViewAdapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


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
                            if ((boolean)doc.get("availability") == true && (boolean)doc.get("authorized")){
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

                                house.setAvailability( (boolean) doc.get("availability"));

                                house.setPhone(doc.get("phone").toString());

                                house.setOwnerEmail( doc.get("ownerEmail").toString() );

                                house.setAuthorized((boolean) doc.get("authorized") );

                                house.setViews((long) doc.get("views") );

                                house.setAddedDate( (Timestamp)  doc.get("addedDate"));

                                house.setLastModifiedDate( (Timestamp) doc.get("lastModifiedDate") );

                                if(doc.get("latitude")  != null){
                                    house.setLatitude((double)  doc.get("latitude") );
                                }
                                if(doc.get("longitude")  != null){
                                    house.setLongitude((double)  doc.get("longitude") );
                                }

                                if(doc.get("images") != null){
                                    List<String> images = (List<String>) doc.get("images");
                                    house.setImages(images);

                                }

                                houses.add(house);
                            }

                        }

                        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),houses, R.layout.houses_cardview);
                        //setting adapter to recycler view
                        my_rcv.setAdapter(recyclerViewAdapter);
                        //LayoutManager for recycler view
                        my_rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }
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