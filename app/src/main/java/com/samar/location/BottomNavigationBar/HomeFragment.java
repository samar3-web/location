package com.samar.location.BottomNavigationBar;


import static com.razorpay.AppSignatureHelper.TAG;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.samar.location.R;
import com.samar.location.homepage.RecyclerViewAdapter;
import com.samar.location.models.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String[] tabs = {"All", "Tendance", "More View", "Luxe"};
    public Bundle bundle;
    //Global variables
    RecyclerView my_rcv;
    ImageView userFace;
    RecyclerViewAdapter recyclerViewAdapter;

    /*

    TabLayout home_tabs;
    ViewPager2 home_viewpager;
    HomeTabs_Adpater homeTabs_adpater;

     */
    List<House> houses;
    TabLayout tabLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView filtreBtn;
    private TextView searchEt;
    private PopupMenu popupMenu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private String token;
    private TabLayout.OnTabSelectedListener tabSelectedListener;
    private TextWatcher textWatcher;

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

        //ajouter les tokens de l'utilsateur de tous les apps installées utilsant son login
        setUserTokens();

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
        for (String tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }


        displayUserFace();


        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnavbar);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        /*shimmerFrameLayout.startShimmer();
        // Simulating data retrieval
        // Replace this with your actual data retrieval logic
       // shimmerFrameLayout.stopShimmer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the shimmer container and show the content views
                shimmerFrameLayout.setVisibility(View.GONE);
                // Make your actual content views visible
                my_rcv.setVisibility(View.VISIBLE);
                // Stop the shimmer effect
                shimmerFrameLayout.stopShimmer();
               // getHouseData();

            }
        }, 2000);*/ // Delay of 2000 milliseconds (2 seconds)

        //Getting the house data from database and showing on houseList tab

        getHouseData();

        searchEt = view.findViewById(R.id.searchEt);
        filtreBtn = view.findViewById(R.id.filtreBtn);

        popupMenu = new PopupMenu(getContext(), filtreBtn);
        popupMenu.getMenu().add(1, 1, 1, "No filter");
        popupMenu.getMenu().add(1, 1, 2, "Filter by Availablilty");
        popupMenu.getMenu().add(1, 1, 3, "Filter by Price");
        popupMenu.getMenu().add(1, 1, 4, "Filter by Size");
        popupMenu.getMenu().add(1, 1, 5, "Filter by Surface");
        popupMenu.getMenu().add(1, 1, 6, "Filter by Creation date");
        popupMenu.getMenu().add(1, 1, 7, "Filter by Updating date");

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

        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
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
        };
        tabLayout.addOnTabSelectedListener(tabSelectedListener);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Mettre à jour les éléments de RecyclerView en fonction de la recherche
                // recyclerViewAdapter.getFilter().filter(s.toString());
                if (recyclerViewAdapter != null) {
                    recyclerViewAdapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
// Ajouter le TextWatcher
        searchEt.addTextChangedListener(textWatcher);


        my_rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                       /*
                           super.onScrollStateChanged(recyclerView, newState);
                           if (newState == RecyclerView.SCROLL_STATE_IDLE)
                           {

                               bottomNavigationView.setVisibility(View.VISIBLE);



                           }

                        */

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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

    /*shimmerFrameLayout.startShimmer();
           // Simulating data retrieval
           // Replace this with your actual data retrieval logic
          // shimmerFrameLayout.stopShimmer();
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   // Hide the shimmer container and show the content views
                   shimmerFrameLayout.setVisibility(View.GONE);
                   // Make your actual content views visible
                   my_rcv.setVisibility(View.VISIBLE);
                   // Stop the shimmer effect
                   shimmerFrameLayout.stopShimmer();
                  // getHouseData();

               }
           }, 2000);*/
    private void getHouseData() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        my_rcv.setVisibility(View.GONE);
        shimmerFrameLayout.startShimmer();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("HouseCollection").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        my_rcv.setVisibility(View.VISIBLE);
                        Log.d("xxxxxdocs", "onComplete: of HouseData fetching " + task.getResult().getDocuments());
                        houses = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            if ((boolean) doc.get("availability") && (boolean) doc.get("authorized")) {
                                House house = new House();
                                house.setDocId(doc.getId());
                                house.setLocation(doc.get("location").toString());
                                house.setSize(doc.get("size").toString());
                                house.setPrice((doc.get("price")).toString());
                                house.setCity(doc.get("city").toString());
                                house.setContactPerson(doc.get("contactPerson").toString());
                                house.setHouseNo(doc.get("houseNo").toString());
                                house.setStreet(doc.get("street").toString());
                                house.setPost(doc.get("post").toString());

                                house.setAvailability((boolean) doc.get("availability"));

                                house.setPhone(doc.get("phone").toString());

                                house.setOwnerEmail(doc.get("ownerEmail").toString());

                                house.setAuthorized((boolean) doc.get("authorized"));

                                house.setViews((long) doc.get("views"));

                                house.setAddedDate((Timestamp) doc.get("addedDate"));

                                house.setLastModifiedDate((Timestamp) doc.get("lastModifiedDate"));

                                if (doc.get("latitude") != null) {
                                    house.setLatitude((double) doc.get("latitude"));
                                }
                                if (doc.get("longitude") != null) {
                                    house.setLongitude((double) doc.get("longitude"));
                                }

                                if (doc.get("images") != null) {
                                    List<String> images = (List<String>) doc.get("images");
                                    house.setImages(images);

                                }

                                houses.add(house);
                            }

                        }
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter = null;
                            my_rcv.setAdapter(null);
                        }

                        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), houses, R.layout.houses_cardview);
                        //setting adapter to recycler view
                        my_rcv.setAdapter(recyclerViewAdapter);
                        //LayoutManager for recycler view
                        my_rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("xxxxx", "onFailure: of HouseData fectching " + e.getLocalizedMessage());
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

    private void setUserTokens() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        Log.d("ffffffffffffffffffffffffffff", token);


                    }
                });

        /*FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                token = task.getResult().getToken();
                Log.d("nnnnnnnnnnnnnnn", "onComplete: myToken "+token);
            }
        });*/

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = firestore.collection("USERDATA").document(currentUserEmail);

        // Requête pour récupérer les données de l'utilisateur actuel
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Récupérer les valeurs actuelles de requests
                    List<String> tokens = (List<String>) documentSnapshot.get("tokens");

                    if (tokens != null && tokens.contains(token)) {
                        // docId existe déjà dans tokens, le supprimer

                        Log.d("nnnnnnnnnnnnnnn", "onComplete: myToken existe deja");
                        /*documentReference.update("tokens", FieldValue.arrayRemove(docId))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }
                                });*/
                    } else {
                        // docId n'existe pas dans tokens, l'ajouter
                        documentReference.update("tokens", FieldValue.arrayUnion(token))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("nnnnnnnnnnnnnnn", "onComplete: myToken est ajouté");
                                    }
                                });
                    }
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        my_rcv.clearOnScrollListeners();
        // Release the ShimmerFrameLayout
        shimmerFrameLayout = null;
        swipeRefreshLayout = null;
        my_rcv = null;
        // Remove the listener
        tabLayout.removeOnTabSelectedListener(tabSelectedListener);

        // Clear the reference to the listener
        tabSelectedListener = null;



        // Remove TabLayout from its parent view
        ViewGroup parent = (ViewGroup) tabLayout.getParent();
        if (parent != null) {
            parent.removeView(tabLayout);
        }

        // Clear reference to TabLayout
        tabLayout = null;
        // Clean up references to avoid memory leaks
        filtreBtn.setOnClickListener(null);
        popupMenu.setOnMenuItemClickListener(null);

        searchEt.removeTextChangedListener(textWatcher);

    }

}