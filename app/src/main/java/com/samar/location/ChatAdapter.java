package com.samar.location;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.models.Discussions;
import com.samar.location.models.Message;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;
/*
public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages;

    private static final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();


    public ChatAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;

    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.message, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.message_user = convertView.findViewById(R.id.message_user);
            viewHolder.message_text = convertView.findViewById(R.id.message_text);
            viewHolder.message_time = convertView.findViewById(R.id.message_time);
            viewHolder.message_receiver = convertView.findViewById(R.id.message_receiver);
            viewHolder.messageView = convertView.findViewById(R.id.messageView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Message message = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm");
        String dateString = dateFormat.format(new Date(message.getTime()));

        viewHolder.message_text.setText(message.getText());
        viewHolder.message_time.setText(dateString);

        displaySenderName(message, viewHolder);

        displayReceiverName(message, viewHolder);


        //verifier que l user envoie ce message
        if (user.equals(message.getSenderEmail())) {
            UiMyMessage(viewHolder.messageView);



        }
        else{
            UIrecieverMessage(viewHolder.messageView);
        }


        //background color


        return convertView;
    }

    private void UiMyMessage(RelativeLayout rl){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );


        // layoutParams.addRule(RelativeLayout.ALIGN_END,RelativeLayout.TRUE);
        rl.setLayoutParams(layoutParams);
                                rl.setGravity(Gravity.RIGHT);
                                rl.setBackgroundColor(Color.rgb(0,0,255));
                                rl.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.backgound_sent_message) );

}

    private void UIrecieverMessage(RelativeLayout rl){
        // Récupération de la vue

        //TextView messageTextView = (TextView) findViewById(R.id.message_text);

        // Création des paramètres de mise en page
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rl.setGravity(Gravity.LEFT);

        // layoutParams.addRule(RelativeLayout.ALIGN_END,RelativeLayout.TRUE);
        rl.setLayoutParams(layoutParams);
        rl.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_receiver_message) );

    }
    private void displayReceiverName(Message message, ViewHolder viewHolder) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document( message.getReceiverEmail() ).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {
                        if (snapshot.get("name") != null) {

                            viewHolder.message_receiver.setText("to: " + snapshot.get("name").toString());
                        }
                        else{
                            viewHolder.message_receiver.setText("to: " + message.getReceiverEmail() );
                        }

                    } catch (Exception e) {

                    }


                }
            }
        });
    }
    private void displaySenderName(Message message, ViewHolder viewHolder) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document( message.getSenderEmail() ).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {
                        if (snapshot.get("name") != null) {

                            viewHolder.message_user.setText(snapshot.get("name").toString());
                        }
                        else{
                            viewHolder.message_user.setText(message.getSenderEmail());
                        }

                    } catch (Exception e) {

                    }


                }
            }
        });
    }



}

 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> messages;

    private static final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    public ChatAdapter(List<Message> messagesList) {
        this.messages = messagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Message message = messages.get(position);


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String dateString = dateFormat.format(new Date(message.getTime()));

        RelativeLayout rl = viewHolder.messageView;
        LinearLayout ll = viewHolder.view;

        viewHolder.message_text.setText(message.getText());
        viewHolder.message_time.setText(dateString);

        if (user.equals(message.getSenderEmail())) {
            viewHolder.message_user.setText("Vous"  );

            UiMyMessage(ll,rl);

        }else{
            viewHolder.message_user.setText(message.getSenderEmail() );
            displaySenderName(message, viewHolder);

            UIrecieverMessage(ll,rl);

        }






        //viewHolder.message_receiver.setText(message.getReceiverEmail());







        //displaySenderName(message, viewHolder);
        //displayReceiverName(message, viewHolder);

        //verifier que l user envoie ce message




    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView message_user;
        TextView message_text;
        TextView message_time;


        RelativeLayout messageView;
        LinearLayout view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_user = itemView.findViewById(R.id.message_user);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);

            message_time = itemView.findViewById(R.id.message_time);
            messageView = itemView.findViewById(R.id.messageView);
            view = itemView.findViewById(R.id.view);
        }

    }




    private void UiMyMessage(LinearLayout ll,RelativeLayout rl){

        ll.setGravity(Gravity.END);
        rl.setBackgroundDrawable(ContextCompat.getDrawable(rl.getContext(), R.drawable.backgound_sent_message) );

    }


    private void UIrecieverMessage(LinearLayout ll,RelativeLayout rl){
        ll.setGravity(Gravity.START);
        rl.setBackgroundDrawable(ContextCompat.getDrawable(rl.getContext(), R.drawable.background_receiver_message) );
    }





    private void displaySenderName(Message message, ViewHolder viewHolder) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document( message.getSenderEmail() ).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {
                        if (snapshot.get("name") != null) {

                            viewHolder.message_user.setText(snapshot.get("name").toString());
                        }
                        else{
                            viewHolder.message_user.setText(message.getSenderEmail());
                        }

                    } catch (Exception e) {

                    }


                }
            }
        });
    }
}
