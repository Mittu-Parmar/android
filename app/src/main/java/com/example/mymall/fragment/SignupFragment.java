package com.example.mymall.fragment;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignupFragment extends Fragment {

    TextView alreadyHaveAccountBtn, forgotPasswordBtn, closeBtn;
    FrameLayout frameLayout;
    EditText name, email, password, confirmPassword;
    Button signupBtn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean disableCloseButton = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        alreadyHaveAccountBtn = view.findViewById(R.id.signup_already_have_account);
        frameLayout = getActivity().findViewById(R.id.frame_layout);

        forgotPasswordBtn = view.findViewById(R.id.signup_forgot_password);
        closeBtn = view.findViewById(R.id.signup_cross_btn);
        name = view.findViewById(R.id.signup_name);
        email = view.findViewById(R.id.signup_email);
        password = view.findViewById(R.id.signup_password);
        confirmPassword = view.findViewById(R.id.signup_confirm_password);
        signupBtn = view.findViewById(R.id.signup_btn);
        progressBar = view.findViewById(R.id.signup_progress_bar);

        if (disableCloseButton == true) {
            closeBtn.setVisibility(View.GONE);
        } else {
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
                Intent MainIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(MainIntent);
                getActivity().finish();
            }
        });

        alreadyHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SigninFragment());
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slidein_from_left, R.anim.slideout_from_right);
                fragmentTransaction.replace(frameLayout.getId(), new ForgotPasswordFragment());
                fragmentTransaction.commit();
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
        name.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailPassword();
            }
        });
    }

    private void checkEmailPassword() {
        if (email.getText().toString().trim().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                progressBar.setVisibility(View.VISIBLE);
                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            firebaseFirestore = FirebaseFirestore.getInstance();

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("full name", name.getText().toString());
                            userData.put("email", email.getText().toString());
                            userData.put("profile", "");

                            firebaseFirestore.collection("users").document(firebaseAuth.getUid())
                                    .set(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                CollectionReference userDataReference = firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("user data");

//                                                ========maps======
                                                Map<String, Object> wishListMap = new HashMap<>();
                                                wishListMap.put("list size", (long) 0);

                                                Map<String, Object> ratingsMap = new HashMap<>();
                                                ratingsMap.put("list size", (long) 0);

                                                Map<String, Object> cartMap = new HashMap<>();
                                                cartMap.put("list size", (long) 0);

                                                Map<String, Object> myAddressMap = new HashMap<>();
                                                myAddressMap.put("list size", (long) 0);
//                                                ========maps======

                                                final List<String> documentNames = new ArrayList<>();
                                                documentNames.add("my wishlist");
                                                documentNames.add("my ratings");
                                                documentNames.add("my cart");
                                                documentNames.add("my addresses");

                                                List<Map<String, Object>> documentsFields = new ArrayList<>();
                                                documentsFields.add(wishListMap);
                                                documentsFields.add(ratingsMap);
                                                documentsFields.add(cartMap);
                                                documentsFields.add(myAddressMap);




                                                for (int x = 0; x < documentNames.size(); x++) {
                                                    final int finalX = x;
                                                    userDataReference.document(documentNames.get(x))
                                                            .set(documentsFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                if (finalX == documentNames.size() - 1) {
                                                                    mainIntent();
                                                                }
                                                            } else {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                signupBtn.setEnabled(true);
                                                                signupBtn.setTextColor(Color.rgb(255, 255, 255));
                                                                Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            signupBtn.setEnabled(true);
                            signupBtn.setTextColor(Color.rgb(255, 255, 255));
                        }
                    }
                });
            } else {
                confirmPassword.setError("password dose't match");
            }
        } else {
            email.setError("invalid email");
        }

    }

    @SuppressLint("ResourceAsColor")
    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText().toString())) {
            if (!TextUtils.isEmpty(name.getText().toString())) {
                if (!TextUtils.isEmpty(password.getText().toString()) && password.length() >= 8) {
                    if (!TextUtils.isEmpty(confirmPassword.getText().toString())) {
                        signupBtn.setEnabled(true);
                        signupBtn.setTextColor(Color.rgb(255, 255, 255));
                    } else {
                        signupBtn.setEnabled(false);
                        signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    signupBtn.setEnabled(false);
                    signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            signupBtn.setEnabled(false);
            signupBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slidein_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void mainIntent() {
        Intent MainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(MainIntent);
        disableCloseButton = false;
        getActivity().finish();
    }
}
