package com.example.mymall.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.activity.MainActivity;
import com.example.mymall.R;
import com.example.mymall.activity.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SigninFragment extends Fragment {

    TextView dontHaveAccoutnBtn,forgotPasswordBtn,closeBtn;
    FrameLayout frameLayout;

    EditText email,password;
    Button signinBtn;
    ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth firebaseAuth;

    public static boolean disableCloseButton=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        dontHaveAccoutnBtn=view.findViewById(R.id.signin_dont_have_an_account);
        frameLayout=getActivity().findViewById(R.id.frame_layout);

        forgotPasswordBtn=view.findViewById(R.id.signin_forgot_password);
        closeBtn=view.findViewById(R.id.signin_cross_btn);
        email=view.findViewById(R.id.signin_email);
        password=view.findViewById(R.id.signin_password);
        signinBtn=view.findViewById(R.id.signin_btn);
        progressBar=view.findViewById(R.id.signin_progressBar);

        if (disableCloseButton==true){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent=new Intent(getActivity(), MainActivity.class);
                startActivity(MainIntent);
                getActivity().finish();
            }
        });

        dontHaveAccoutnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignupFragment());
            }
        });
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.forgotPasswordBackPressFragment=true;
                setFragment(new ForgotPasswordFragment());
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailPassword();
            }
        });
    }

    private void checkEmailPassword() {
        if (email.getText().toString().trim().matches(emailPattern)) {
            if (password.getText().toString().length()>=8) {

                progressBar.setVisibility(View.VISIBLE);
                signinBtn.setEnabled(false);
                signinBtn.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth= FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mainIntent();
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            signinBtn.setEnabled(true);
                            signinBtn.setTextColor(Color.rgb(255,255,255));
                        }
                    }
                });
            }else{
                Toast.makeText(getActivity(), "incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "incorrect email or password", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText().toString())) {
                if (!TextUtils.isEmpty(password.getText().toString()) && password.length()>=8) {
                        signinBtn.setEnabled(true);
                        signinBtn.setTextColor(Color.rgb(255,255,255));
                    } else {
                        signinBtn.setEnabled(false);
                        signinBtn.setTextColor(Color.argb(50,255,255,255));
                    }
                } else {
                    signinBtn.setEnabled(false);
                    signinBtn.setTextColor(Color.argb(50,255,255,255));
                }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slidein_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void mainIntent() {
        Intent MainIntent=new Intent(getActivity(),MainActivity.class);
        startActivity(MainIntent);
        disableCloseButton=false;
        getActivity().finish();
    }

}
