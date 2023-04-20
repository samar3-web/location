package com.samar.location.BottomNavigationBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.DiscussionActivity;
import com.samar.location.R;
import com.samar.location.models.Discussions;
import com.samar.location.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class FriendDiscussionAdapter extends RecyclerView.Adapter<FriendDiscussionAdapter.ViewHolder> {
    private List<Discussions> discussions;



    public FriendDiscussionAdapter(List<Discussions> discussionsList) {
        this.discussions = discussionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Discussions discussion = discussions.get(position);


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String dateString = dateFormat.format(new Date(discussion.getTime()));

         String text = discussion.getText().length()>20 ? discussion.getText().substring(0, 20)+" ..."
                 : discussion.getText();

        viewHolder.friend.setText(discussion.getFriendEmail());

        viewHolder.message_text.setText(text);
        viewHolder.message_time.setText(dateString);


        viewHolder.discussionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DiscussionActivity.class);
                intent.putExtra("friendEmail", discussion.getFriendEmail());
                v.getContext().startActivity(intent);
            }
        });

        displayFriendInformation(viewHolder.itemView.getContext(), discussion, viewHolder);

    }

    @Override
    public int getItemCount() {
        return discussions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageFilterView profile;
        TextView friend;
        TextView message_text;
        TextView message_time;
        RelativeLayout discussionView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.img_thumbnail);
            friend = itemView.findViewById(R.id.friend);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);
            discussionView = itemView.findViewById(R.id.discussionView);
        }



    }



    private void displayFriendInformation(Context  context, Discussions discussion, ViewHolder viewHolder) {
        // Effacer l'image précédente
        viewHolder.profile.setImageResource(R.drawable.b);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String friendEmail = discussion.getFriendEmail();

        firestore.collection("USERDATA").document(friendEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        if(document.getString("name")!=null){
                            String friendName = document.getString("name");
                            viewHolder.friend.setText(friendName);
                        }

                        if( document.getString("profileUrl")!=null){

                            Glide.with(context)
                                    .load( document.getString("profileUrl") )
                                    .into(viewHolder.profile);
                        }




                    }
                }
            }
        });
    }
}




