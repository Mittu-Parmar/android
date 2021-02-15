package com.example.mymall.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatePasswordFragment extends Fragment {


    EditText oldPassword, newPassword, confirmNewPassword;
    Button updateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);

        oldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        confirmNewPassword = view.findViewById(R.id.confirm_new_password);
        updateButton = view.findViewById(R.id.update_password_button);


        oldPassword.addTextChangedListener(new TextWatcher() {
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
        newPassword.addTextChangedListener(new TextWatcher() {
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
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
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


        return view;
    }

    private void checkEmailPassword() {

        if (newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {

            // TODO: 14-02-2021 Confirm password

        } else {
            confirmNewPassword.setError("password dose't match");
        }
    }


    @SuppressLint("ResourceAsColor")
    private void checkInputs() {
        if (!TextUtils.isEmpty(oldPassword.getText().toString()) && oldPassword.length() >= 8) {
            if (!TextUtils.isEmpty(newPassword.getText().toString()) && newPassword.length() >= 8) {
                if (!TextUtils.isEmpty(confirmNewPassword.getText().toString()) && confirmNewPassword.length() >= 8) {

                    updateButton.setEnabled(true);
                    updateButton.setTextColor(Color.rgb(255, 255, 255));

                } else {
                    updateButton.setEnabled(false);
                    updateButton.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                updateButton.setEnabled(false);
                updateButton.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            updateButton.setEnabled(false);
            updateButton.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

}
