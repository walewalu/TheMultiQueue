package com.baekdev.themultiqueue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baekdev.themultiqueue.DataStructure.ChatRoom;
import com.baekdev.themultiqueue.DataStructure.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int GAME_LOL = 1;
    private static final int GAME_FF14 = 2;
    private User user;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private DrawerLayout drawerLayout;
    private ImageButton lolButton;
    private ImageButton ffxivButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private RecyclerView chatRoomList;
    private RecyclerView.Adapter rViewAdapter;
    private RecyclerView.LayoutManager rViewManager;
    private ArrayList<ChatRoom> roomItem;
    private ArrayList<String> roomItemIndex;
    private String name, pic;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }

        user = new User();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
        storage = FirebaseStorage.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.profile_revise:
                        Intent intent = new Intent(MainActivity.this, ProfileUpdateActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        mAuth.signOut();
                        Intent intent2 = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent2);
                        finish();
                    case R.id.nav_sub_menu_item01:
                    case R.id.nav_sub_menu_item02:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });

        final ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton addRoom = findViewById(R.id.addRoom);
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatCreateActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        lolButton = findViewById(R.id.lolButton);
        lolButton.setOnClickListener(onClickListener);
        ffxivButton = findViewById(R.id.ffxivButton);
        ffxivButton.setOnClickListener(onClickListener);
        fm = getSupportFragmentManager();

        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        final TextView header_nick = headerView.findViewById(R.id.header_nick);
        final TextView header_email = headerView.findViewById(R.id.header_email);
        final TextView header_lolnick = headerView.findViewById(R.id.header_lolnick);
        final TextView header_ffnick = headerView.findViewById(R.id.header_ffnick);
        final ImageView header_header = headerView.findViewById(R.id.header_header);
        final ImageView header_pic = headerView.findViewById(R.id.header_pic);

        profileButton.setBackgroundResource(R.drawable.circle);
        profileButton.setClipToOutline(true);
        header_pic.setBackgroundResource(R.drawable.circle);
        header_pic.setClipToOutline(true);

        ref.child("users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    user.setName(dataSnapshot.child("name").getValue().toString());
                    name = dataSnapshot.child("name").getValue().toString();
                    user.setEmail(dataSnapshot.child("email").getValue().toString());
                    header_nick.setText(user.getName());
                    header_email.setText(user.getEmail());
                } else {
                    Intent intent = new Intent (MainActivity.this, CreateDataActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });

        ref.child("userpic").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("profileImageUri").getValue() != null){
                    String s = dataSnapshot.child("profileImageUri").getValue().toString();
                    pic = s;
                    Glide.with(getApplicationContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(header_pic);
                    Glide.with(getApplicationContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileButton);
                } else {
                    header_pic.setImageResource(R.drawable.default_pic);
                    profileButton.setImageResource(R.drawable.default_pic);
                }
                if(dataSnapshot.child("headerImageUri").getValue() != null){
                    String s = dataSnapshot.child("headerImageUri").getValue().toString();
                    Glide.with(getApplicationContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(header_header);
                } else {
                    header_header.setImageResource(R.color.colorPrimary);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("lolusers").child(mUser.getUid()).child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    header_lolnick.setText(dataSnapshot.getValue().toString());
                } else {
                    header_lolnick.setText("정보 없음");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });

        ref.child("ffusers").child(mUser.getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    header_ffnick.setText(dataSnapshot.getValue().toString());
                } else {
                    header_ffnick.setText("정보 없음");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });

        chatRoomList = (RecyclerView) findViewById(R.id.room);
        chatRoomList.setHasFixedSize(true);

        rViewManager = new LinearLayoutManager(this);
        chatRoomList.setLayoutManager(rViewManager);

        roomItem = new ArrayList<ChatRoom>();
        roomItemIndex = new ArrayList<String>();
        rViewAdapter = new ChatRoomAdapter(roomItem);
        chatRoomList.setAdapter(rViewAdapter);
        chatRoomList.setItemAnimator(new DefaultItemAnimator());

        ref.child("chatroom").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatRoom room = dataSnapshot.getValue(ChatRoom.class);
                roomItem.add(room);
                roomItemIndex.add(room.getId());
                rViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatRoom room = dataSnapshot.getValue(ChatRoom.class);
                roomItem.set(roomItemIndex.indexOf(room.getId()), room);
                rViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                ChatRoom room = dataSnapshot.getValue(ChatRoom.class);
                roomItem.remove(roomItemIndex.indexOf(room.getId()));
                rViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.lolButton:
                    lolButton.setImageResource(R.drawable.lol_selected);
                    ffxivButton.setImageResource(R.drawable.ff14_unselected);
                    break;
                case R.id.ffxivButton:
                    lolButton.setImageResource(R.drawable.lol_unselected);
                    ffxivButton.setImageResource(R.drawable.ff14_selected);
                    break;
            }
        }
    };

    public void onStart(){
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "방이 개설되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
