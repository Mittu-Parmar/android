package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapter.CartAdapter;
import com.example.mymall.db_handler.DbQueries;
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

import static com.example.mymall.model.AddressesModel.SELECT_ADDRESS;

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
    public static Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private TextView codTitle;
    private View divider;
    private ImageView paytmButton;
    private ImageView codButton;
    String paymentMethod = "PAYTM";
    private ConstraintLayout orderConfirmationLayout;
    private ImageView continueShoppingButton;
    private TextView orderId;
    private String id;
    private boolean success = false;
    public static boolean fromCart;
    public static boolean getQtyIDs = true;
    public static CartAdapter cartAdapter;

    private FirebaseFirestore firebaseFirestore;

    public static boolean codOrderConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
        paytmButton = paymentMethodDialog.findViewById(R.id.paytm);
        codButton = paymentMethodDialog.findViewById(R.id.cod_button);
        codTitle = paymentMethodDialog.findViewById(R.id.cod_button_title);
        divider = paymentMethodDialog.findViewById(R.id.cod_paytm_divider);
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

        cartAdapter = new CartAdapter(cartItemModelList, totalAmount, DeliveryActivity.this, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        addAddressButton.setVisibility(View.VISIBLE);
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs = false;
                Intent addressesIntent = new Intent(DeliveryActivity.this, AddressesActivity.class);
                addressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(addressesIntent);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean allProductAvailable = true;
                for (CartItemModel cartItemModel : cartItemModelList) {
                    if (cartItemModel.isQtyError()) {
                        allProductAvailable = false;
                        break;
                    }
                    if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                        if (!cartItemModel.isCod()) {
                            codButton.setEnabled(false);
                            codButton.setAlpha(0.5f);
                            codTitle.setAlpha(0.5f);
                            divider.setVisibility(View.GONE);
                            break;
                        } else {
                            codButton.setEnabled(false);
                            codButton.setAlpha(1f);
                            codTitle.setAlpha(1f);
                            divider.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (allProductAvailable) {
                    paymentMethodDialog.show();
                }
            }
        });


        codButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "COD";
                placeOrderDetails();
            }
        });


        paytmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "PAYTM";
                placeOrderDetails();
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
            loadingDialog.show();
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {
                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("products").document(cartItemModelList.get(x).getProductId()).collection("quantity").document(quantityDocumentName).set(timeStamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

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
                                                                long availableQty = 0;
                                                                boolean noLongerAvailable = true;
                                                                for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()) {
                                                                    cartItemModelList.get(finalX).setQtyError(false);
                                                                    if (!serverQuantity.contains(qtyId)) {
                                                                        if (noLongerAvailable) {
                                                                            cartItemModelList.get(finalX).setInStock(false);
                                                                        } else {
                                                                            cartItemModelList.get(finalX).setQtyError(true);
                                                                            cartItemModelList.get(finalX).setMaxQuantity(availableQty);
                                                                            Toast.makeText(DeliveryActivity.this, "Sorry !, All products may not be available in required", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        availableQty++;
                                                                        noLongerAvailable = false;
                                                                    }
                                                                }
                                                                cartAdapter.notifyDataSetChanged();
                                                            } else {
                                                                Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });
                                        }

                                    } else {
                                        loadingDialog.dismiss();
                                        Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        } else {
            getQtyIDs = true;
        }
        //=========accessing quantity===========

        name = DbQueries.addressesModelList.get(DbQueries.selectedAddress).getName();
        mobileNo = DbQueries.addressesModelList.get(DbQueries.selectedAddress).getMobileNo();
        if (DbQueries.addressesModelList.get(DbQueries.selectedAddress).getAlternateMobileNo().equals("")) {
            fullName.setText(name + " - " + mobileNo);
        } else {
            fullName.setText(name + " - " + mobileNo + " or " + DbQueries.addressesModelList.get(DbQueries.selectedAddress).getAlternateMobileNo());
        }

        String flatNo = DbQueries.addressesModelList.get(DbQueries.selectedAddress).getFlatNo();
        String locality = DbQueries.addressesModelList.get(DbQueries.selectedAddress).getLocality();
        String landMark = DbQueries.addressesModelList.get(DbQueries.selectedAddress).getLandMark();
        String city = DbQueries.addressesModelList.get(DbQueries.selectedAddress).getCity();
        String state = DbQueries.addressesModelList.get(DbQueries.selectedAddress).getState();

        if (landMark.equals("")) {
            fullAddress.setText(flatNo + " " + locality + " " + city + " " + state);
        } else {
            fullAddress.setText(flatNo + " " + locality + " " + landMark + " " + city + " " + state);
        }
        pincode.setText(DbQueries.addressesModelList.get(DbQueries.selectedAddress).getPinCode());

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
                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1))) {
                                            cartItemModelList.get(finalX).getQtyIDs().clear();
                                        }
                                    }
                                });
                    }
                } else {
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

        loadingDialog.dismiss();
        // TODO: message sending api integration
        Toast.makeText(this, "Your Order is Confirm, your order id is:" + id, Toast.LENGTH_LONG).show();

    }

    private void placeOrderDetails() {
        String userID = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (CartItemModel cartItemModel : cartItemModelList) {
            if (cartItemModel.getType() == cartItemModel.CART_ITEM) {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("order id", id);
                orderDetails.put("product id", cartItemModel.getProductId());
                orderDetails.put("product image", cartItemModel.getProductImage());
                orderDetails.put("product title", cartItemModel.getProductTitle());
                orderDetails.put("user id", userID);
                orderDetails.put("product quantity", cartItemModel.getProductQuantity());
                if (cartItemModel.getCuttedPrice() != null) {
                    orderDetails.put("cutted price", cartItemModel.getCuttedPrice());
                } else {
                    orderDetails.put("cutted price", "");
                }
                orderDetails.put("product price", cartItemModel.getProductPrice());
                if (cartItemModel.getSelectedCouponId() != null) {
                    orderDetails.put("coupon id", cartItemModel.getSelectedCouponId());
                } else {
                    orderDetails.put("coupon id", "");
                }
                if (cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("discounted price", cartItemModel.getDiscountedPrice());
                } else {
                    orderDetails.put("discounted price", "");
                }
                orderDetails.put("ordered date", FieldValue.serverTimestamp());
                orderDetails.put("packed date", FieldValue.serverTimestamp());
                orderDetails.put("shipped date", FieldValue.serverTimestamp());
                orderDetails.put("delivery date", FieldValue.serverTimestamp());
                orderDetails.put("cancelled date", FieldValue.serverTimestamp());
                orderDetails.put("order status", "ordered");
                orderDetails.put("payment method", paymentMethod);
                orderDetails.put("address", fullAddress.getText());
                orderDetails.put("full name", fullName.getText());
                orderDetails.put("pin code", pincode.getText());
                orderDetails.put("free coupons", cartItemModel.getFreeCoupens());
                orderDetails.put("delivery price", cartItemModelList.get(cartItemModelList.size() - 1).getDeliveryPrice());
                orderDetails.put("cancellation requested", false);

                firebaseFirestore.collection("orders").document(String.valueOf(id)).collection("order items").document(cartItemModel.getProductId()).set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("total items", cartItemModel.getTotalItems());
                orderDetails.put("total items price", cartItemModel.getTotalItemsPrice());
                orderDetails.put("delivery price", cartItemModel.getDeliveryPrice());
                orderDetails.put("total amount", cartItemModel.getTotalAmount());
                orderDetails.put("saved amount", cartItemModel.getSavedAmount());

                orderDetails.put("payment status", "not paid");
                orderDetails.put("order status", "cancelled");

                firebaseFirestore.collection("orders").document(id).set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (paymentMethod.equals("PAYTM")) {
                                paytm();
                            } else {
                                cod();
                            }
                        } else {
                            Toast.makeText(DeliveryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void paytm() {
        getQtyIDs = false;
        // TODO: todo payment gateway integration

        //put this block in status success block of integration of payment getway
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("payment status", "paid");
        updateStatus.put("order status", "ordered");
        firebaseFirestore.collection("orders").document(id).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> userOrder = new HashMap<>();
                    userOrder.put("order id", id);
                    userOrder.put("time", FieldValue.serverTimestamp());
                    firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user orders").document(id).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showConfirmationLayout();
                            } else {
                                Toast.makeText(DeliveryActivity.this, "Failed of update user OrderList", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(DeliveryActivity.this, "Order Cancelled", Toast.LENGTH_LONG).show();
                }
            }
        });
        //put this block in status success block of integration of payment getway
    }

    private void cod() {
        getQtyIDs = false;
        paymentMethodDialog.dismiss();
        paymentMethodDialog.dismiss();
        Intent OTPIntent = new Intent(DeliveryActivity.this, OTPVarificationActivity.class);
        OTPIntent.putExtra("mobileNo", mobileNo.substring(0, 10));
        OTPIntent.putExtra("id", id);
        startActivity(OTPIntent);

    }
}
