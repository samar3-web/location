package com.samar.location;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
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
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.models.Customer_Model;
import com.samar.location.models.House;
import com.samar.location.models.Owner_Model;
import com.samar.location.privateSpace.UserSpaceActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewHouseUserDetailsActivity extends AppCompatActivity {

    TextView  availability;
    ImageView face;
    EditText price, street, phone, size,houseNo, city,location;
    Button edit_btn, save_btn,delete_btn;
    RadioGroup radioGroup;
    LinearLayout availability_layout;
    String currentUserEmail;

    FirebaseDB firebaseDB;
    RadioButton valable, invalable;
    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    ArrayList urlStrings;

    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    FirebaseFirestore firestore;

   public static String houseDocId;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house_user_details);


        houseDocId =  getIntent().getStringExtra("houseDocId");

        face = findViewById(R.id.face);
        availability = findViewById(R.id.house_availability);
        price = findViewById(R.id.price);
        phone = findViewById(R.id.owner_phone);
        street = findViewById(R.id.street);
        location = findViewById(R.id.location);
        city = findViewById(R.id.city);
        size = findViewById(R.id.size);
        houseNo = findViewById(R.id.houseNo);
        radioGroup = findViewById(R.id.house_availability_input);
        availability_layout = findViewById(R.id.availability_layout);
        valable = findViewById(R.id.valable);
        invalable = findViewById(R.id.invalable);
        edit_btn = findViewById(R.id.edit_btn);

        save_btn = findViewById(R.id.save_btn);

        delete_btn = findViewById(R.id.delete_btn);

        recyclerView = findViewById(R.id.recyclerViewHorizontal);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        House  myHouse = new House();

        firebaseDB = new FirebaseDB();
        firestore = FirebaseFirestore.getInstance();

        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        //showing data details
        showHouseDetails(houseDocId);






        //------------------------edit Button code
       // cProfile.setEnabled(false);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_btn.setVisibility(View.VISIBLE);
                edit_btn.setVisibility(View.GONE);

                price.setEnabled(true);
                phone.setEnabled(true);
                city.setEnabled(true);
                location.setEnabled(true);
                street.setEnabled(true);
                size.setEnabled(true);
                houseNo.setEnabled(true);

                availability_layout.setVisibility(View.VISIBLE);
            }
        });
        //------------------------------------save button code
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Here we are Updating the data from House

                RadioButton selectedRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                if(selectedRadioButton!=null && selectedRadioButton==invalable)
                    myHouse.setAvailability(false);
                else
                    myHouse.setAvailability(true);

                if(price.getText()!=null)
                    myHouse.setPrice(price.getText().toString());

                if(phone.getText()!=null)
                    myHouse.setPhone(phone.getText().toString());

                if(city.getText()!=null)
                    myHouse.setCity(city.getText().toString());

                if(location.getText()!=null)
                    myHouse.setLocation(location.getText().toString());

                if(street.getText()!=null)
                    myHouse.setStreet(street.getText().toString());

                if(size.getText()!=null)
                    myHouse.setSize(size.getText().toString());

                if(houseNo.getText()!=null)
                    myHouse.setHouseNo(houseNo.getText().toString());

                //making imageview clickbable to change profile image.

               // uploadDataHouseToStorage();

                firebaseDB.updateHouseData(houseDocId,myHouse,getApplicationContext());

                showHouseDetails(houseDocId);

                //User Interface

                save_btn.setVisibility(View.GONE);
                edit_btn.setVisibility(View.VISIBLE);

                price.setEnabled(false);
                phone.setEnabled(false);
                city.setEnabled(false);
                location.setEnabled(false);
                street.setEnabled(false);
                size.setEnabled(false);
                houseNo.setEnabled(false);

                availability_layout.setVisibility(View.GONE);


            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("You are sure you want to delete this House?")
                        .setCancelable(false)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                firebaseDB.deleteHouse(houseDocId, v.getContext());


                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


    }






    private void showHouseDetails(String houseDocId) {


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("HouseCollection").document(houseDocId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getId());
                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getData());
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {

                        if (snapshot.get("availability") != null) {
                            availability.setText( snapshot.get("availability").toString().equals("true") ? "Available": "Not Available");
                            if (snapshot.get("availability").toString().equals("true"))
                                valable.setChecked(true);
                            else
                                invalable.setChecked(true);
                        }


                        if (snapshot.get("price") != null)
                            price.setText(snapshot.get("price").toString());

                        if (snapshot.get("phone") != null)
                            phone.setText(snapshot.get("phone").toString());

                        if (snapshot.get("city") != null)
                            city.setText(snapshot.get("city").toString());
                        if (snapshot.get("location") != null)
                            location.setText(snapshot.get("location").toString());

                        if (snapshot.get("street") != null)
                            street.setText(snapshot.get("street").toString());

                        if (snapshot.get("size") != null)
                            size.setText(snapshot.get("size").toString());

                        if (snapshot.get("houseNo") != null)
                            houseNo.setText(snapshot.get("houseNo").toString());



                        //Aficher une Gallery des images

                        if(snapshot.get("images") != null){

                            List<String> images = (List<String>) snapshot.get("images");
                            Glide.with(getApplicationContext())
                                    .load( images.get(0) )
                                    .into(face);
                            PhotoViewAttacher pAttacher;
                            pAttacher = new PhotoViewAttacher(face);
                            pAttacher.update();
                            RecyclerViewHorizontalAdapter adapter = new RecyclerViewHorizontalAdapter(images,face );
                            recyclerView.setAdapter(adapter);

                        }

                    } catch (Exception e) {
                        Log.d("xxxxxxx", "onComplete Exception in setting data to house : " + e.getLocalizedMessage());
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


    private void setProfile(String url) {
        Glide.with(getApplicationContext())
                .load(url)
                .into(face);
    }


    private void uploadDataHouseToStorage() {

        if (!ImageList.isEmpty()) {
            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("Updating House Data Please Wait");
            urlStrings = new ArrayList<>();
            progressDialog.show();


            StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");

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


            storeLinksInFiretore(urlStrings);

        }

    }

    private void storeLinksInFiretore(ArrayList urlStrings) {
        Log.d("xxxx", "storeLinksInFiretore: " + urlStrings);

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

*/
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("HouseCollection").document(houseDocId);

        documentReference.update("profileUrl", urlStrings.get(0));
        progressDialog.dismiss();
        ImageList.clear();
    }

    public void getPickImageIntent() {
        Intent GalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("image/");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(GalleryIntent, 42);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==42) {
            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor = getApplicationContext().getContentResolver().query(data.getData(), filePathColumn, null, null, null);

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
            face.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
        }
    }


}
