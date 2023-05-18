package com.samar.location.BottomNavigationBar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.R;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.models.Discussions;
import com.samar.location.models.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView list_of_discussions;
    EditText search;
    String currentUserEmail;
    FirebaseDB firebaseDB;
    FirebaseFirestore firestore;
    FirebaseDatabase database;
    DatabaseReference messagesRef;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Owner_Account.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        list_of_discussions = view.findViewById(R.id.list_of_discussions);

        //search = view.findViewById(R.id.searchFriend);

        firebaseDB = new FirebaseDB();
        firestore = FirebaseFirestore.getInstance();

        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        displayChatDiscussions(currentUserEmail);

        return view;

    }

    private void displayChatDiscussions(String me) {

        database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("MESSAGES");

        ValueEventListener messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Message> myMessages = new ArrayList<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    //verifier que le message est pour moi

                    if (me.equals(message.getSenderEmail()) || me.equals(message.getReceiverEmail()))

                        myMessages.add(message);
                }

                // Tri
                Comparator<Message> messageComparator = new Comparator<Message>() {
                    @Override
                    public int compare(Message m1, Message m2) {
                        // tri DESC
                        return Long.compare(m2.getTime(), m1.getTime());
                    }
                };
                Collections.sort(myMessages, messageComparator);

                getDiscussions(me, myMessages);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs ici
            }
        };

        messagesRef.addValueEventListener(messagesListener);

    }


    private void getDiscussions(String me, List<Message> myMessages) {

        List<Discussions> friendsDiscussions = new ArrayList<>();

        for (Message message : myMessages) {
            String friendEmail = me.equals(message.getSenderEmail()) ? message.getReceiverEmail() : message.getSenderEmail();

            if (!exist(friendEmail, friendsDiscussions)) {

                friendsDiscussions.add(new Discussions(message.getText(), friendEmail, message.getTime()));

            }
        }


        //adapter les friends Discussions
        FriendDiscussionAdapter adapter = new FriendDiscussionAdapter(friendsDiscussions);

        list_of_discussions.setAdapter(adapter);
        list_of_discussions.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    private boolean exist(String email, List<Discussions> discussions) {
        boolean exist = false;
        for (Discussions discussion : discussions) {
            if (email.equals(discussion.getFriendEmail())) {
                exist = true;
                break; // Exit the loop as soon as exist becomes true
            }
        }

        return exist;
    }


}

