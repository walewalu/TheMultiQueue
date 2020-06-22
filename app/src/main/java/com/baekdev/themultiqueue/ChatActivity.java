package com.baekdev.themultiqueue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baekdev.themultiqueue.DataStructure.ChatData;
import com.baekdev.themultiqueue.DataStructure.ChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String name;
    private TextView title;
    private EditText msg;
    private ImageButton closeChat;
    private ImageButton sendMsg;
    private RecyclerView chatContents;
    private ChatRoom chatRoom;
    private ArrayList<ChatData> list;
    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        title = (TextView) findViewById(R.id.chatroomTitle);
        msg = (EditText) findViewById(R.id.messageinput);
        closeChat = (ImageButton) findViewById(R.id.closeChatRoom);
        sendMsg = (ImageButton) findViewById(R.id.sendButton);
        chatContents = (RecyclerView) findViewById(R.id.chatContents);
        list = new ArrayList<>();
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        adapter = new ChatListAdapter(list);
        chatContents.setLayoutManager(manager);
        chatContents.setAdapter(adapter);


        title.setText(intent.getExtras().getString("name"));
        final String roomId = intent.getExtras().getString("id");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        ref.child("users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });

        ref.child("chatroom").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRoom = dataSnapshot.getValue(ChatRoom.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("chatdata").child(roomId).child("data").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData c = dataSnapshot.getValue(ChatData.class);
                list.add(c);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        closeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msg.getText().toString();
                msg.setText("");
                Date d = new Date();
                SimpleDateFormat date = new SimpleDateFormat("hh:mm a");
                String current_Time = date.format(d);
                String uid = mUser.getUid();
                ChatData c = new ChatData(message, name, uid, current_Time);
                ref.child("chatdata").child(roomId).child("data").push().setValue(c);
            }
        });
    }
}
