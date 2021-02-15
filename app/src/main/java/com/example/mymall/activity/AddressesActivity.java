package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapter.AddressesAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.mymall.model.AddressesModel.SELECT_ADDRESS;

public class AddressesActivity extends AppCompatActivity {

    private int previousAddress;
    private Button deliverHearBtn;
    private RecyclerView addressesRecyclerView;
    public static AddressesAdapter addressesAdapter;
    private TextView addNewAddAddressBtn;
    private TextView addressesSaved;
    private Dialog loadingDialog;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Address");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addressesSaved.setText(DbQueries.addressesModelList.size() + " saved addresses");
            }
        });

        findViewById(R.id.addresses_Deliver_hear_btn).setVisibility(View.GONE);
        addressesRecyclerView = findViewById(R.id.addresses_recycler_view);
        addNewAddAddressBtn = findViewById(R.id.addresses_add_address_btn);
        addressesSaved = findViewById(R.id.addresses_total_address_text_view);
        deliverHearBtn = findViewById(R.id.addresses_Deliver_hear_btn);
        previousAddress = DbQueries.selectedAddress;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddressesActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        addressesRecyclerView.setLayoutManager(linearLayoutManager);


        mode = getIntent().getIntExtra("MODE", -1);
        if (mode == SELECT_ADDRESS) {
            deliverHearBtn.setVisibility(View.VISIBLE);
        } else {
            deliverHearBtn.setVisibility(View.INVISIBLE);
        }


        deliverHearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DbQueries.selectedAddress != previousAddress) {
                    loadingDialog.show();
                    final int previousAddressIndex = previousAddress;

                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected " + String.valueOf(previousAddress + 1), false);
                    updateSelection.put("selected " + String.valueOf(DbQueries.selectedAddress + 1), true);

                    previousAddress = DbQueries.selectedAddress;

                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my addresses")
                            .update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                finish();
                            } else {
                                previousAddress = previousAddressIndex;
                                Toast.makeText(AddressesActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                }
            }
        });

        addressesAdapter = new AddressesAdapter(DbQueries.addressesModelList, loadingDialog);
        addressesRecyclerView.setAdapter(addressesAdapter);
        addressesAdapter.notifyDataSetChanged();
        ((SimpleItemAnimator) addressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        addNewAddAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(AddressesActivity.this, AddAddressActivity.class);
                if (mode != SELECT_ADDRESS) {
                    addAddressIntent.putExtra("INTENT", "manage");
                } else {
                    addAddressIntent.putExtra("INTENT", "null");
                }
                startActivity(addAddressIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        addressesSaved.setText(DbQueries.addressesModelList.size() + " saved addresses");
    }

    public static void refreshItem(int deselect, int select) {
        addressesAdapter.notifyItemChanged(deselect);
        if (select != -1) {
            addressesAdapter.notifyItemChanged(select);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mode == SELECT_ADDRESS) {
                if (DbQueries.selectedAddress != previousAddress) {
                    DbQueries.addressesModelList.get(DbQueries.selectedAddress).setSelected(false);
                    DbQueries.addressesModelList.get(previousAddress).setSelected(true);
                    DbQueries.selectedAddress = previousAddress;
                }
            }
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (mode == SELECT_ADDRESS) {
            if (DbQueries.selectedAddress != previousAddress) {
                DbQueries.addressesModelList.get(DbQueries.selectedAddress).setSelected(false);
                DbQueries.addressesModelList.get(previousAddress).setSelected(true);
                DbQueries.selectedAddress = previousAddress;
            }
            super.onBackPressed();
        }
        finish();
    }
}









