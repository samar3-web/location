package com.samar.location;

import android.content.Context;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.models.Message;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = dateFormat.format(new Date(message.getTime()));

        viewHolder.message_text.setText(message.getText());
        viewHolder.message_time.setText(dateString);

        displaySenderName(message, viewHolder);

        displayReceiverName(message, viewHolder);


        //verifier que l user envoie ce message
        if (user.equals(message.getSenderEmail()))
            UiMyMessage(viewHolder.messageView);


        //background color


        return convertView;
    }

    private void UiMyMessage(RelativeLayout rl){
        // Récupération de la vue

    //TextView messageTextView = (TextView) findViewById(R.id.message_text);

    // Création des paramètres de mise en page
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    rl.setGravity(Gravity.END);
   // layoutParams.addRule(RelativeLayout.ALIGN_END,RelativeLayout.TRUE);
                                rl.setLayoutParams(layoutParams);
                                rl.setBackgroundColor(Color.rgb(0,0,255));
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


    private static class ViewHolder {
        TextView message_user;
        TextView message_text;
        TextView message_time;
        TextView message_receiver;
        RelativeLayout messageView;

    }
}
