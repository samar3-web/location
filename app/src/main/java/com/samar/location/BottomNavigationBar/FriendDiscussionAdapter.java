package com.samar.location.BottomNavigationBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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




public class FriendDiscussionAdapter extends BaseAdapter {

    private Context context;
    private List<Discussions> discussions;

   // private static final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();


    public FriendDiscussionAdapter(Context context, List<Discussions> discussions) {
        this.context = context;
        this.discussions = discussions;

    }


    @Override
    public int getCount() {
        return discussions.size();
    }

    @Override
    public Discussions getItem(int position) {
        return discussions.get(position);
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
            convertView = inflater.inflate(R.layout.discussion_element, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.friend = convertView.findViewById(R.id.friend);
            viewHolder.message_text = convertView.findViewById(R.id.message_text);
            viewHolder.message_time = convertView.findViewById(R.id.message_time);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Discussions discussion = getItem(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = dateFormat.format(new Date(discussion.getTime()));


        viewHolder.friend.setText(discussion.getFriendEmail());
        viewHolder.message_text.setText(discussion.getText());
        viewHolder.message_time.setText(dateString);


           // displayFriendName(position, discussion, viewHolder);



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DiscussionActivity.class);
                intent.putExtra("friendEmail", discussion.getFriendEmail());
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }










    private void displayFriendName(int position, Discussions discussion, ViewHolder viewHolder) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("USERDATA").document(discussion.getFriendEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> snapshot = task.getResult().getData();
                    try {
                        if ((snapshot.get("name") != null) && !snapshot.get("name").toString().equals("")) {
                            // Vérifiez si les données de Firestore correspondent à l'élément actuellement en cours de traitement dans la méthode getView
                            if (snapshot.get("email").equals(discussion.getFriendEmail())) {
                                // Vérifier si le message Toast a déjà été affiché pour cet élément
                                if (discussion.isDisplayed()) {
                                    return;
                                }
                                Toast.makeText(context, snapshot.get("name").toString(), Toast.LENGTH_LONG).show();
                                viewHolder.friend.setText(snapshot.get("name").toString());
                                discussion.setDisplayed(true);
                            }
                        }
                    } catch (Exception e) {
                        // Gérer les exceptions ici
                    }
                }
            }
        });
    }










    private static class ViewHolder {
        TextView friend;
        TextView message_text;
        TextView message_time;

    }

    public interface Callback<T> {
        void onResult(T result);
    }

}

