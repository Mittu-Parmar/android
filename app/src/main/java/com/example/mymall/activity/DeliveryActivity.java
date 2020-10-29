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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private String name, mobileNo;
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
    private boolean success = false;
    public static boolean fromCart;
    private boolean allProductAvailable = true;
    public static boolean getQtyIDs = true;

    private FirebaseFirestore firebaseFirestore;

    public static boolean codOrderConfirm = false;

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
        paytm = paymentMethodDialog.findViewById(R.id.paytm);
        cod = paymentMethodDialog.findViewById(R.id.cod_button);
        id = UUID.randomUUID().toString().substring(0, 28);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs = true;


        addAddressButton = findViewById(R.id.add_address_btn);
        deliveryRecyclerView = findViewById(R.id.delivery_recycler_view);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullName = findViewById(R.id.full_name);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pin_code);
        continueButton = findViewById(R.id.cart_continue_button);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingButton = findViewById(R.id.continue_shopping_button);
        orderId = findViewById(R.id.order_id);

        LinearLayoutManager layoutManager = new LinearLayoutManager(DeliveryActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList, totalAmount, DeliveryActivity.this, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        addAddressButton.setVisibility(View.VISIBLE);
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs = false;
                Intent addressesIntent = new Intent(DeliveryActivity.this, AddressesActivity.class);
                addressesIntent.putExtra("MODE", DELIVERY_ACTIVITY);
                startActivity(addressesIntent);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allProductAvailable) {
                    paymentMethodDialog.show();
                } else {
                    //nothing
                }
            }
        });


        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs = false;
                paymentMethodDialog.dismiss();
                paymentMethodDialog.dismiss();
                Intent OTPIntent = new Intent(DeliveryActivity.this, OTPVarificationActivity.class);
                OTPIntent.putExtra("mobileNo", mobileNo.substring(0, 10));
                startActivity(OTPIntent);
            }
        });


        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs = false;
                // TODO: todo payment gateway integration
                showConfirmationLayout();

            }
        });
        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //=========accessing quantity===========
        if (getQtyIDs) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {
                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("products").document(cartItemModelList.get(x).getProductId()).collection("quantity").document(quantityDocumentName).set(timeStamp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    cartItemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);
                                    if (finalY + 1 == cartItemModelList.get(finalX).getProductQuantity()) {
                                        firebaseFirestore.collection("products").document(cartItemModelList.get(finalX).getProductId()).collection("quantity").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuantity()).get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            List<String> serverQuantity = new ArrayList<>();
                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                serverQuantity.add(queryDocumentSnapshot.getId());
                                                            }
                                                            for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()) {
                                                                if (!serverQuantity.contains(qtyId)) {
                                                                    Toast.makeText(DeliveryActivity.this, "Sorry !, All products may not be available in required", Toast.LENGTH_SHORT).show();
                                                                    allProductAvailable = false;
                                                                }
                                                                if (serverQuantity.size() >= cartItemModelList.get(finalX).getStockQuantity()) {
                                                                    firebaseFirestore.collection("products").document(cartItemModelList.get(finalX).getProductId()).update("in stock", false);
                                                                }
                                                            }
                                                        } else {
                                                            Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }

//                final int finalX = x;
//                firebaseFirestore.collection("products").document(cartItemModelList.get(x).getProductId()).collection("quantity").orderBy("available", Query.Direction.DESCENDING).limit(cartItemModelList.get(x).getProductQuantity()).get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//
//                                    for (final QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//
//                                        if ((boolean) queryDocumentSnapshot.get("available")) {
//                                            firebaseFirestore.collection("products").document(cartItemModelList.get(finalX).getProductId()).collection("quantity").document(queryDocumentSnapshot.getId()).update("available", false)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                cartItemModelList.get(finalX).getQtyIDs().add(queryDocumentSnapshot.getId());
//                                                            } else {
//                                                                Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
//
//                                        } else {
//                                            //not available
//                                            allProductAvailable = false;
//                                            Toast.makeText(DeliveryActivity.this, "all products may not be available at required quantity", Toast.LENGTH_SHORT).show();
//                                            break;
//                                        }
//                                    }
//                                } else {
//                                    Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
            }
        } else {
            getQtyIDs = true;
        }
        //=========accessing quantity===========

        name = DbQueries.addressModelList.get(DbQueries.selectedAddress).getName();
        mobileNo = DbQueries.addressModelList.get(DbQueries.selectedAddress).getMobileNo();
        fullName.setText(name + " - " + mobileNo);
        fullAddress.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getAddress());
        pincode.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getPinCode());

        if (codOrderConfirm == true) {
            showConfirmationLayout();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        //=========accessing quantity===========
        if (getQtyIDs) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                if (!success) {
                    for (final String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        final int finalX = x;
                        firebaseFirestore.collection("products").document(cartItemModelList.get(x).getProductId()).collection("quantity").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1))){
                                            cartItemModelList.get(finalX).getQtyIDs().clear();
                                            firebaseFirestore.collection("products").document(cartItemModelList.get(finalX).getProductId()).collection("quantity").orderBy("time", Query.Direction.ASCENDING).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()){
                                                                if (task.getResult().getDocuments().size() < cartItemModelList.get(finalX).getStockQuantity()){
                                                                    firebaseFirestore.collection("products").document(cartItemModelList.get(finalX).getProductId()).update("in stock", true);
                                                                }
                                                            }else {
                                                                Toast.makeText(DeliveryActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }

            }
        }
        //=========accessing quantity===========
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
        if (success) {
            finish();
            return;
//            if (ProductDetailsActivity.productDetailsActivity != null) {
//                ProductDetailsActivity.productDetailsActivity.finish();
//            }
        }
        super.onBackPressed();
    }

    private void showConfirmationLayout() {
        codOrderConfirm = false;
        if (fromCart) {
            loadingDialog.show();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();
            Map<String, Object> updateCartList = new HashMap<>();
            for (int x = 0; x < DbQueries.cartList.size(); x++) {
                if (!cartItemModelList.get(x).isInStock()) {
                    updateCartList.put("product id " + cartListSize, cartItemModelList.get(x).getProductId());
                    cartListSize++;
                } else {
                    indexList.add(x);
                }
            }
            updateCartList.put("list size", cartListSize);
            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my cart")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int x = 0; x < indexList.size(); x++) {
                            DbQueries.cartList.remove(indexList.get(x).intValue());
                            DbQueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size() - 1);
                        }
                    } else {
                        Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });
        }

        success = true;

        continueButton.setEnabled(false);
        addAddressButton.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        paymentMethodDialog.dismiss();
//                loadingDialog.show();

        orderId.setText("Order ID:" + id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);

        MainActivity.resetMainActivity = true;

        if (CartFragmentActivity.cartFragmentActivity != null) {
            CartFragmentActivity.cartFragmentActivity.finish();
            CartFragmentActivity.cartFragmentActivity = null;

        }
        if (ProductDetailsActivity.productDetailsActivity != null) {
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }

        //=========accessing quantity===========
        getQtyIDs = false;
        for (int x = 0; x < cartItemModelList.size() - 1; x++) {
            for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                firebaseFirestore.collection("products").document(cartItemModelList.get(x).getProductId()).collection("quantity").document(qtyID).update("user id", FirebaseAuth.getInstance().getUid());
            }
        }
        //=========accessing quantity===========

        // TODO: message sending api integration
        Toast.makeText(this, "Your Order is Confirm, your order id is:" + id, Toast.LENGTH_LONG).show();

    }
}
