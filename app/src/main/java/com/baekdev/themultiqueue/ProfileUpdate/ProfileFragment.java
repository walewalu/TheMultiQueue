package com.baekdev.themultiqueue.ProfileUpdate;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baekdev.themultiqueue.CreateData.CreateDataLoL;
import com.baekdev.themultiqueue.DataStructure.FFFavMode;
import com.baekdev.themultiqueue.DataStructure.FFFavStyle;
import com.baekdev.themultiqueue.DataStructure.FFJob;
import com.baekdev.themultiqueue.DataStructure.FFUserInfo;
import com.baekdev.themultiqueue.DataStructure.LoLFavMode;
import com.baekdev.themultiqueue.DataStructure.LoLFavPos;
import com.baekdev.themultiqueue.DataStructure.LoLFavStyle;
import com.baekdev.themultiqueue.DataStructure.User;
import com.baekdev.themultiqueue.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

public class ProfileFragment extends Fragment {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ProfileUpdateFragment fragment;
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

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
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
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);

        final ChipGroup chipFavPos = v.findViewById(R.id.profile_chipFavPos);
        final ChipGroup chipFavModeLoL = v.findViewById(R.id.profile_chipFavMode);
        final ChipGroup chipFavStyleLoL = v.findViewById(R.id.profile_chipFavStyle);

        final ChipGroup chipTribe = v.findViewById(R.id.profile_chiptribe);
        final ChipGroup chipJob = v.findViewById(R.id.profile_chipJob);
        final ChipGroup chipFavModeFF = v.findViewById(R.id.profile_chipFavModeff);
        final ChipGroup chipFavStyleFF = v.findViewById(R.id.profile_chipFavStyleff);

        final TextView profileNick = v.findViewById(R.id.profile_nick);
        final TextView lolNick = v.findViewById(R.id.profile_lolnick);
        final TextView ffNick = v.findViewById(R.id.profile_ffnick);
        final ImageView profileHeader = v.findViewById(R.id.profile_header);
        final ImageView profilePic = v.findViewById(R.id.profile_pic);

        profilePic.setBackgroundResource(R.drawable.circle);
        profilePic.setClipToOutline(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
        storage = FirebaseStorage.getInstance();

        ref.child("userpic").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("profileImageUri").getValue() != null){
                    String s = dataSnapshot.child("profileImageUri").getValue().toString();
                    Glide.with(v.getContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePic);
                } else {
                    profilePic.setImageResource(R.drawable.default_pic);
                }
                if(dataSnapshot.child("headerImageUri").getValue() != null){
                    String s = dataSnapshot.child("headerImageUri").getValue().toString();
                    Glide.with(v.getContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileHeader);
                } else {
                    profileHeader.setImageResource(R.color.colorPrimary);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    profileNick.setText(dataSnapshot.child("name").getValue().toString());
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
                    lolNick.setText(dataSnapshot.child("nickname").getValue().toString());
                    favPosData = dataSnapshot.child("lol_favPos").getValue(LoLFavPos.class);
                    favModeDataLoL = dataSnapshot.child("lol_favMode").getValue(LoLFavMode.class);
                    favStyleDataLoL = dataSnapshot.child("lol_favStyle").getValue(LoLFavStyle.class);

                    for (String s : favPos){
                        Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                        c.setText(s);
                        int paddingDp = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 10,
                                getResources().getDisplayMetrics()
                        );
                        c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
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
                        chipFavStyleLoL.addView(c);
                    }
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
                    ffNick.setText(dataSnapshot.child("name").getValue().toString());
                    currentTribe = dataSnapshot.child("tribe").getValue().toString();
                    jobData = dataSnapshot.child("ff_job").getValue(FFJob.class);
                    favModeDataFF = dataSnapshot.child("ff_favMode").getValue(FFFavMode.class);
                    favStyleDataFF = dataSnapshot.child("ff_favStyle").getValue(FFFavStyle.class);

                    for (String s : tribe){
                        Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
                        c.setText(s);
                        int paddingDp = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 10,
                                getResources().getDisplayMetrics()
                        );
                        c.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
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
                        chipFavStyleFF.addView(c);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fm = getActivity().getSupportFragmentManager();
        ImageButton editButton = v.findViewById(R.id.edit_profile_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = fm.beginTransaction();
                fragment = ProfileUpdateFragment.newInstance();
                ft.replace(R.id.frame_profile, fragment);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
            }
        });
        return v;
    }
}
