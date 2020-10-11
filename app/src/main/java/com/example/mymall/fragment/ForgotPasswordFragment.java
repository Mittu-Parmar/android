package com.example.mymall.fragment;


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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {

    EditText RegEmail;
    Button ResetPasswordButton;
    TextView GoBack;
    ProgressBar progressBar;
    ViewGroup emailIconContainer;
    ImageView emailIcon;
    TextView emailText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        RegEmail=view.findViewById(R.id.forgot_password_email_edit_text);
        ResetPasswordButton=view.findViewById(R.id. forgot_password_button);
        GoBack=view.findViewById(R.id. forgot_password_go_back_btn);
        progressBar=view.findViewById(R.id. forgot_password_progressBar);
        emailIconContainer=view.findViewById(R.id.forgot_password_image_and_text_container);
        emailIcon=view.findViewById(R.id.forgot_password_Container_icon_image);
        emailText=view.findViewById(R.id. forgot_password_Container_text_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RegEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SigninFragment());
            }
        });
        ResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailIcon.setVisibility(View.INVISIBLE);
                emailText.setVisibility(View.INVISIBLE);
                emailText.setText("Recovery email sent Successfully! Check your inbox.");
                emailText.setTextColor(getResources().getColor(R.color.successGreen));

                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                ResetPasswordButton.setEnabled(false);
                ResetPasswordButton.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.sendPasswordResetEmail(RegEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    emailIcon.setVisibility(View.VISIBLE);
                                    emailText.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                    ResetPasswordButton.setEnabled(false);
                                    ResetPasswordButton.setTextColor(Color.argb(50,255,255,255));
                                }else{

                                    ResetPasswordButton.setEnabled(true);
                                    ResetPasswordButton.setTextColor(Color.rgb(255,255,255));
                                    progressBar.setVisibility(View.GONE);
                                    emailText.setText(task.getException().getMessage());
                                    emailText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    emailText.setVisibility(View.VISIBLE);
                                }
                            }
                });
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slidein_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(getActivity().findViewById(R.id.frame_layout).getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(RegEmail.getText().toString()) && RegEmail.getText().toString().matches(emailPattern)) {
            ResetPasswordButton.setEnabled(true);
            ResetPasswordButton.setTextColor(Color.rgb(255,255,255));
        }else {
            ResetPasswordButton.setEnabled(false);
            ResetPasswordButton.setTextColor(Color.argb(50,255,255,255));
        }
    }
}


