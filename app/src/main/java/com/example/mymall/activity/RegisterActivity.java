package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.example.mymall.R;
import com.example.mymall.fragment.SigninFragment;
import com.example.mymall.fragment.SignupFragment;

public class RegisterActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    public static boolean setSignUpFregment=false;

    public static Boolean forgotPasswordBackPressFragment=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frameLayout=findViewById(R.id.frame_layout);
        if (setSignUpFregment){
            setSignUpFregment=false;
            setDefaultFragment(new SignupFragment());
        }else {
            setDefaultFragment(new SigninFragment());
        }
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        SignupFragment.disableCloseButton=false;
        SigninFragment.disableCloseButton=false;

        if (keyCode == KeyEvent.KEYCODE_BACK)
            if(forgotPasswordBackPressFragment){
                setFragment(new SigninFragment());
                forgotPasswordBackPressFragment=false;
                return false;
            }
        return super.onKeyDown(keyCode, event);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slidein_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
