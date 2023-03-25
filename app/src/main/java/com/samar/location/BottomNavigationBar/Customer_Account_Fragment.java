package com.samar.location.BottomNavigationBar;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.samar.location.R;
import com.samar.location.authentication.LoginActivity;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.models.Customer_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Customer_Account_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Customer_Account_Fragment extends Fragment {

    TextView cName, cGenderAge ;
    ImageView cProfile;
    EditText cEmail , cAccountType , cPhone , cPassword ,cLocation , cInput_name;
    Button edit_btn , logout_btn, save_btn;
    RadioGroup radioGroup;
    LinearLayout gender_layout;
    String currentUserEmail;
    Customer_Model customerModel;
    FirebaseDB firebaseDB;
    RadioButton male , female;
    FirebaseFirestore firestore;
    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    ArrayList urlStrings;
    private ProgressDialog progressDialog ;




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Customer_Account_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Customer_Account_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Customer_Account_Fragment newInstance(String param1, String param2) {
        Customer_Account_Fragment fragment = new Customer_Account_Fragment();
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
        currentUserEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();

        View view=inflater.inflate(R.layout.fragment_customer__account_, container, false);

        cName = view.findViewById(R.id.customer_name);
        cGenderAge = view.findViewById(R.id.customer_gender);
        cProfile = view.findViewById(R.id.customer_profile_image);
        cEmail = view.findViewById(R.id.customer_email);
        cAccountType = view.findViewById(R.id.custmer_accountType);
        cPhone = view.findViewById(R.id.customer_phone);
        cPassword = view.findViewById(R.id.customer_password);
        cLocation = view.findViewById(R.id.customer_address);
        cInput_name=view.findViewById(R.id.customer_name_input);
        radioGroup=view.findViewById(R.id.customer_gender_input);
        gender_layout=view.findViewById(R.id.gender_layout);
        male=view.findViewById(R.id.male);
        female=view.findViewById(R.id.female);
        edit_btn=view.findViewById(R.id.edit_btn);
        logout_btn=view.findViewById(R.id.customer_logout_btn);

        save_btn=view.findViewById(R.id.save_btn);
        customerModel=new Customer_Model();
        firebaseDB=new FirebaseDB();
        firestore=FirebaseFirestore.getInstance();
        //Showing Data on profile

        showDataOnProfile();

        cProfile.setEnabled(false);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 save_btn.setVisibility(View.VISIBLE);
                 edit_btn.setVisibility(View.GONE);
               //  cEmail.setEnabled(true);
              //   cAccountType.setEnabled(true);
                 cPhone.setEnabled(true);
                // cPassword.setEnabled(true);
                 cLocation.setEnabled(true);
                 cInput_name.setVisibility(View.VISIBLE);
                 gender_layout.setVisibility(View.VISIBLE);
                 cProfile.setEnabled(true);


            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Here we are Updating the data from profile
              if(cInput_name.getText()!=null)
                customerModel.setName(cInput_name.getText().toString());

                RadioButton selectedRadioButton  = (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
                if(selectedRadioButton!=null)
                           customerModel.setGender(selectedRadioButton.getText().toString().toUpperCase());
                if(cPhone.getText()!=null)
                              customerModel.setPhone(cPhone.getText().toString());
                if (cLocation.getText()!=null)
                          customerModel.setAddress(cLocation.getText().toString());
                //making imageview clickbable to change profile image.

                uploadProfileToStorage();
                firebaseDB.updateProfileData(FirebaseAuth.getInstance().getCurrentUser().getEmail(),customerModel);

                // showDataOnProfile();

                //  cEmail.setEnabled(false);
                cPhone.setEnabled(false);
                // cPassword.setEnabled(false);
                cLocation.setEnabled(false);

                cProfile.setEnabled(false);
                cInput_name.setVisibility(View.GONE);
                gender_layout.setVisibility(View.GONE);

                edit_btn.setVisibility(View.VISIBLE);
                save_btn.setVisibility(View.GONE);


            }
        });

        cProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This will open the file explorer
                getPickImageIntent();

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                Toast.makeText(getActivity(), "Logging Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });


        return view;
    }

    private void uploadProfileToStorage() {

        if(!ImageList.isEmpty()){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading Please Wait");
            urlStrings = new ArrayList<>();
            progressDialog.show();
            StorageReference imageFolder = FirebaseStorage.getInstance()
                    .getReference().child("ImageFolder");
            for (int upload_count = 0; upload_count < ImageList.size(); upload_count++) {

                Uri individualImage = (Uri) ImageList.get(upload_count);
                final StorageReference imageName = imageFolder.child("Images"+individualImage.getLastPathSegment());

                imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlStrings.add(String.valueOf(uri));
                                if(urlStrings.size()==ImageList.size())
                                {
                                    storeLinksInFiretore(urlStrings);
                                }
                            }
                        });
                    }
                });
            }

        }

    }

    private void storeLinksInFiretore(ArrayList urlStrings) {
        Log.d("xxxx", "storeLinksInFiretore: "+urlStrings);

       /* HashMap<String,String> hashMap = new HashMap<>();
        for (int i = 0; i <urlStrings.size() ; i++) {
            hashMap.put("ImgLink"+i, urlStrings.get(i).toString());
        }*/

       /* firestore.collection("images")
                .add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(Task<DocumentReference> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getActivity(), "FileUploadedSuccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

*/            firestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference=firestore.collection("USERDATA").document(currentUserEmail);

        documentReference.update("profileUrl",urlStrings.get(0));
        progressDialog.dismiss();
        ImageList.clear();
    }


    private void showDataOnProfile() {


         FirebaseFirestore   firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection("USERDATA").document(currentUserEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getId());
                        Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getData());
                        Map<String, Object> snapshot= task.getResult().getData();
                        try {
                            if(snapshot.get("name")!=null){
                                   cName.setText(snapshot.get("name").toString());
                                 cInput_name.setText(snapshot.get("name").toString());}

                            if(snapshot.get("gender")!=null)
                            {cGenderAge.setText(snapshot.get("gender").toString()+" ");
                              if(snapshot.get("gender").toString().equals("FEMALE"))
                                  female.setChecked(true);
                              else
                                  male.setChecked(true);
                            }


                            if(snapshot.get("age")!=null)
                                cGenderAge.append(snapshot.get("name").toString());

                            if(snapshot.get("address")!=null)
                                cLocation.append(snapshot.get("address").toString());

                            if(snapshot.get("profileUrl")!=null)
                                   setProfile(snapshot.get("profileUrl").toString());


                            cEmail.setText(snapshot.get("email").toString());
                            cPassword.setText(snapshot.get("password").toString());
                            cPhone.setText(snapshot.get("phone").toString());
                        }catch (Exception e)
                        {
                            Log.d("xxxxxxx", "onComplete Exception in setting data to profile : "+e.getLocalizedMessage());
                        }


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.d("xxxx", "onFailure: " + e.getLocalizedMessage());
                }
            });
        }




    private void setProfile(String profileUrl) {
        Glide.with(getActivity())
                .load(profileUrl)
                .into(cProfile);
    }


    public void getPickImageIntent(){
        Intent GalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(GalleryIntent, 42);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==42) {
            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(data.getData(), filePathColumn, null, null, null);

            String imagePath;
            Uri imageUri;
            if (cursor == null) {
                imageUri= data.getData();
                imagePath = data.getData().getPath();
                ImageList.add(imageUri);
                setProfile(ImageList.get(0).toString());
            }
            else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                imagePath = cursor.getString(idx);
                imageUri= data.getData();
                ImageList.add(imageUri);
                setProfile(ImageList.get(0).toString());
                cursor.close();
            }
            //Transformer la photo en Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //Afficher le Bitmap
            cProfile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
        }
    }


}