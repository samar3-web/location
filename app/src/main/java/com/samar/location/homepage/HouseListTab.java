package com.samar.location.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.samar.location.authentication.LoginActivity;
import com.samar.location.models.House;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.samar.location.R;
import com.samar.location.models.House;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseListTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseListTab extends Fragment {

    //Global variables
    RecyclerView my_rcv;
    RecyclerViewAdapter recyclerViewAdapter;
     List<House> houses;
     public Bundle bundle;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText searchEt;
    private ImageView filtreBtn;

    public HouseListTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseListTab.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseListTab newInstance(String param1, String param2) {
        HouseListTab fragment = new HouseListTab();
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
                    searchEt = view.findViewById(R.id.searchEt);


                   BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomnavbar);

                   //Getting the house data from database and showing on houseList tab

                   getHouseData();


                   filtreBtn = view.findViewById(R.id.filtreBtn);

                   filtreBtn.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Toast.makeText(getContext(),searchEt.getText().toString(),Toast.LENGTH_SHORT).show();
                           Log.d("aaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaaaaaaaa");
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
               // recyclerViewAdapter.getFilter().filter(s.toString());
                Toast.makeText(getContext(),s.toString(),Toast.LENGTH_SHORT).show();
                Log.d("EditText :     ",s.toString());
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

                               // house.setOwnerUid(doc.get("ownerUid").toString());

                                house.setAvailability( (boolean) doc.get("availability"));
                                house.setPhone(doc.get("phone").toString());

                                house.setAdditionDate( doc.get("additionDate").toString() );
                                house.setAdditionDate(  doc.get("lastModifiedDate").toString() );
                                if(doc.get("images") != null){
                                    house.setImages( (List<String>) doc.get("images") );
                                }




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

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
       //  MenuInflater inflater= getActivity().getMenuInflater();
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.option_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.option_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                Log.d("xxxxxsearch", "onQueryTextChange: " + text);
                 if(recyclerViewAdapter!=null)
                     recyclerViewAdapter.getFilter().filter(text);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int option_id = item.getItemId();

        switch (option_id) {

            case R.id.option_logout:
                logoutcurrentUser();
                return true;
            default:
                break;

        }
        return false;
    }

    private void logoutcurrentUser() {
        FirebaseAuth.getInstance().signOut();
         getActivity().finish();
        Toast.makeText(getActivity(), "Logging Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), LoginActivity.class));


    }
}