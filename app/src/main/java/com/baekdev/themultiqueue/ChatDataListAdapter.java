package com.baekdev.themultiqueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baekdev.themultiqueue.DataStructure.ChatData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatDataListAdapter extends BaseAdapter {
    private ArrayList<ChatData> arrayList;

    public ChatDataListAdapter(){
        arrayList = new ArrayList<ChatData>();
    }

    public ChatDataListAdapter(ArrayList<ChatData> list){
        arrayList = list;
    }

    public void addItem(ChatData c){
        arrayList.add(c);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        ChatData data = arrayList.get(position);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        TextView getName, getMsg, getTime;
        TextView sendMsg, sendTime;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (data.getUid().equals(mUser.getUid())) {
                convertView = inflater.inflate(R.layout.chatballon_me, parent, false);
                sendMsg = (TextView) convertView.findViewById(R.id.send_msg);
                sendTime = (TextView) convertView.findViewById(R.id.send_time);

                sendMsg.setText(data.getChat());
                sendTime.setText(data.getTime());

            } else {
                convertView = inflater.inflate(R.layout.chatballon_you, parent, false);
                getName = (TextView) convertView.findViewById(R.id.get_name);
                getMsg = (TextView) convertView.findViewById(R.id.get_msg);
                getTime = (TextView) convertView.findViewById(R.id.get_time);

                getName.setText(data.getName());
                getMsg.setText(data.getChat());
                getTime.setText(data.getTime());
            }
        }

        return convertView;
    }
}
