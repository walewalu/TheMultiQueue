package com.baekdev.themultiqueue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baekdev.themultiqueue.DataStructure.ChatData;
import com.baekdev.themultiqueue.DataStructure.ChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatData> arrayList;
    private FirebaseUser mUser;

    public ChatListAdapter(ArrayList<ChatData> list){
        arrayList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == Code.ViewType.CHAT_SEND) {
            v = inflater.inflate(R.layout.chatballon_me, parent, false);
            return new SenderViewHolder(v);
        } else {
            v = inflater.inflate(R.layout.chatballon_you, parent, false);
            return new GetterViewHolder(v);
        }
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatData data = arrayList.get(position);

        if (holder instanceof SenderViewHolder) {
            ((SenderViewHolder)holder).sendMsg.setText(data.getChat());
            ((SenderViewHolder)holder).sendTime.setText(data.getTime());
        } else if (holder instanceof GetterViewHolder) {
            ((GetterViewHolder)holder).getMsg.setText(data.getChat());
            ((GetterViewHolder)holder).getTime.setText(data.getTime());
            ((GetterViewHolder)holder).getName.setText(data.getName());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public int getItemViewType(int position) {
        String s = arrayList.get(position).getUid();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(s.equals(mUser.getUid())){
            return Code.ViewType.CHAT_SEND;
        } else {
            return Code.ViewType.CHAT_GET;
        }
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView sendMsg, sendTime;
        SenderViewHolder(View v) {
            super(v);
            sendMsg = (TextView) v.findViewById(R.id.send_msg);
            sendTime = (TextView) v.findViewById(R.id.send_time);
        }
    }

    static class GetterViewHolder extends RecyclerView.ViewHolder {
        TextView getName, getMsg, getTime;
        GetterViewHolder(View v) {
            super(v);
            getName = (TextView) v.findViewById(R.id.get_name);
            getMsg = (TextView) v.findViewById(R.id.get_msg);
            getTime = (TextView) v.findViewById(R.id.get_time);
        }
    }
}
