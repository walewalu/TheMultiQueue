package com.baekdev.themultiqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.baekdev.themultiqueue.CreateData.CreateData;
import com.baekdev.themultiqueue.ProfileUpdate.ProfileFragment;

public class ProfileUpdateActivity extends AppCompatActivity {
    private ProfileFragment profileFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        profileFragment = ProfileFragment.newInstance();
        ft.replace(R.id.frame_profile, profileFragment).commitAllowingStateLoss();

        ImageButton close = findViewById(R.id.closeProfile);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
                fm.popBackStack();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EventBus.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }
}
