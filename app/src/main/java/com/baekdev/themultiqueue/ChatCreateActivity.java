package com.baekdev.themultiqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baekdev.themultiqueue.DataStructure.ChatRoom;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ChatCreateActivity extends AppCompatActivity {
    private static final int GAME_LOL = 1;
    private static final int GAME_FF14 = 2;
    private EditText title;
    private EditText description;
    private ChipGroup maxPerson;
    private ChipGroup selectGame;
    private Button createButton;
    private ChatRoom chatroom;
    private int max;
    private int game;
    private String[] chip1 = {" 2", " 3", " 4", " 5"};
    private String[] chip2 = {"LOL", "FF14"};
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String username;
    private String userpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_create);

        title = (EditText) findViewById(R.id.createroom_title);
        description = (EditText) findViewById(R.id.createroom_description);
        maxPerson = (ChipGroup) findViewById(R.id.createroom_maxPerson);
        selectGame = (ChipGroup) findViewById(R.id.createroom_Game);
        createButton = (Button) findViewById(R.id.createbutton);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        for (String s : chip1) {
            Chip c = new Chip(this, null, R.style.Widget_MaterialComponents_Chip_Filter);
            c.setText(s);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            c.setClickable(true);
            c.setCheckable(true);
            c.setCheckedIconVisible(false);
            c.setChipBackgroundColorResource(R.color.chip_bg);
            maxPerson.addView(c);
        }

        maxPerson.setSingleSelection(true);

        maxPerson.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case 1:
                        max = 2;
                        break;
                    case 2:
                        max = 3;
                        break;
                    case 3:
                        max = 4;
                        break;
                    case 4:
                        max = 5;
                        break;
                }
            }
        });

        for (String s : chip2) {
            Chip c = new Chip(this, null, R.style.Widget_MaterialComponents_Chip_Filter);
            c.setText(s);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            c.setClickable(true);
            c.setCheckable(true);
            c.setCheckedIconVisible(false);
            c.setChipBackgroundColorResource(R.color.chip_bg);
            selectGame.addView(c);
        }

        selectGame.setSingleSelection(true);

        selectGame.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case 5:
                        game = GAME_LOL;
                        break;
                    case 6:
                        game = GAME_FF14;
                        break;
                }
            }
        });

        ref.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    username = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("userpic").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userpic = dataSnapshot.child("profileImageUri").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatroom = new ChatRoom();
                final String code = String.format("%06d", new Random().nextInt(1000000));
                String s = "Room" + code;
                if (title.getText().toString().isEmpty()) {
                    chatroom.setName(s);
                } else {
                    chatroom.setName(title.getText().toString());
                }
                chatroom.setId(code);
                chatroom.setCurrent_Members(1);
                chatroom.setMaximum_Members(max);
                chatroom.setDescription(description.getText().toString());
                chatroom.setGame(game);
                ref.child("chatroom").child(code).setValue(chatroom);
                ref.child("chatdata").child(code).child("leader").setValue(mUser.getUid());
                ref.child("chatdata").child(code).child("participant").child(mUser.getUid()).child("username").setValue(username);
                ref.child("chatdata").child(code).child("participant").child(mUser.getUid()).child("userpic").setValue(userpic);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
