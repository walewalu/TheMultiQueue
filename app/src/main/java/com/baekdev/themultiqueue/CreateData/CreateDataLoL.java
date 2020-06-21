package com.baekdev.themultiqueue.CreateData;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.baekdev.themultiqueue.DataStructure.LoLFavPos;
import com.baekdev.themultiqueue.DataStructure.LoLFavMode;
import com.baekdev.themultiqueue.DataStructure.LoLFavStyle;
import com.baekdev.themultiqueue.DataStructure.User;
import com.baekdev.themultiqueue.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateDataLoL extends Fragment {
    private static int RESULT_CODE = 2;

    private User user;
    private String userNick;
    private String[] favPos, favMode, favStyle;

    private LoLFavPos favPosData;
    private LoLFavMode favModeData;
    private LoLFavStyle favStyleData;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    public CreateDataLoL() {
        // Required empty public constructor
    }

    public static CreateDataLoL getInstance(String name, String gender) {
        CreateDataLoL fragment = new CreateDataLoL();
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

        Bundle args = getArguments();
        user = new User(args.getString("name"), args.getString("gender"), mUser.getEmail());

        favPos = new String[]{" 탑 ", "정글", "미드", "원딜", "서포터"};
        favMode = new String[]{"노말", "랭크", "칼바람 나락", "전략적 팀전투"};
        favStyle = new String[]{"승리 지향", "즐거운 게임", "1인분", "연습"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_data_lol, container, false);
        ChipGroup chipFavPos = v.findViewById(R.id.chipFavPos);
        ChipGroup chipFavMode = v.findViewById(R.id.chipFavMode);
        ChipGroup chipFavStyle = v.findViewById(R.id.chipFavStyle);
        Button okButton = v.findViewById(R.id.saveButtonlol);

        favPosData = new LoLFavPos();
        favModeData = new LoLFavMode();
        favStyleData = new LoLFavStyle();

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
            c.setCheckedIconVisible(false);
            c.setChipBackgroundColorResource(R.color.chip_bg);
            c.setOnCheckedChangeListener(posCheckedChangeListener);
            chipFavPos.addView(c);
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

        final EditText lolnick = v.findViewById(R.id.lolnick);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = db.getReference();
                ref.child("users").child(mUser.getUid()).setValue(user);
                ref.child("users").child(mUser.getUid()).child("lolusers").setValue("true");

                HashMap<String, Object> map = new HashMap<>();
                userNick = lolnick.getText().toString();
                Log.d("TAG", userNick);
                map.put("nickname", userNick);

                if(!userNick.isEmpty()) {
                    ref.child("lolusers").child(mUser.getUid()).setValue(map);
                    ref.child("lolusers").child(mUser.getUid()).child("lol_favPos").setValue(favPosData);
                    Log.d("TAG", Boolean.toString(favPosData.isAdc()));
                    ref.child("lolusers").child(mUser.getUid()).child("lol_favMode").setValue(favModeData);
                    ref.child("lolusers").child(mUser.getUid()).child("lol_favStyle").setValue(favStyleData);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "이름은 반드시 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    CompoundButton.OnCheckedChangeListener posCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
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

    CompoundButton.OnCheckedChangeListener modeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case "노말":
                    favModeData.setNormal(isChecked);
                    break;
                case "랭크":
                    favModeData.setRank(isChecked);
                    break;
                case "칼바람 나락":
                    favModeData.setAbyss(isChecked);
                    break;
                case "전략적 팀전투":
                    favModeData.setTft(isChecked);
                    break;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener styleCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getText().toString()){
                case "승리 지향":
                    favStyleData.setWin_first(isChecked);
                    break;
                case "즐거운 게임":
                    favStyleData.setEnjoyable(isChecked);
                    break;
                case "1인분":
                    favStyleData.setDoing_role(isChecked);
                    break;
                case "연습":
                    favStyleData.setPractice(isChecked);
                    break;
            }
        }
    };
}
