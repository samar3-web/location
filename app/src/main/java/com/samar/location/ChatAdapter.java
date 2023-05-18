package com.samar.location;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private final List<Message> messages;

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
            viewHolder.message_user.setText("Vous");

            UiMyMessage(ll, rl);

        } else {
            viewHolder.message_user.setText(message.getSenderEmail());
            displaySenderName(message, viewHolder);

            UIrecieverMessage(ll, rl);

        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void UiMyMessage(LinearLayout ll, RelativeLayout rl) {

        ll.setGravity(Gravity.END);
        rl.setBackgroundDrawable(ContextCompat.getDrawable(rl.getContext(), R.drawable.backgound_sent_message));

    }

    private void UIrecieverMessage(LinearLayout ll, RelativeLayout rl) {
        ll.setGravity(Gravity.START);
        rl.setBackgroundDrawable(ContextCompat.getDrawable(rl.getContext(), R.drawable.background_receiver_message));
    }

    private void displaySenderName(Message message, ViewHolder viewHolder) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document(message.getSenderEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {
                        if (snapshot.get("name") != null) {

                            viewHolder.message_user.setText(snapshot.get("name").toString());
                        } else {
                            viewHolder.message_user.setText(message.getSenderEmail());
                        }

                    } catch (Exception e) {

                    }


                }
            }
        });
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
}
