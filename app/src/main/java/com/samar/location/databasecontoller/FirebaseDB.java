package com.samar.location.databasecontoller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

//import com.example.renthouse.BottomNavigationBar.BootomNavBarMain;
import androidx.annotation.NonNull;

import com.samar.location.models.Customer_Model;
import com.samar.location.models.House;
import com.samar.location.models.Owner_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.privateSpace.UserSpaceActivity;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDB {

    FirebaseFirestore firebaseFirestore;
    boolean dataAdded = false;

    String collection = "USERDATA", document = null;


   /* public FirebaseDB() {
       currentUserEmail =FirebaseAuth.getInstance().getCurrentUser().getEmail();

    }*/

    //--------------------------------------------add or setdata data to firebase firestore-------------------------------------------------------------

    public boolean setData(FirebaseFirestore firebaseFirestore, Customer_Model customerModel, String userID) {

      /*  Log.d("xxxxx", "setData: "+customerModel.getAccountType());
        if(customerModel.getAccountType().equals("CUSTOMER"))
        {
            collection="CUSTOMER";
            document=customerModel.getEmail();
        }else if(customerModel.getAccountType().equals("OWNER")){
            collection="OWNER";
            document=customerModel.getEmail();
        }*/
        document = customerModel.getEmail();

        DocumentReference documentReference = firebaseFirestore
                .collection(collection)
                .document(document);
        documentReference.set(customerModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("xxxx", "onSuccess: " + document);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("xxxx", "onFailure: " + e.getLocalizedMessage());
            }
        });

        return dataAdded;
    }


    //Overloading the same method for owner model


    public boolean setData(FirebaseFirestore firebaseFirestore, Owner_Model ownerModel, String userID) {

      /*  Log.d("xxxxx", "setData: "+customerModel.getAccountType());
        if(customerModel.getAccountType().equals("CUSTOMER"))
        {
            collection="CUSTOMER";
            document=customerModel.getEmail();
        }else if(customerModel.getAccountType().equals("OWNER")){
            collection="OWNER";
            document=customerModel.getEmail();
        }*/
        document = ownerModel.getEmail();

        DocumentReference documentReference = firebaseFirestore
                .collection(collection)
                .document(document);
        documentReference.set(ownerModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("xxxx", "onSuccess: " + document);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("xxxx", "onFailure: " + e.getLocalizedMessage());
            }
        });

        return dataAdded;
    }


    //--------------------------get data from firebase firestore-------------------------------------------------

    public void getData(String currentUserEmail) {
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document(currentUserEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getId());
                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getData());
                   /*customerModel.setEmail(task.getResult().get("Email").toString());
                   customerModel.setAccountType(task.getResult().get("accountType").toString());
                   customerModel.setPhone(task.getResult().get("phone").toString());
                    Log.d("xxxxx", "onComplete: CustomerModel "+customerModel);*/
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("xxxx", "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    public void updateProfileData(String currentUserEmail , Customer_Model model)
    {   firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference=firebaseFirestore.collection(collection).document(currentUserEmail);
        Map<String,Object> map = new HashMap<>();
        String name=model.getName();
        String phone=model.getPhone();
        String address=model.getAddress();
        map.put("name",name);
        map.put("phone",phone);
        map.put("address",address);
        documentReference.update(map);
    }


    //Updating the key:value pair in existing document
    public void updateProfileData(String currentUserEmail , Owner_Model model)
    {   firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference=firebaseFirestore.collection(collection).document(currentUserEmail);
        Map<String,Object> map = new HashMap<>();
        String name=model.getName();
        String phone=model.getPhone();

        String address=model.getAddress();
        String profileUrl=model.getProfileUrl();

        map.put("name",name);
        map.put("phone",phone);

        map.put("address",address);

        documentReference.update(map);
    }


     //saving house data from owner dashboard
      public void saveHouseData(String documentUid, House house, Context context, String currentUserEmail)
      {
          firebaseFirestore=FirebaseFirestore.getInstance();
          DocumentReference documentReference=  firebaseFirestore.collection("HouseCollection").document(documentUid);
         documentReference.set(house).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(Task<Void> task) {
                 if(task.isSuccessful())

                   // updateHouseInOwnerData(currentUid,currentUserEmail);
                 Toast.makeText(context, "House Added Successfully", Toast.LENGTH_SHORT).show();
                // context.startActivity(new Intent(context, BootomNavBarMain.class));
             }
         });
      }

    //updating house data from owner dashboard

    public void updateHouseData(String documentId,House house, Context context){
        firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference=firebaseFirestore.collection("HouseCollection").document(documentId);

        Map<String, Object> updates = new HashMap<>();
            updates.put("availability", house.isAvailability());
            updates.put("price",house.getPrice());
            updates.put("phone", house.getPhone());
            updates.put("city", house.getCity());
            updates.put("location", house.getLocation());
            updates.put("street", house.getStreet());
            updates.put("size", house.getSize());
            updates.put("houseNo", house.getHouseNo());
            updates.put("views", house.getViews());

            updates.put("lastModifiedDate", house.getLastModifiedDate() );
        documentReference.update(updates);

    }


    public void deleteHouse(String documentId,Context context){
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("HouseCollection").document(documentId)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override


                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(context, "House Deeting Successful", Toast.LENGTH_SHORT).show();


                        //Redirection to UserSpaceActivity with FLAG_ACTIVITY_CLEAR_TOP
                    Intent intent = new Intent( context, UserSpaceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                        // finish current activity
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }

                    }
                }) ;



    }



        private void updateHouseInOwnerData(String currentUid, String currentUserEmail) {
        firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference=firebaseFirestore.collection(collection).document(currentUserEmail);
        documentReference.update("houseList",currentUid);
    }


}
