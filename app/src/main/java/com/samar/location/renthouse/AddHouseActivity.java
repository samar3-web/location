package com.samar.location.renthouse;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.samar.location.BottomNavigationBar.BootomNavBarMain;
import com.samar.location.R;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.models.House;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddHouseActivity extends AppCompatActivity {


    EditText contactPersonName, phone, houseNo, street, city, post, location, rentPrice;
    Button addImagesBtn, saveHouseBtn;
    Spinner houseSize, available;
    RecyclerView addHouse_rcv;
    AddHouseAdapter addHouseAdapter;
    List selectedImages;
    House house;
    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    ArrayList urlStrings;
    private ProgressDialog progressDialog;
    String documentUid;
    Intent intent;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addhouse_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        contactPersonName = findViewById(R.id.contact_person_name);
        phone = findViewById(R.id.owner_phone);
        houseNo = findViewById(R.id.housenumber);
        street = findViewById(R.id.housestreet);
        city = findViewById(R.id.housecity);
        post = findViewById(R.id.postnumber);
        location = findViewById(R.id.house_location);
        rentPrice = findViewById(R.id.rentprice);
        addImagesBtn = findViewById(R.id.addImagesBtn);
        saveHouseBtn = findViewById(R.id.house_save_btn);
        houseSize = findViewById(R.id.housesizespinner);
        available = findViewById(R.id.houseavailalespinner);
        addHouse_rcv = findViewById(R.id.houseiamge_rcv);

        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        //String ownerUid = firebaseAuth.getCurrentUser().getUid();


        house = new House();
        //setting item in size spinner
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(AddHouseActivity.this
                , R.array.size_array, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houseSize.setAdapter(sizeAdapter);

        //setting item in available adapter
        ArrayAdapter<CharSequence> availableAdapter = ArrayAdapter.createFromResource(AddHouseActivity.this
                , R.array.available_array, android.R.layout.simple_spinner_item);
        availableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        available.setAdapter(availableAdapter);

        //Setting house image adapter and recyclerview
        //selectedImages = new ArrayList<>();


        addImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPickImageIntent();

            }
        });


        saveHouseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID uuid = UUID.randomUUID();
                documentUid = uuid.toString();
               /* UUID sameUuid = UUID.fromString(uuidAsString);
               sameUuid.equals(uuid);*/
               // house.setOwnerUid(ownerUid);
                house.setOwnerEmail(userEmail);

               if(validator(contactPersonName.getText().toString(), phone.getText().toString(),houseNo.getText().toString(),street.getText().toString(),city.getText().toString(),post.getText().toString(),location.getText().toString(),
                rentPrice.getText().toString()))
               {

                   house.setContactPerson(contactPersonName.getText().toString());
                   house.setPhone(phone.getText().toString() );
                   house.setHouseNo(houseNo.getText().toString());
                   house.setStreet(street.getText().toString());
                   house.setCity(city.getText().toString());
                   house.setPost(post.getText().toString());
                   house.setLocation(location.getText().toString());
                   house.setPrice(rentPrice.getText().toString());
                   house.setSize(houseSize.getSelectedItem().toString());

                   if (available.getSelectedItem().toString().equals("YES"))
                       house.setAvailability(true);
                   else
                       house.setAvailability(false);
               }
               //uploading images to storage
                uploadImagesToStorage();
                Log.d("xxxxxx", "onClick: of savehouse button " + house.toString());


            }
        });


    }

    private boolean phoneValidator(String number) {
        if (number.isEmpty()) {
            phone.setError("Cannot be Empty");
            return false;
        }
        if (number.length() < 8) {
            phone.setError("Must be 8 Characters in Minumum");
            return false;
        }

        return true;
    }

    private boolean validator(String name,String phoneN, String houseno, String street_s, String city_s, String post_s, String location_s, String price_s) {
        if (name.isEmpty()) {
            contactPersonName.setError("Cannot be empty");
            return false;
        }

        if (!phoneValidator(phoneN))
            return false;

        if (houseno.isEmpty()) {
            houseNo.setError("Cannot be empty");
            return false;
        }
        if (street_s.isEmpty()) {
            street.setError("Cannot be empty");
            return false;
        }
        if (city_s.isEmpty()) {
            city.setError("Cannot be empty");
            return false;
        }
        if (post_s.isEmpty()) {
            post.setError("Cannot be empty");
            return false;
        }
        if (location_s.isEmpty()) {
            location.setError("Cannot be empty");
            return false;
        }
        if (price_s.isEmpty()) {
            rentPrice.setError("Cannot be empty");
            return false;
        }
            if (ImageList.isEmpty())
            {
                Toast.makeText(this, "You must select images", Toast.LENGTH_SHORT).show();
            }
        return true;
    }


    private void uploadImagesToStorage() {
        if (!ImageList.isEmpty()) {
            progressDialog = new ProgressDialog(AddHouseActivity.this);
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
    }

    private void storeLinksInFiretore(ArrayList<String> urlStrings) {

        String[] images =  urlStrings.toArray(new String[ urlStrings.size() ]);

        house.setImages(urlStrings);

        FirebaseDB firebaseDB = new FirebaseDB();
        firebaseDB.saveHouseData(documentUid, house, AddHouseActivity.this, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        progressDialog.dismiss();
        ImageList.clear();
        finish();
        startActivity(new Intent(AddHouseActivity.this, BootomNavBarMain.class));
    }


    public void getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 42);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 42) {
                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;

                    ImageList.clear();
                    while (currentImageSlect < countClipData) {

                        ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSlect = currentImageSlect + 1;
                    }

                    Toast.makeText(AddHouseActivity.this, "You have Selected " + ImageList.size(), Toast.LENGTH_SHORT).show();
                    addHouseAdapter = new AddHouseAdapter(AddHouseActivity.this, ImageList);
                    addHouse_rcv.setAdapter(addHouseAdapter);
                    addHouse_rcv.setLayoutManager(new GridLayoutManager(AddHouseActivity.this, 2));

            }
        }
    }
}
