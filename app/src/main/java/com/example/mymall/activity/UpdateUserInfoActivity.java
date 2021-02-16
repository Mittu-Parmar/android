package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.mymall.R;
import com.example.mymall.fragment.UpdateInfoFragment;
import com.example.mymall.fragment.UpdatePasswordFragment;
import com.google.android.material.tabs.TabLayout;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UpdateInfoFragment updateInfoFragment;
    private UpdatePasswordFragment updatePasswordFragment;

    String name;
    String email;
    String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        tabLayout = findViewById(R.id.tab_layout);
        frameLayout = findViewById(R.id.update_user_frame_layout);

        updateInfoFragment = new UpdateInfoFragment();
        updatePasswordFragment = new UpdatePasswordFragment();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        photo = getIntent().getStringExtra("photo");


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setFragment(updateInfoFragment, true);
                }
                if (tab.getPosition() == 1) {
                    setFragment(updatePasswordFragment, false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0).select();
        setFragment(updateInfoFragment, true);
    }

    private void setFragment(Fragment fragment, boolean setBundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (setBundle) {
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("email", email);
            bundle.putString("photo", photo);
            fragment.setArguments(bundle);
        } else {

            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}




