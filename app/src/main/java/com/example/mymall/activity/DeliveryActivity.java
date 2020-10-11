package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.adapter.AddressesAdapter;
import com.example.mymall.adapter.CartAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.AddressesModel;
import com.example.mymall.model.CartItemModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.mymall.model.AddressesModel.DELIVERY_ACTIVITY;

public class DeliveryActivity extends AppCompatActivity {


    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecyclerView;
    private Button addAddressButton;
    private TextView totalAmount;
    private TextView fullName;
    private TextView fullAddress;
    private TextView pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addAddressButton=findViewById(R.id.add_address_btn);
        deliveryRecyclerView=findViewById(R.id.delivery_recycler_view);
        totalAmount=findViewById(R.id.total_cart_amount);

        fullName=findViewById(R.id.full_name);
        fullAddress=findViewById(R.id.address);
        pincode=findViewById(R.id.pin_code);


        LinearLayoutManager layoutManager=new LinearLayoutManager(DeliveryActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        CartAdapter cartAdapter=new CartAdapter(cartItemModelList,totalAmount,DeliveryActivity.this,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        addAddressButton.setVisibility(View.VISIBLE);
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressesIntent=new Intent(DeliveryActivity.this,AddressesActivity.class);
                addressesIntent.putExtra("MODE",DELIVERY_ACTIVITY);
                startActivity(addressesIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DbQueries.addressModelList.size()>0) {
            fullName.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getName());
            fullAddress.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getAddress());
            pincode.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getPinCode());
        }
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
