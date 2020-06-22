package com.baekdev.themultiqueue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baekdev.themultiqueue.DataStructure.ChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {
    private static final int GAME_LOL = 1;
    private static final int GAME_FF14 = 2;
    private ArrayList<ChatRoom> mDataset;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView game;
        TextView title;
        TextView description;
        TextView person;

        MyViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout) v.findViewById(R.id.roomlist);
            game = (ImageView) v.findViewById(R.id.game_choice);
            title = (TextView) v.findViewById(R.id.room_title);
            description = (TextView) v.findViewById(R.id.room_description);
            person = (TextView) v.findViewById(R.id.num_ofPerson);
        }
    }

    public ChatRoomAdapter(ArrayList<ChatRoom> dataset){
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ChatRoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ChatRoom room = mDataset.get(position);

        // Show Data
        switch(room.getGame()){
            case GAME_LOL:
                holder.game.setImageResource(R.drawable.lol_unselected);
                holder.linearLayout.setBackgroundResource(R.drawable.rounded_chatroom);
                holder.title.setBackgroundResource(R.drawable.rounded_chatroom2);
                holder.description.setBackgroundResource(R.drawable.rounded_chatroom2);
                break;
            case GAME_FF14:
                holder.game.setImageResource(R.drawable.ff14_unselected);
                holder.linearLayout.setBackgroundResource(R.drawable.rounded_chatroom_2);
                holder.title.setBackgroundResource(R.drawable.rounded_chatroom2_2);
                holder.description.setBackgroundResource(R.drawable.rounded_chatroom2_2);
                break;
        }

        holder.title.setText(room.getName());
        holder.description.setText(room.getDescription());
        holder.person.setText(room.getCurrent_Members() + " / " + room.getMaximum_Members() + "명");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                db = FirebaseDatabase.getInstance();
                ref = db.getReference();

                final View getV = v;
                ref.child("chatdata").child(room.getId()).child("participant").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(getV.getContext(), ChatActivity.class);
                        intent.putExtra("name", room.getName());
                        intent.putExtra("id", room.getId());
                        if(dataSnapshot.exists()){
                            getV.getContext().startActivity(intent);
                        } else {
                            if (room.getCurrent_Members() < room.getMaximum_Members()) {
                                getV.getContext().startActivity(intent);
                                ref.child("chatroom").child(room.getId()).child("current_Members").setValue(room.getCurrent_Members() + 1);
                            } else {
                                Toast.makeText(getV.getContext(), "정원이 초과되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
