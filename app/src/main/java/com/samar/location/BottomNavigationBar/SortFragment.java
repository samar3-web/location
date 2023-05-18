package com.samar.location.BottomNavigationBar;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.samar.location.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SortFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //Creating firebasefirestore global variable
    FirebaseFirestore firestore;
    Button select_btn, upload_btn, get_btn;
    ImageView show_imageview;
    ArrayList ImageList = new ArrayList();
    ArrayList urlStrings;
    private Uri ImageUri;
    private ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SortFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SortFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SortFragment newInstance(String param1, String param2) {
        SortFragment fragment = new SortFragment();
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
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        //creating an instance of firestore
        firestore = FirebaseFirestore.getInstance();

        select_btn = view.findViewById(R.id.select_images);
        upload_btn = view.findViewById(R.id.upload_images);
        get_btn = view.findViewById(R.id.get_images);

        show_imageview = view.findViewById(R.id.show_imageview);
        Map<String, Object> user = new HashMap<>();

        /*user.put("Name","Gourav Patel");
        user.put("AccountType","Customer");
        user.put("Email","gourav@gmail.com");
        user.put("Password","password");
        user.put("phone","1234567890");
        user.put("Merge","merged into doc");*/

       /*   Adding document with unique_id
            firestore.collection("SignupDetails")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Data Saved Successfully "+documentReference.getId(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Toast.makeText(getActivity(), "Transaction failed "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/

        /*//Adding documnet with a specific name

        firestore.collection("signupDetails").document("customer_email").set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Data successfully added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

       /* //Mergiing Another key:value pair or object on already present document.
        firestore.collection("signupDetails").document("customer_email").set(user, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Megre successfully done", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        /*  //Updating the key:value pair in existing document
          DocumentReference documentReference=firestore.collection("signupDetails").document("customer_email");
          documentReference.update("phone","xxxxxxxxx");
*/
        ///////-----------------------------Reading the Data-----------------------------------------------------------------------------------------------
        //Getting the current logged in user email
        String email_current = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.d("email_current", "onComplete: " + email_current);
      /*
        //Reading a particular document by directly referencing to it .
        DocumentReference documentReference =firestore.collection("signupDetails").document("customer_email");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists())
                    {
                        Log.d("snapshot", "onComplete: "+snapshot.getData().toString());
                    }
                    else
                    {
                        Log.d("snapshot", "onComplete: NO DATA");
                    }
                }
            }
        });*/

       /* //Using Where query on collection
        firestore.collection("signupDetails").whereEqualTo("Email","ggourav@gmail.com").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot doc:task.getResult())
                            {
                                Log.d("snapshot", "onComplete: "+doc.getData());
                            }
                        }
                        else{
                            String msg=task.getException().getLocalizedMessage();
                            Log.d("snapshot", "onFailure: "+msg);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("snapshot", "onFailure: "+e.getLocalizedMessage());
            }
        });*/

        //Uploading Images --Getting input images from user and storinig it storage and add it to firestore document.
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPickImageIntent();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading Please Wait");
                urlStrings = new ArrayList<>();
                progressDialog.show();
                StorageReference imageFolder = FirebaseStorage.getInstance()
                        .getReference().child("ImageFolder");

                for (int upload_count = 0; upload_count < ImageList.size(); upload_count++) {

                    Uri individualImage = (Uri) ImageList.get(upload_count);
                    final StorageReference imageName = imageFolder.child("Images" + individualImage.getLastPathSegment());

                    imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlStrings.add(String.valueOf(uri));
                                    if (urlStrings.size() == ImageList.size()) {
                                        storeLinksInFiretore(urlStrings);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });


        //showing images on image view....

        get_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firestore.collection("images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Object[] obj = documentSnapshot.getData().values().toArray();
                                Log.d("xxxxxxx", "onComplete: " + obj[1]);

                                Glide.with(getActivity())
                                        .load(obj[1].toString())
                                        .into(show_imageview);
                            }
                        }
                    }
                });
            }
        });


        return view;
    }

    private void storeLinksInFiretore(ArrayList urlStrings) {
        Log.d("urlstrings", "storeLinksInFiretore: " + urlStrings);

        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < urlStrings.size(); i++) {
            hashMap.put("ImgLink" + i, urlStrings.get(i).toString());
        }

        firestore.collection("images")
                .add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "FileUploadedSuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


        progressDialog.dismiss();
        ImageList.clear();
    }

    public void getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 42);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 42) {
                if (data.getClipData() != null) {

                    ClipData mClipData = data.getClipData();
                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;
                   /* for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        // display your images
                        show_imageview.setImageURI(uri);
                    }*/
                    while (currentImageSlect < countClipData) {

                        ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSlect = currentImageSlect + 1;
                    }
                    Toast.makeText(getActivity(), "You have Selected " + ImageList.size(), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getActivity(), "Please Select multiple Images", Toast.LENGTH_SHORT).show();

                  /*else if (data.getData() != null) {
                    Uri uri = data.getData();
                    // display your image
                    show_imageview.setImageURI(uri);*/

                }
            }
        }
    }
}