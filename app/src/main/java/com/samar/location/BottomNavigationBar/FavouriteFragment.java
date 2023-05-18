package com.samar.location.BottomNavigationBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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
import com.samar.location.models.Tenant;
import com.samar.location.owner_requestlist.Customer_Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    Customer_Adapter adapter;
    FirebaseFirestore firebaseFirestore;
    String ownerId;
    Tenant tenant;
    String houseNo;
    List<String> houseNos;
    List<Tenant> tenantList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.owner_requestlist1, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);


        //get house Data and pass into adapter so that it can be showed in the notification fragment
        getHouseDataOfCurrentOwner();


        return view;


    }

    private void getHouseDataOfCurrentOwner() {

        try {
            ownerId = FirebaseAuth.getInstance().getUid();
            Log.d("xxxxxOwnerid", "getHouseDataOfCurrentOwner: ownerId " + ownerId);
            firebaseFirestore = FirebaseFirestore.getInstance();
            tenant = new Tenant();
            tenantList = new ArrayList<>();
            houseNos = new ArrayList<>();
            firebaseFirestore.collection("HouseCollection").whereEqualTo("ownerUid", ownerId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                                //Log.d("xxxtaskdoc", "onComplete: of getHouseOfCurrentOwner "+task.getResult().getDocuments());
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    if (doc.get("tenant") != null) {
                                        Map<String, String> map = (Map<String, String>) doc.get("tenant");

                                        houseNo = doc.get("houseNo").toString();
                                        tenant.setName(map.get("name"));
                                        tenant.setPhone(map.get("phone"));
                                        tenant.setEmail(map.get("email"));
                                        tenant.setShiftingDate(map.get("shiftingDate"));
                                        tenant.setAmtPaid(map.get("amtPaid"));
                                        tenant.setPaymentDate(map.get("paymentDate"));
                                        // Log.d("xxxtaskdoc", "onComplete: of getHouseOfCurrentOwner "+tenant);
                                        houseNos.add(houseNo);
                                        tenantList.add(tenant);
                                    }


                                }
                            Log.d("xxxtaskdoc", "onComplete: of getHouseOfCurrentOwner " + tenantList.toString() + "" + houseNos.toString());
                            adapter = new Customer_Adapter(getActivity(), tenantList, houseNos);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.d("xxxxexception", "onFailure: of getHouseOfCurrentOwner " + e.getLocalizedMessage());
                        }
                    });


        } catch (Exception e) {
            Log.d("xxxx", "getHouseDataOfCurrentOwner: " + e.getLocalizedMessage());
        }


    }
}