package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPVarificationActivity extends AppCompatActivity {

    private TextView mobileNo;
    private String userMobileNo;
    private EditText OTP;
    private Button verifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_varification);

        mobileNo = findViewById(R.id.phone_no);
        OTP = findViewById(R.id.otp);
        verifyButton = findViewById(R.id.verify_btn);

        userMobileNo = getIntent().getStringExtra("mobileNo");
        mobileNo.setText("Verification code has been send to +91 " + userMobileNo);

        Random random = new Random();
        final int OTPNumber = random.nextInt(999999 - 111111) + 111111;
        Log.d("Mittu OTP : ", String.valueOf(OTPNumber));

        // TODO: sms gateway integration

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (OTP.getText().toString().equals(String.valueOf(OTPNumber))) {

                    //put this block in status success block of integraton of payment getway
                    Map<String, Object> updateStatus = new HashMap<>();
                    updateStatus.put("order status", "ordered");

                    final String id = getIntent().getStringExtra("id");
                    FirebaseFirestore.getInstance().collection("orders").document(id).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Map<String, Object> userOrder = new HashMap<>();
                                userOrder.put("order id", id);
                                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user orders").document(id).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DeliveryActivity.codOrderConfirm = true;
                                            finish();
                                        } else {
                                            Toast.makeText(OTPVarificationActivity.this, "Failed of update user OrderList", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(OTPVarificationActivity.this, "Order Cancelled", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //put this block in status success block of integraton of payment getway
                } else {
                    Toast.makeText(OTPVarificationActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
