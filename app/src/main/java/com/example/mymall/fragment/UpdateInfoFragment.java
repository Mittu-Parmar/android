package com.example.mymall.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateInfoFragment extends Fragment {

    private CircularImageView photoCircularImageView;
    private Button changePhotoButton, removeButton, updateButton;
    private TextView nameTextView, emailTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        photoCircularImageView = view.findViewById(R.id.profile_photo_image);
        changePhotoButton = view.findViewById(R.id.change_photo_button);
        removeButton = view.findViewById(R.id.remove_photo_button);
        updateButton = view.findViewById(R.id.update_info_button);
        nameTextView = view.findViewById(R.id.update_info_name);
        emailTextView = view.findViewById(R.id.update_info_email);

        String name = getArguments().getString("name");
        String email = getArguments().getString("email");
        String photo = getArguments().getString("photo");

        Glide.with(getContext()).load(photo).into(photoCircularImageView);
        nameTextView.setText(name);
        emailTextView.setText(email);

        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });


        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(R.drawable.profile_place_holder).into(photoCircularImageView);
            }
        });


        emailTextView.addTextChangedListener(new TextWatcher() {
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
        nameTextView.addTextChangedListener(new TextWatcher() {
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailPassword();
            }
        });


        return view;
    }


    @SuppressLint("ResourceAsColor")
    private void checkInputs() {
        if (!TextUtils.isEmpty(emailTextView.getText().toString())) {
            if (!TextUtils.isEmpty(nameTextView.getText().toString())) {

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

    }


    private void checkEmailPassword() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailTextView.getText().toString().trim().matches(emailPattern)) {
            // TODO: update email and name
        } else {
            emailTextView.setError("invalid email");
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    Glide.with(getContext()).load(uri).into(photoCircularImageView);
                } else {
                    Toast.makeText(getContext(), "Image not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}




