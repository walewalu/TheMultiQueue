package com.baekdev.themultiqueue.ProfileUpdate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baekdev.themultiqueue.ActivityResultEvent;
import com.baekdev.themultiqueue.DataStructure.FFFavMode;
import com.baekdev.themultiqueue.DataStructure.FFFavStyle;
import com.baekdev.themultiqueue.DataStructure.FFJob;
import com.baekdev.themultiqueue.DataStructure.LoLFavMode;
import com.baekdev.themultiqueue.DataStructure.LoLFavPos;
import com.baekdev.themultiqueue.DataStructure.LoLFavStyle;
import com.baekdev.themultiqueue.ProfileUpdateActivity;
import com.baekdev.themultiqueue.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileUpdateFragment extends Fragment {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private String[] favPos, favModeLoL, favStyleLoL;
    private String[] tribe, job, favModeFF, favStyleFF;
    private String currentTribe;
    private LoLFavPos favPosData;
    private LoLFavMode favModeDataLoL;
    private LoLFavStyle favStyleDataLoL;
    private FFJob jobData;
    private FFFavMode favModeDataFF;
    private FFFavStyle favStyleDataFF;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private ImageView edit_header;
    private ImageView edit_pic;

    public ProfileUpdateFragment() {
        // Required empty public constructor
    }

    public static ProfileUpdateFragment newInstance() {
        return new ProfileUpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favPos = new String[]{" 탑 ", "정글", "미드", "원딜", "서포터"};
        favModeLoL = new String[]{"노말", "랭크", "칼바람 나락", "전략적 팀전투"};
        favStyleLoL = new String[]{"승리 지향", "즐거운 게임", "1인분", "연습"};
        tribe = new String[]{"휴런", "미코테", "라라펠", "엘레젠", "루가딘", "아우라", "비에라", "로스가르"};
        job = new String[]{"전사", "나이트", "암흑기사", "건브레이커", "닌자", "용기사", "몽크",
                "사무라이", "음유시인", "기공사", "적마도사", "흑마법사", "소환사", "무도가", "청마도사",
                "백마도사", "점성술자", "학자", "잡 없음"};
        favModeFF = new String[]{"스토리", "레벨링", "PVP", "하드 컨텐츠"
                ,"채집", "골드소서", "스크린샷", "무작위"};
        favStyleFF = new String[]{"스토리 감상", "룩덕질", "1인분", "연습"
                ,"집꾸미기", "헬퍼", "맵 탐험", "대화"};
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_profile_update, container, false);

        final ChipGroup chipFavPos = v.findViewById(R.id.edit_chipFavPos);
        final ChipGroup chipFavModeLoL = v.findViewById(R.id.edit_chipFavMode);
        final ChipGroup chipFavStyleLoL = v.findViewById(R.id.edit_chipFavStyle);

        final ChipGroup chipTribe = v.findViewById(R.id.edit_chiptribe);
        final ChipGroup chipJob = v.findViewById(R.id.edit_chipJob);
        final ChipGroup chipFavModeFF = v.findViewById(R.id.edit_chipFavModeff);
        final ChipGroup chipFavStyleFF = v.findViewById(R.id.edit_chipFavStyleff);

        final EditText edit_Name = v.findViewById(R.id.edit_name);
        final EditText edit_lolNick = v.findViewById(R.id.edit_lolnick);
        final EditText edit_ffNick = v.findViewById(R.id.edit_ffnick);
        edit_header = v.findViewById(R.id.edit_header);
        edit_pic = v.findViewById(R.id.edit_pic);

        edit_pic.setBackgroundResource(R.drawable.circle);
        edit_pic.setClipToOutline(true);

        edit_pic.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        edit_header.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();


        ref.child("userpic").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("profileImageUri").getValue() != null){
                    String s = dataSnapshot.child("profileImageUri").getValue().toString();
                    Glide.with(v.getContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(edit_pic);
                } else {
                    edit_pic.setImageResource(R.drawable.default_pic);
                }
                if(dataSnapshot.child("headerImageUri").getValue() != null){
                    String s = dataSnapshot.child("headerImageUri").getValue().toString();
                    Glide.with(v.getContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(edit_header);
                } else {
                    edit_header.setImageResource(R.color.colorPrimary);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    edit_Name.setText(dataSnapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("lolusers").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    edit_lolNick.setText(dataSnapshot.child("nickname").getValue().toString());
                    favPosData = dataSnapshot.child("lol_favPos").getValue(LoLFavPos.class);
                    favModeDataLoL = dataSnapshot.child("lol_favMode").getValue(LoLFavMode.class);
                    favStyleDataLoL = dataSnapshot.child("lol_favStyle").getValue(LoLFavStyle.class);
                }
                for (String s : favPos){
                    Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                    c.setText(s);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    c.setClickable(true);
                    c.setCheckable(true);
                    switch(s){
                        case " 탑 ":
                            c.setChecked(favPosData.isTop());
                            break;
                        case "정글":
                            c.setChecked(favPosData.isJgl());
                            break;
                        case "미드":
                            c.setChecked(favPosData.isMid());
                            break;
                        case "원딜":
                            c.setChecked(favPosData.isAdc());
                            break;
                        case "서포터":
                            c.setChecked(favPosData.isSpt());
                            break;
                    }
                    c.setCheckedIconVisible(false);
                    c.setChipBackgroundColorResource(R.color.chip_bg);
                    c.setOnCheckedChangeListener(lolPosCheckedChangeListener);
                    chipFavPos.addView(c);
                }

                for (String s : favModeLoL){
                    Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                    c.setText(s);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    c.setClickable(true);
                    c.setCheckable(true);
                    switch(s){
                        case "노말":
                            c.setChecked(favModeDataLoL.isNormal());
                            break;
                        case "랭크":
                            c.setChecked(favModeDataLoL.isRank());
                            break;
                        case "칼바랆 나락":
                            c.setChecked(favModeDataLoL.isAbyss());
                            break;
                        case "전략적 팀전투":
                            c.setChecked(favModeDataLoL.isTft());
                            break;
                    }
                    c.setCheckedIconVisible(false);
                    c.setChipBackgroundColorResource(R.color.chip_bg);
                    c.setOnCheckedChangeListener(lolModeCheckedChangeListener);
                    chipFavModeLoL.addView(c);
                }

                for (String s : favStyleLoL){
                    Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                    c.setText(s);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    c.setClickable(true);
                    c.setCheckable(true);
                    switch(s) {
                        case "승리 지향":
                            c.setChecked(favStyleDataLoL.isWin_first());
                            break;
                        case "즐거운 게임":
                            c.setChecked(favStyleDataLoL.isEnjoyable());
                            break;
                        case "1인분":
                            c.setChecked(favStyleDataLoL.isDoing_role());
                            break;
                        case "연습":
                            c.setChecked(favStyleDataLoL.isPractice());
                            break;
                    }
                    c.setCheckedIconVisible(false);
                    c.setChipBackgroundColorResource(R.color.chip_bg);
                    c.setOnCheckedChangeListener(lolStyleCheckedChangeListener);
                    chipFavStyleLoL.addView(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("ffusers").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    edit_ffNick.setText(dataSnapshot.child("name").getValue().toString());
                    currentTribe = dataSnapshot.child("tribe").getValue().toString();
                    jobData = dataSnapshot.child("ff_job").getValue(FFJob.class);
                    favModeDataFF = dataSnapshot.child("ff_favMode").getValue(FFFavMode.class);
                    favStyleDataFF = dataSnapshot.child("ff_favStyle").getValue(FFFavStyle.class);
                }
                for (String s : tribe){
                    Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                    c.setText(s);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    c.setClickable(true);
                    c.setCheckable(true);
                    switch(s){
                        case "휴런":
                            if (currentTribe.equals("hyur")) c.setChecked(true);
                            break;
                        case "미코테":
                            if (currentTribe.equals("miqote")) c.setChecked(true);
                            break;
                        case "라라펠":
                            if (currentTribe.equals("lalafell")) c.setChecked(true);
                            break;
                        case "엘레젠":
                            if (currentTribe.equals("elezen")) c.setChecked(true);
                            break;
                        case "루가딘":
                            if (currentTribe.equals("roeqadyn")) c.setChecked(true);
                            break;
                        case "아우라":
                            if (currentTribe.equals("aura")) c.setChecked(true);
                            break;
                        case "비에라":
                            if (currentTribe.equals("viera")) c.setChecked(true);
                            break;
                        case "로스가르":
                            if (currentTribe.equals("hrothgar")) c.setChecked(true);
                            break;
                    }
                    c.setCheckedIconVisible(false);
                    c.setChipBackgroundColorResource(R.color.chip_bg);
                    chipTribe.setSingleSelection(true);
                    chipTribe.addView(c);
                }

                for (String s : job) {
                    Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                    c.setText(s);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    c.setClickable(true);
                    c.setCheckable(true);
                    switch(s){
                        case "전사":
                            c.setChecked(jobData.isWarrior());
                            break;
                        case "나이트":
                            c.setChecked(jobData.isPaladin());
                            break;
                        case "암흑기사":
                            c.setChecked(jobData.isDark_knight());
                            break;
                        case "건브레이커":
                            c.setChecked(jobData.isGunbreaker());
                            break;
                        case "닌자":
                            c.setChecked(jobData.isNinja());
                            break;
                        case "용기사":
                            c.setChecked(jobData.isDragoon());
                            break;
                        case "몽크":
                            c.setChecked(jobData.isMonk());
                            break;
                        case "사무라이":
                            c.setChecked(jobData.isSamurai());
                            break;
                        case "음유시인":
                            c.setChecked(jobData.isBard());
                            break;
                        case "기공사":
                            c.setChecked(jobData.isMachinist());
                            break;
                        case "적마도사":
                            c.setChecked(jobData.isRed_mage());
                            break;
                        case "흑마법사":
                            c.setChecked(jobData.isBlack_mage());
                            break;
                        case "소환사":
                            c.setChecked(jobData.isSummoner());
                            break;
                        case "무도가":
                            c.setChecked(jobData.isDancer());
                            break;
                        case "청마도사":
                            c.setChecked(jobData.isBlue_mage());
                            break;
                        case "백마도사":
                            c.setChecked(jobData.isWhite_mage());
                            break;
                        case "점성술자":
                            c.setChecked(jobData.isAstrologian());
                            break;
                        case "학자":
                            c.setChecked(jobData.isScholar());
                            break;
                        case "잡 없음":
                            c.setChecked(jobData.isNo_job());
                            break;
                    }
                    c.setCheckedIconVisible(false);
                    c.setChipBackgroundColorResource(R.color.chip_bg);
                    c.setOnCheckedChangeListener(ffJobCheckedChangeListener);
                    chipJob.addView(c);
                }

                for (String s : favModeFF){
                    Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                    c.setText(s);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    c.setClickable(true);
                    c.setCheckable(true);
                    switch(s){
                        case "스토리":
                            c.setChecked(favModeDataFF.isStory());
                            break;
                        case "레벨링":
                            c.setChecked(favModeDataFF.isLevel());
                            break;
                        case "PVP":
                            c.setChecked(favModeDataFF.isPvp());
                            break;
                        case "하드 컨텐츠":
                            c.setChecked(favModeDataFF.isHard_contents());
                            break;
                        case "채집":
                            c.setChecked(favModeDataFF.isCollect());
                            break;
                        case "골드소서":
                            c.setChecked(favModeDataFF.isGold_saucer());
                            break;
                        case "스크린샷":
                            c.setChecked(favModeDataFF.isScreenshot());
                            break;
                        case "무작위":
                            c.setChecked(favModeDataFF.isRandom());
                            break;
                    }
                    c.setCheckedIconVisible(false);
                    c.setChipBackgroundColorResource(R.color.chip_bg);
                    c.setOnCheckedChangeListener(ffModeCheckedChangeListener);
                    chipFavModeFF.addView(c);
                }

                for (String s : favStyleFF){
                    Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                    c.setText(s);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    c.setClickable(true);
                    c.setCheckable(true);
                    switch(s){
                        case "스토리 감상":
                            c.setChecked(favStyleDataFF.isStory());
                            break;
                        case "룩덕질":
                            c.setChecked(favStyleDataFF.isFashion());
                            break;
                        case "1인분":
                            c.setChecked(favStyleDataFF.isDoing_role());
                            break;
                        case "연습":
                            c.setChecked(favStyleDataFF.isPractice());
                            break;
                        case "집꾸미기":
                            c.setChecked(favStyleDataFF.isHousing());
                            break;
                        case "헬퍼":
                            c.setChecked(favStyleDataFF.isHelper());
                            break;
                        case "맵 탐험":
                            c.setChecked(favStyleDataFF.isAdventure());
                            break;
                        case "대화":
                            c.setChecked(favStyleDataFF.isTalk());
                            break;
                    }
                    c.setCheckedIconVisible(false);
                    c.setChipBackgroundColorResource(R.color.chip_bg);
                    c.setOnCheckedChangeListener(ffStyleCheckedChangeListener);
                    chipFavStyleFF.addView(c);
                }

                Log.d("TAG", Integer.toString(chipTribe.getCheckedChipId()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chipTribe.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Log.d("TAG", Integer.toString(checkedId));
                switch(checkedId){
                    case 70:
                        currentTribe = "hyur";
                        break;
                    case 71:
                        currentTribe = "miqote";
                        break;
                    case 72:
                        currentTribe = "lalafell";
                        break;
                    case 73:
                        currentTribe = "elezen";
                        break;
                    case 74:
                        currentTribe = "roegadyn";
                        break;
                    case 75:
                        currentTribe = "aura";
                        break;
                    case 76:
                        currentTribe = "viera";
                        break;
                    case 77:
                        currentTribe = "hrothgar";
                        break;
                }
            }
        });

        fm = getActivity().getSupportFragmentManager();
        ImageButton saveButton = v.findViewById(R.id.save_profile_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = db.getReference();

                String name = edit_Name.getText().toString();
                String lolNick = edit_lolNick.getText().toString();
                String ffNick = edit_ffNick.getText().toString();

                ref.child("users").child(mUser.getUid()).child("name").setValue(name);
                ref.child("users").child(mUser.getUid()).child("lolusers").setValue("true");

                if(!name.isEmpty()) {
                    ref.child("users").child(mUser.getUid()).child("name").setValue(name);
                    ref.child("lolusers").child(mUser.getUid()).child("nickname").setValue(lolNick);
                    ref.child("lolusers").child(mUser.getUid()).child("lol_favPos").setValue(favPosData);
                    ref.child("lolusers").child(mUser.getUid()).child("lol_favMode").setValue(favModeDataLoL);
                    ref.child("lolusers").child(mUser.getUid()).child("lol_favStyle").setValue(favStyleDataLoL);
                    ref.child("ffusers").child(mUser.getUid()).child("name").setValue(ffNick);
                    ref.child("ffusers").child(mUser.getUid()).child("tribe").setValue(currentTribe);
                    ref.child("ffusers").child(mUser.getUid()).child("ff_job").setValue(jobData);
                    ref.child("ffusers").child(mUser.getUid()).child("ff_favStyle").setValue(favStyleDataFF);
                    ref.child("ffusers").child(mUser.getUid()).child("ff_favMode").setValue(favModeDataFF);

                    ft = fm.beginTransaction();
                    ft.replace(R.id.frame_profile, ProfileFragment.newInstance());
                    ft.addToBackStack(null);
                    ft.commitAllowingStateLoss();
                } else {
                    Toast.makeText(getContext(), "이름은 반드시 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    CompoundButton.OnCheckedChangeListener lolPosCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case " 탑 ":
                    favPosData.setTop(isChecked);
                    break;
                case "정글":
                    favPosData.setJgl(isChecked);
                    break;
                case "미드":
                    favPosData.setMid(isChecked);
                    break;
                case "원딜":
                    favPosData.setAdc(isChecked);
                    break;
                case "서포터":
                    favPosData.setSpt(isChecked);
                    break;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener lolModeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getText().toString()) {
                case "노말":
                    favModeDataLoL.setNormal(isChecked);
                    break;
                case "랭크":
                    favModeDataLoL.setRank(isChecked);
                    break;
                case "칼바람 나락":
                    favModeDataLoL.setAbyss(isChecked);
                    break;
                case "전략적 팀전투":
                    favModeDataLoL.setTft(isChecked);
                    break;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener lolStyleCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case "승리 지향":
                    favStyleDataLoL.setWin_first(isChecked);
                    break;
                case "즐거운 게임":
                    favStyleDataLoL.setEnjoyable(isChecked);
                    break;
                case "1인분":
                    favStyleDataLoL.setDoing_role(isChecked);
                    break;
                case "연습":
                    favStyleDataLoL.setPractice(isChecked);
                    break;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener ffJobCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case "전사":
                    jobData.setWarrior(isChecked);
                    break;
                case "나이트":
                    jobData.setPaladin(isChecked);
                    break;
                case "암흑기사":
                    jobData.setDark_knight(isChecked);
                    break;
                case "건브레이커":
                    jobData.setGunbreaker(isChecked);
                    break;
                case "닌자":
                    jobData.setNinja(isChecked);
                    break;
                case "용기사":
                    jobData.setDragoon(isChecked);
                    break;
                case "몽크":
                    jobData.setMonk(isChecked);
                    break;
                case "사무라이":
                    jobData.setSamurai(isChecked);
                    break;
                case "음유시인":
                    jobData.setBard(isChecked);
                    break;
                case "기공사":
                    jobData.setMachinist(isChecked);
                    break;
                case "적마도사":
                    jobData.setRed_mage(isChecked);
                    break;
                case "흑마법사":
                    jobData.setBlack_mage(isChecked);
                    break;
                case "소환사":
                    jobData.setSummoner(isChecked);
                    break;
                case "무도가":
                    jobData.setDancer(isChecked);
                    break;
                case "청마도사":
                    jobData.setBlue_mage(isChecked);
                    break;
                case "백마도사":
                    jobData.setWhite_mage(isChecked);
                    break;
                case "점성술자":
                    jobData.setAstrologian(isChecked);
                    break;
                case "학자":
                    jobData.setScholar(isChecked);
                    break;
                case "잡 없음":
                    jobData.setNo_job(isChecked);
                    break;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener ffModeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case "스토리":
                    favModeDataFF.setStory(isChecked);
                    break;
                case "레벨링":
                    favModeDataFF.setLevel(isChecked);
                    break;
                case "PVP":
                    favModeDataFF.setPvp(isChecked);
                    break;
                case "하드 컨텐츠":
                    favModeDataFF.setHard_contents(isChecked);
                    break;
                case "채집":
                    favModeDataFF.setCollect(isChecked);
                    break;
                case "골드소서":
                    favModeDataFF.setGold_saucer(isChecked);
                    break;
                case "스크린샷":
                    favModeDataFF.setScreenshot(isChecked);
                    break;
                case "무작위":
                    favModeDataFF.setRandom(isChecked);
                    break;
            }
        }
    };
    CompoundButton.OnCheckedChangeListener ffStyleCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()) {
                case "스토리 감상":
                    favModeDataFF.setStory(isChecked);
                    break;
                case "룩덕질":
                    favStyleDataFF.setFashion(isChecked);
                    break;
                case "1인분":
                    favStyleDataFF.setDoing_role(isChecked);
                    break;
                case "연습":
                    favStyleDataFF.setPractice(isChecked);
                    break;
                case "집꾸미기":
                    favStyleDataFF.setHousing(isChecked);
                    break;
                case "헬퍼":
                    favStyleDataFF.setHelper(isChecked);
                    break;
                case "맵 탐험":
                    favStyleDataFF.setAdventure(isChecked);
                    break;
                case "대화":
                    favStyleDataFF.setTalk(isChecked);
                    break;
            }
        }
    };

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storage = FirebaseStorage.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        final StorageReference pic = storage.getReference().child("pic/" + mUser.getUid() + ".jpg");
        final StorageReference header = storage.getReference().child("header/" + mUser.getUid() + ".jpg");

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성


                    InputStream in = ((ProfileUpdateActivity)getActivity()).getContentResolver().openInputStream(data.getData());
                    final Bitmap img1 = BitmapFactory.decodeStream(in);
                    in.close();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    img1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bitmapdata = baos.toByteArray();
                    baos.close();

                    UploadTask uploadTask = pic.putBytes(bitmapdata);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("Error", exception.getLocalizedMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ref.child("userpic").child(mUser.getUid()).child("profileImageUri").setValue(uri.toString());
                                    edit_pic.setImageBitmap(img1);
                                }
                            });
                        }
                    });
                    // 이미지 표시
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = ((ProfileUpdateActivity)getActivity()).getContentResolver().openInputStream(data.getData());
                    final Bitmap img2 = BitmapFactory.decodeStream(in);
                    in.close();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    img2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bitmapdata = baos.toByteArray();
                    baos.close();

                    UploadTask uploadTask = header.putBytes(bitmapdata);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("Error", exception.getLocalizedMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            header.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ref.child("userpic").child(mUser.getUid()).child("headerImageUri").setValue(uri.toString());
                                    edit_header.setImageBitmap(img2);
                                }
                            });
                        }
                    });
                    // 이미지 표시
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
