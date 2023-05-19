package com.samar.location;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.samar.location.models.Message;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DiscussionActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    Button fab;
    ImageView back;
    TextView userName;
    ImageFilterView friendFace;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        database = FirebaseDatabase.getInstance();

        fab = findViewById(R.id.fab);
        userName = findViewById(R.id.user);
        back = findViewById(R.id.back_home);
        friendFace = findViewById(R.id.friendFace);

        // Retrieve the data from the intent

        String friendEmail = getIntent().getStringExtra("friendEmail");

        String currentUser = firebaseAuth.getCurrentUser().getEmail();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);
                if (!input.getText().toString().isEmpty()) {
                    String inputValue = input.getText().toString();
                    String displayValue = currentUser + " : " + inputValue;
                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance()
                            .getReference("MESSAGES")

                            .push()
                            .setValue(new Message(input.getText().toString(),
                                    currentUser, friendEmail)
                            );

                    getFriendTokens(friendEmail).thenAccept(tokens -> {
                        for (String token : tokens) {
                            Log.d("llllllllllllllllllllll", "onComplete: token " + token);
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, "New Message", displayValue, getApplicationContext(), DiscussionActivity.this);
                            notificationsSender.SendNotifications();


                        }
                    }).exceptionally(throwable -> {
                        Log.e("xxxxcustomer", "Error getting tokens: " + throwable.getMessage());
                        return null;
                    });


                    // Clear the input
                    input.setText("");


// Do something with the token


                } else {
                    Toast.makeText(DiscussionActivity.this, "please type a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        displayFriendInformations(friendEmail);


        // Load chat room contents
        displayChatMessages(currentUser, friendEmail);
    }

    private void displayChatMessages(String me, String owner) {

        RecyclerView listOfMessages = findViewById(R.id.list_of_messages);

        DatabaseReference messagesRef = database.getReference("MESSAGES");

        ValueEventListener messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    //verifier que le message est entre les deux (owner & me)
                    if ((me.equals(message.getSenderEmail()) &&
                            owner.equals(message.getReceiverEmail())) ||

                            (owner.equals(message.getSenderEmail()) &&
                                    me.equals(message.getReceiverEmail()))
                    )
                        messages.add(message);
                }

                // Tri
                Comparator<Message> messageComparator = new Comparator<Message>() {
                    @Override
                    public int compare(Message m1, Message m2) {
                        // Triez les messages en fonction de leur temps croissant
                        return Long.compare(m2.getTime(), m1.getTime());
                    }
                };

                //Collections.sort(messages, messageComparator);

                //adapter
                ChatAdapter adapter = new ChatAdapter(messages);
                int spacingInPixels = -10;
                ItemSpacingDecoration itemSpacingDecoration = new ItemSpacingDecoration(spacingInPixels);
                listOfMessages.addItemDecoration(itemSpacingDecoration);
                listOfMessages.setAdapter(adapter);
                listOfMessages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                listOfMessages.scrollToPosition(messages.size() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs ici
            }
        };

        messagesRef.addValueEventListener(messagesListener);

    }


    private void displayFriendInformations(String email) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> snapshot = task.getResult().getData();

                    if (snapshot.get("name") != null) {
                        userName.setText((String) snapshot.get("name"));
                    } else {
                        userName.setText(email);
                    }

                    if (snapshot.get("profileUrl") != null) {
                        Glide.with(friendFace.getContext())
                                .load(snapshot.get("profileUrl"))
                                .into(friendFace);

                    }

                }
            }
        });


    }

 /*   public void getData(String userEmail) {
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document(userEmail).getId();
        firebaseFirestore.collection("USERDATA").document(userEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getId());
                    Log.d("xxxxxx", "onComplete: getData()" + task.getResult().getData());
                   *//*customerModel.setEmail(task.getResult().get("Email").toString());
                   customerModel.setAccountType(task.getResult().get("accountType").toString());
                   customerModel.setPhone(task.getResult().get("phone").toString());
                    Log.d("xxxxx", "onComplete: CustomerModel "+customerModel);*//*
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("xxxx", "onFailure: " + e.getLocalizedMessage());
            }
        });
    }*/

    /*   private List<String> getFriendTokens(String friendEmail){


           FirebaseFirestore firestore = FirebaseFirestore.getInstance();

           //String currentUserEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();
           DocumentReference documentReference= firestore.collection("USERDATA").document(friendEmail);

           documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       Log.d("xxxxcustomer", "onComplete: customer notification " + task.getResult().getData().toString());


                       List<String> tokens = new ArrayList<>();

                       if(task.getResult().get("tokens")!=null)
                           tokens = (List<String>) task.getResult().get("requests");

                       if (tokens.size() > 0) {
                           //le request est le ducuentId de house
                           return tokens;

                       }

                   }
               }
           });
       }*/
    private CompletableFuture<List<String>> getFriendTokens(String friendEmail) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("USERDATA").document(friendEmail);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("xxxxcustomer", "onComplete: customer notification " + task.getResult().getData().toString());

                List<String> tokens = new ArrayList<>();

                if (task.getResult().get("tokens") != null)
                    tokens = (List<String>) task.getResult().get("tokens");

                if (tokens.size() > 0) {
                    future.complete(tokens);
                    Log.d("llllllllllllllllllllll", "onComplete: token.get(0) " + tokens.get(0));

                } else {
                    future.completeExceptionally(new RuntimeException("No tokens found for user " + friendEmail));
                }
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        System.gc();
    }

}


