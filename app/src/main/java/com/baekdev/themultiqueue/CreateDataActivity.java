package com.baekdev.themultiqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.baekdev.themultiqueue.CreateData.CreateData;

public class CreateDataActivity extends AppCompatActivity {
    private CreateData createData;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_data);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        createData = new CreateData();
        ft.replace(R.id.frameLayout, createData).commitAllowingStateLoss();
    }
}
