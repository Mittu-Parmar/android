package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapter.AddressesAdapter;
import com.example.mymall.adapter.CartAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.AddressesModel;
import com.example.mymall.model.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.mymall.model.AddressesModel.DELIVERY_ACTIVITY;

public class DeliveryActivity extends AppCompatActivity {


    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecyclerView;
    private Button addAddressButton;
    private Button continueButton;
    private TextView totalAmount;
    private TextView fullName;
    private String name,mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageView paytm;
    private ImageView cod;
    private ConstraintLayout orderConfirmationLayout;
    private ImageView continueShoppingButton;
    private TextView orderId;
    private String id;
    private boolean success=false;
    public static boolean fromCart;

    public static boolean codOrderConfirm=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.layout_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm=paymentMethodDialog.findViewById(R.id.paytm);
        cod=paymentMethodDialog.findViewById(R.id.cod_button);
        id =UUID.randomUUID().toString().substring(0,28);


        addAddressButton=findViewById(R.id.add_address_btn);
        deliveryRecyclerView=findViewById(R.id.delivery_recycler_view);
        totalAmount=findViewById(R.id.total_cart_amount);
        fullName=findViewById(R.id.full_name);
        fullAddress=findViewById(R.id.address);
        pincode=findViewById(R.id.pin_code);
        continueButton=findViewById(R.id.cart_continue_button);
         orderConfirmationLayout=findViewById(R.id.order_confirmation_layout);
         continueShoppingButton=findViewById(R.id.continue_shopping_button);
         orderId=findViewById(R.id.order_id);

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

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.show();
            }
        });


        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
                paymentMethodDialog.dismiss();
                Intent OTPIntent=new Intent(DeliveryActivity.this,OTPVarificationActivity.class);
                OTPIntent.putExtra("mobileNo",mobileNo.substring(0,10));
                startActivity(OTPIntent);
            }
        });


        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: todo payment gateway integration

                showConfirmationLayout();

            }
        });
        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (MainActivity.mainActivity!=null) {
//                    MainActivity.mainActivity.finish();
//                    MainActivity.mainActivity = null;
//                }
                if (CartFragmentActivity.cartFragmentActivity!=null){
                    CartFragmentActivity.cartFragmentActivity.finish();
                    CartFragmentActivity.cartFragmentActivity=null;

                }
                if (ProductDetailsActivity.productDetailsActivity!=null){
                    ProductDetailsActivity.productDetailsActivity.finish();
                    ProductDetailsActivity.productDetailsActivity=null;
                }
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
            name=DbQueries.addressModelList.get(DbQueries.selectedAddress).getName();
            mobileNo=DbQueries.addressModelList.get(DbQueries.selectedAddress).getMobileNo();
            fullName.setText(name + " - " +mobileNo);
            fullAddress.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getAddress());
            pincode.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getPinCode());

        if (codOrderConfirm==true){
            showConfirmationLayout();
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

    @Override
    public void onBackPressed() {
        if (success){
            finish();
            ProductDetailsActivity.productDetailsActivity.finish();
        }
        super.onBackPressed();
    }

    private void showConfirmationLayout(){
        codOrderConfirm=false;
        if (fromCart){
            loadingDialog.show();
            long cartListSize=0;
            final List <Integer> indexList=new ArrayList<>();
            Map<String, Object> updateCartList = new HashMap<>();
            for (int x = 0; x < DbQueries.cartList.size(); x++) {
                if(!cartItemModelList.get(x).isInStock()) {
                    updateCartList.put("product id " + cartListSize, cartItemModelList.get(x).getProductId());
                    cartListSize++;
                }else {
                    indexList.add(x);
                }
            }
            updateCartList.put("list size", cartListSize);
            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my cart")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        for (int x = 0; x < indexList.size() ; x++) {
                            DbQueries.cartList.remove(indexList.get(x).intValue());
                            DbQueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size()-1);
                        }
                    }else {
                        Toast.makeText(DeliveryActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });
        }

        success=true;

        continueButton.setEnabled(false);
        addAddressButton.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        paymentMethodDialog.dismiss();
//                loadingDialog.show();

        orderId.setText("Order ID:"+id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);

        // TODO: message sending api integration
        Toast.makeText(this, "Your Order is Confirm, your order id is:"+id, Toast.LENGTH_LONG).show();

    }
}
