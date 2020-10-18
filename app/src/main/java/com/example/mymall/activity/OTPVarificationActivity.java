package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;

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

        mobileNo=findViewById(R.id.phone_no);
        OTP=findViewById(R.id.otp);
        verifyButton=findViewById(R.id.verify_btn);

        userMobileNo=getIntent().getStringExtra("mobileNo");
        mobileNo.setText("Verification code has been send to +91 "+userMobileNo);

        Random random=new Random();
        final int OTPNumber=random.nextInt(999999 - 111111)+111111;
        Log.d("Mittu OTP : ",String.valueOf(OTPNumber));

        // TODO: sms gateway integration

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (OTP.getText().toString().equals(String.valueOf(OTPNumber))){
                    DeliveryActivity.codOrderConfirm=true;
                    finish();
                }else {
                    Toast.makeText(OTPVarificationActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
