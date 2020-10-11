package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.AddressesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pinCode;
    private Spinner stateSpinner;
    private EditText landMark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternateMobileNo;

    private String[] stateList;
    private String selectedState;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stateList = getResources().getStringArray(R.array.india_states);
        loadingDialog = new Dialog(AddAddressActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(AddAddressActivity.this.getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        setContentView(R.layout.activity_add_address);
        saveButton = findViewById(R.id.save_address_btn);
        city = findViewById(R.id.address_city);
        locality = findViewById(R.id.address_locality);
        flatNo = findViewById(R.id.address_flat_no);
        pinCode = findViewById(R.id.address_pin_code);
        stateSpinner = findViewById(R.id.state_spinner);
        landMark = findViewById(R.id.address_land_mark);
        name = findViewById(R.id.address_name);
        mobileNo = findViewById(R.id.address_mobile_no);
        alternateMobileNo = findViewById(R.id.address_alternate_mobile_no);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(AddAddressActivity.this, android.R.layout.simple_spinner_item, stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(spinnerAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Address");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locality.getText())) {
                        if (!TextUtils.isEmpty(flatNo.getText())) {
                            if (!TextUtils.isEmpty(pinCode.getText()) && pinCode.getText().length() == 6) {
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 10) {

                                        final String fullAddress = flatNo.getText() + " " + locality.getText() + " " + landMark.getText() + " " + city.getText() + " " + selectedState;

                                        Map<String, Object> addAddress = new HashMap();
                                        addAddress.put("list size", (long) DbQueries.addressModelList.size() + 1);
                                        if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                                            addAddress.put("full name " + String.valueOf((long) DbQueries.addressModelList.size() + 1), name.getText().toString() + ", " + mobileNo.getText().toString());
                                        } else {
                                            addAddress.put("full name " + String.valueOf((long) DbQueries.addressModelList.size() + 1), name.getText().toString() + ", " + mobileNo.getText().toString() + " " + alternateMobileNo.getText());
                                        }
                                        addAddress.put("address " + String.valueOf((long) DbQueries.addressModelList.size() + 1), fullAddress);
                                        addAddress.put("pincode " + String.valueOf((long) DbQueries.addressModelList.size() + 1), pinCode.getText().toString());
                                        addAddress.put("selected " + String.valueOf((long) DbQueries.addressModelList.size() + 1), true);
                                        if (DbQueries.addressModelList.size() > 0) {
                                            addAddress.put("selected " + (DbQueries.selectedAddress + 1), false);
                                        }
                                        loadingDialog.show();

                                        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my addresses")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (DbQueries.addressModelList.size() > 0) {
                                                        DbQueries.addressModelList.get(DbQueries.selectedAddress).setSelected(false);
                                                    }
                                                    if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                                                        DbQueries.addressModelList.add(new AddressesModel(name.getText().toString() + ", " + mobileNo.getText().toString(), fullAddress, pinCode.getText().toString(), true));
                                                    } else {
                                                        DbQueries.addressModelList.add(new AddressesModel(name.getText().toString() + ", " + mobileNo.getText().toString() + " or " + alternateMobileNo.getText().toString(), fullAddress, pinCode.getText().toString(), true));
                                                    }
                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    } else {
                                                        AddressesActivity.refreshItem(DbQueries.selectedAddress, DbQueries.addressModelList.size() - 1);
                                                    }
                                                    DbQueries.selectedAddress = DbQueries.addressModelList.size() - 1;
                                                    finish();
                                                } else {
                                                    Toast.makeText(AddAddressActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.show();
                                            }
                                        });
                                    } else {
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddAddressActivity.this, "Please provide valid Number", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    name.requestFocus();
                                }
                            } else {
                                pinCode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Please provide valid Pincode", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            flatNo.requestFocus();
                        }
                    } else {
                        locality.requestFocus();
                    }
                } else {
                    city.requestFocus();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
