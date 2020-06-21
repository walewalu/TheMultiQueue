package com.baekdev.themultiqueue.CreateData;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baekdev.themultiqueue.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;


public class CreateData extends Fragment {
    private static final int SPINNER_LOL = 1;
    private static final int SPINNER_FF14 = 2;

    private String name_args;
    private String gender_args;
    private String[] gender;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private EditText usernick;

    public CreateData() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<String>();
        arrayList.add("게임 선택");
        arrayList.add("리그 오브 레전드");
        arrayList.add("파이널 판타지 14");
        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        fm = getChildFragmentManager();
        gender = new String[]{"남성", "여성"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_data, container, false);

        ChipGroup chipGroupGender = v.findViewById(R.id.chipGroupGender);
        for(String s : gender){
            Chip c = new Chip(v.getContext(), null, R.style.Widget_MaterialComponents_Chip_Filter);
            c.setText(s);
            c.setClickable(true);
            c.setCheckable(true);
            c.setCheckedIconVisible(false);
            c.setChipBackgroundColorResource(R.color.chip_bg);
            chipGroupGender.addView(c);
        }
        chipGroupGender.setSingleSelection(true);
        chipGroupGender.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch(checkedId){
                    case 1:
                        gender_args = "m";
                        break;
                    case 2:
                        gender_args = "f";
                        break;
                }
            }
        });

        usernick = v.findViewById(R.id.usernick);

        Spinner spinner = v.findViewById(R.id.spinner_selectGame);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

        return v;
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            name_args = usernick.getText().toString();
            if (!(name_args.isEmpty()) && !(gender_args.isEmpty())) {
                switch (position) {
                    case SPINNER_LOL:
                        ft = fm.beginTransaction();
                        ft.replace(R.id.frameLayout2, CreateDataLoL.getInstance(name_args, gender_args));
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case SPINNER_FF14:
                        ft = fm.beginTransaction();
                        ft.replace(R.id.frameLayout2, CreateDataFF.getInstance(name_args, gender_args));
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                }
            } else {
                Toast.makeText(view.getContext(), "이름과 성별을 먼저 입력해주세요.", Toast.LENGTH_SHORT);
                parent.setSelection(0);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
