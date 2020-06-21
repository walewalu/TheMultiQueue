package com.baekdev.themultiqueue.CreateData;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.baekdev.themultiqueue.DataStructure.FFFavMode;
import com.baekdev.themultiqueue.DataStructure.FFFavStyle;
import com.baekdev.themultiqueue.DataStructure.FFJob;
import com.baekdev.themultiqueue.DataStructure.FFUserInfo;
import com.baekdev.themultiqueue.DataStructure.LoLFavMode;
import com.baekdev.themultiqueue.DataStructure.LoLFavPos;
import com.baekdev.themultiqueue.DataStructure.LoLFavStyle;
import com.baekdev.themultiqueue.DataStructure.User;
import com.baekdev.themultiqueue.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateDataFF extends Fragment {
    private static int RESULT_CODE = 2;

    private User user;
    private FFUserInfo ffuser;
    private String[] tribe, job, favMode, favStyle;

    private FFJob jobData;
    private FFFavMode favModeData;
    private FFFavStyle favStyleData;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    public CreateDataFF() {
        // Required empty public constructor
    }

    public static CreateDataFF getInstance(String name, String gender) {
        CreateDataFF fragment = new CreateDataFF();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("gender", gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        Bundle args = getArguments();
        user = new User(args.getString("name"), args.getString("gender"), mUser.getEmail());

        ffuser = new FFUserInfo();
        tribe = new String[]{"휴런", "미코테", "라라펠", "엘레젠", "루가딘", "아우라", "비에라", "로스가르"};
        job = new String[]{"전사", "나이트", "암흑기사", "건브레이커", "닌자", "용기사", "몽크",
                "사무라이", "음유시인", "기공사", "적마도사", "흑마법사", "소환사", "무도가", "청마도사",
        "백마도사", "점성술자", "학자", "잡 없음"};
        favMode = new String[]{"스토리", "레벨링", "PVP", "하드 컨텐츠"
                ,"채집", "골드소서", "스크린샷", "무작위"};
        favStyle = new String[]{"스토리 감상", "룩덕질", "1인분", "연습"
                ,"집꾸미기", "헬퍼", "맵 탐험", "대화"};
        jobData = new FFJob();
        favModeData = new FFFavMode();
        favStyleData = new FFFavStyle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_data_ff, container, false);
        ChipGroup chipTribe = v.findViewById(R.id.chiptribe);
        ChipGroup chipJob = v.findViewById(R.id.chipJob);
        ChipGroup chipFavMode = v.findViewById(R.id.chipFavModeff);
        ChipGroup chipFavStyle = v.findViewById(R.id.chipFavStyleff);
        Button okButton = v.findViewById(R.id.saveButtonff);

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
            c.setClickable(true);
            c.setCheckable(true);
            c.setCheckedIconVisible(false);
            c.setChipBackgroundColorResource(R.color.chip_bg);
            c.setOnCheckedChangeListener(jobCheckedChangeListener);
            chipJob.addView(c);
        }

        for (String s : favMode){
            Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
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
            c.setOnCheckedChangeListener(modeCheckedChangeListener);
            chipFavMode.addView(c);
        }

        for (String s : favStyle){
            Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
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
            c.setOnCheckedChangeListener(styleCheckedChangeListener);
            chipFavStyle.addView(c);
        }

        chipTribe.setSingleSelection(true);
        chipTribe.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch(checkedId){
                    case 3:
                        ffuser.setTribe("hyur");
                        break;
                    case 4:
                        ffuser.setTribe("miqote");
                        break;
                    case 5:
                        ffuser.setTribe("lalafell");
                        break;
                    case 6:
                        ffuser.setTribe("elezen");
                        break;
                    case 7:
                        ffuser.setTribe("roegadyn");
                        break;
                    case 8:
                        ffuser.setTribe("aura");
                        break;
                    case 9:
                        ffuser.setTribe("viera");
                        break;
                    case 10:
                        ffuser.setTribe("hrothgar");
                        break;
                }
            }
        });

        final EditText ffnick = v.findViewById(R.id.ffnick);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("users").child(mUser.getUid()).setValue(user);
                ref.child("users").child(mUser.getUid()).child("ffusers").setValue("true");

                ffuser.setName(ffnick.getText().toString());
                if(!ffuser.getName().isEmpty()) {
                    ref.child("ffusers").child(mUser.getUid()).setValue(ffuser);
                    ref.child("ffusers").child(mUser.getUid()).child("ff_job").setValue(jobData);
                    ref.child("ffusers").child(mUser.getUid()).child("ff_favStyle").setValue(favModeData);
                    ref.child("ffusers").child(mUser.getUid()).child("ff_favMode").setValue(favStyleData);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "이름은 반드시 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    CompoundButton.OnCheckedChangeListener jobCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
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

    CompoundButton.OnCheckedChangeListener modeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case "스토리":
                    favModeData.setStory(isChecked);
                    break;
                case "레벨링":
                    favModeData.setLevel(isChecked);
                    break;
                case "PVP":
                    favModeData.setPvp(isChecked);
                    break;
                case "하드 컨텐츠":
                    favModeData.setHard_contents(isChecked);
                    break;
                case "채집":
                    favModeData.setCollect(isChecked);
                    break;
                case "골드소서":
                    favModeData.setGold_saucer(isChecked);
                    break;
                case "스크린샷":
                    favModeData.setScreenshot(isChecked);
                    break;
                case "무작위":
                    favModeData.setRandom(isChecked);
                    break;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener styleCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case "스토리 감상":
                    favModeData.setStory(isChecked);
                    break;
                case "룩덕질":
                    favStyleData.setFashion(isChecked);
                    break;
                case "1인분":
                    favStyleData.setDoing_role(isChecked);
                    break;
                case "연습":
                    favStyleData.setPractice(isChecked);
                    break;
                case "집꾸미기":
                    favStyleData.setHousing(isChecked);
                    break;
                case "헬퍼":
                    favStyleData.setHelper(isChecked);
                    break;
                case "맵 탐험":
                    favStyleData.setAdventure(isChecked);
                    break;
                case "대화":
                    favStyleData.setTalk(isChecked);
                    break;
            }
        }
    };
}
