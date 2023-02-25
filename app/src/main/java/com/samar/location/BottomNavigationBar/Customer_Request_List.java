package com.samar.location.BottomNavigationBar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.samar.location.R;
import com.samar.location.models.CustomGalleryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Customer_Request_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Customer_Request_List extends Fragment {

    TextView house_city , house_address;
    Button call_owner , house_size , house_price;
    Gallery gallery;
    ImageView house_Images;
    CustomGalleryAdapter cga;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Customer_Request_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Customer_Request_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Customer_Request_List newInstance(String param1, String param2) {
        Customer_Request_List fragment = new Customer_Request_List();
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
        View view = inflater.inflate(R.layout.fragment_customer__request__list, container, false);

        house_city=view.findViewById(R.id.house_city);
        house_address=view.findViewById(R.id.house_address);
        house_size=view.findViewById(R.id.house_size);
        house_price=view.findViewById(R.id.house_price);
        call_owner=view.findViewById(R.id.call_owner);
         house_Images=view.findViewById(R.id.house_Images);
         gallery=view.findViewById(R.id.simple_Gallery);
        String currentUserEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();
       DocumentReference documentReference= FirebaseFirestore.getInstance().collection("USERDATA").document(currentUserEmail);
       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(Task<DocumentSnapshot> task) {
               if(task.isSuccessful())
               {
                   Log.d("xxxxcustomer", "onComplete: customer notification "+task.getResult().getData().toString());
                   Map<String, Object> snapshot = (Map<String, Object>) task.getResult().get("house");
                   if(snapshot!=null) {
                       house_size.setText(snapshot.get("size").toString());
                       house_price.setText((snapshot.get("price").toString()));
                       house_city.setText(snapshot.get("city").toString() + ", India");
                       house_address.setText("Contact Name : " + snapshot.get("contactPerson").toString() + "\n" + "HouseNo : " + snapshot.get("houseNo").toString() + "\n" + "Street : " + snapshot.get("street").toString() + "\n" + "Post : " + snapshot.get("post").toString());
                       call_owner.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String phone = "tel:" + snapshot.get("phone").toString();
                               Intent intent = new Intent(Intent.ACTION_CALL);
                               intent.setData(Uri.parse(phone));
                               startActivity(intent);
                           }
                       });
                       List images=new ArrayList();
                       images.add(snapshot.get("image1").toString());
                       images.add(snapshot.get("image2").toString());
                       images.add(snapshot.get("image3").toString());
                       images.add(snapshot.get("image4").toString());
                       images.add(snapshot.get("image5").toString());

                       Glide.with(getActivity()).load(snapshot.get("image1").toString()).into(house_Images);
                       cga=new CustomGalleryAdapter(getActivity(),images);
                       gallery.setAdapter(cga);
                       gallery.setSpacing(5);
                       gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               Glide.with(getActivity()).load(images.get(position)).into(house_Images );

                           }
                       });
                   }
                   else{
                       view.setVisibility(View.GONE);
                   }

               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(Exception e) {
               Log.d("TAG", "onFailure: "+e);
           }
       });



        return view;
    }
}