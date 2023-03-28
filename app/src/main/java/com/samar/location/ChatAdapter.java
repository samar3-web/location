package com.samar.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samar.location.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> messages;

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public ChatMessage getItem(int position) {
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
            viewHolder.message_user =  convertView.findViewById(R.id.message_user);
            viewHolder.message_text =  convertView.findViewById(R.id.message_text);
            viewHolder.message_time = convertView.findViewById(R.id.message_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }




        ChatMessage message = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = dateFormat.format(new Date(message.getMessageTime()));


        viewHolder.message_user.setText(message.getMessageSender()+" to "+message.getMessageReceiver());
        viewHolder.message_text.setText(message.getMessageText());
        viewHolder.message_time.setText(dateString );



        return convertView;
    }

    private static class ViewHolder {
        TextView message_user;
        TextView message_text;
        TextView message_time;
    }
}
