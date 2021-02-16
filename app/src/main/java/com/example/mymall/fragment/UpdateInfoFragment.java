package com.example.mymall.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymall.R;
import com.example.mymall.db_handler.DbQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateInfoFragment extends Fragment {

    private CircularImageView photoCircularImageView;
    private Button changePhotoButton, removeButton, updateButton;
    private TextView nameField, emailField;
    Dialog loadingDialog, passwordDialog;
    EditText password;
    Button doneButton;
    String name, email, photo;
    Uri imageUri;
    private boolean updatePhoto = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        photoCircularImageView = view.findViewById(R.id.profile_photo_image);
        changePhotoButton = view.findViewById(R.id.change_photo_button);
        removeButton = view.findViewById(R.id.remove_photo_button);
        updateButton = view.findViewById(R.id.update_info_button);
        nameField = view.findViewById(R.id.update_info_name);
        emailField = view.findViewById(R.id.update_info_email);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(true);
        passwordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_background));
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        password = passwordDialog.findViewById(R.id.confirmation_dialog_password);
        doneButton = passwordDialog.findViewById(R.id.confirmation_dialog_done_button);

        name = getArguments().getString("name");
        email = getArguments().getString("email");
        photo = getArguments().getString("photo");

        Glide.with(getContext()).load(photo).into(photoCircularImageView);
        nameField.setText(name);
        emailField.setText(email);

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
                imageUri = null;
                updatePhoto = true;
                Glide.with(getContext()).load(R.drawable.profile_place_holder).into(photoCircularImageView);
            }
        });


        emailField.addTextChangedListener(new TextWatcher() {
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
        nameField.addTextChangedListener(new TextWatcher() {
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
        if (!TextUtils.isEmpty(emailField.getText().toString())) {
            if (!TextUtils.isEmpty(nameField.getText().toString())) {

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

        if (emailField.getText().toString().trim().matches(emailPattern)) {

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (emailField.getText().toString().toLowerCase().trim().equals(email.trim().toLowerCase())) {//same email
                loadingDialog.show();
                updatePhoto(user);
            } else {///update email
                passwordDialog.show();
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        String userPassword = password.getText().toString();
                        passwordDialog.dismiss();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, userPassword);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            user.updateEmail(emailField.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Map<String, Object> updateData = new HashMap<>();
                                                                updateData.put("full name", nameField.getText().toString());
                                                                updateData.put("email", emailField.getText().toString());
                                                                updateFields(user,updateData);

                                                            } else {
                                                                loadingDialog.dismiss();
                                                                Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        } else {
                                            loadingDialog.dismiss();
                                            Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }

        } else {
            emailField.setError("invalid email");
        }

    }

    private void updatePhoto(final FirebaseUser user) {
        ////updating photo

        if (updatePhoto) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile/" + user.getUid() + ".jpg");
            if (imageUri != null) {

                Glide.with(getContext()).asBitmap().load(imageUri).circleCrop().into(new ImageViewTarget<Bitmap>(photoCircularImageView) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {

                                                imageUri = task.getResult();
                                                DbQueries.profile = task.getResult().toString();
                                                Glide.with(getContext()).load(DbQueries.profile).into(photoCircularImageView);

                                                Map<String, Object> updateData = new HashMap<>();
                                                updateData.put("email", emailField.getText().toString());
                                                updateData.put("full name", nameField.getText().toString());
                                                updateData.put("profile", DbQueries.profile);

                                                updateFields(user, updateData);

                                            } else {

                                                loadingDialog.dismiss();
                                                DbQueries.profile = "";
                                                Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    loadingDialog.dismiss();
                                    Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        photoCircularImageView.setImageResource(R.drawable.profile_place_holder);
                    }
                });

            } else {  ////remove photo

                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            DbQueries.profile = "";

                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("email", emailField.getText().toString());
                            updateData.put("full name", nameField.getText().toString());
                            updateData.put("profile", "");

                            updateFields(user, updateData);

                        } else {
                            Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }else {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("full name", nameField.getText().toString());
            updateFields(user,updateData);
        }

        ////updating photo
    }

    private void updateFields(FirebaseUser user, final Map<String, Object> updateData) {
        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).
                update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (updateData.size()>1){
                        DbQueries.fullName=nameField.getText().toString().trim();
                        DbQueries.email=emailField.getText().toString().trim();
                    }else {
                        DbQueries.fullName=nameField.getText().toString().trim();
                    }
                    getActivity().finish();
                    Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();
                    updatePhoto = true;
                    Glide.with(getContext()).load(imageUri).into(photoCircularImageView);
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




