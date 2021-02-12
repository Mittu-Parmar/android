package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.OrderItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    private int position;

    private TextView title;
    private TextView quantity;
    private TextView price;
    private ImageView productImage;
    private ImageView orderedIndicator;
    private ImageView packedIndicator;
    private ImageView shippedIndicator;
    private ImageView deliveredIndicator;
    private ProgressBar O_P_Progress;
    private ProgressBar P_S_Progress;
    private ProgressBar S_D_Progress;
    private TextView orderedTitle;
    private TextView packedTitle;
    private TextView shippedTitle;
    private TextView deliveredTitle;
    private TextView orderedDate;
    private TextView packedDate;
    private TextView shippedDate;
    private TextView deliveredDate;
    private TextView orderedBody;
    private TextView packedBody;
    private TextView shippedBody;
    private TextView deliveredBody;
    private LinearLayout rateNowContainer;

    private int rating;

    private TextView fullName;
    private TextView address;
    private TextView pinCode;

    private TextView totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount;
    private Dialog loadingDialog, cancelDialog;

    private SimpleDateFormat simpleDateFormat;
    private Button cancelOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        cancelDialog = new Dialog(OrderDetailsActivity.this);
        cancelDialog.setContentView(R.layout.order_cancel_dialog);
        cancelDialog.setCancelable(true);
        cancelDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.layout_background));
//        cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        position = getIntent().getIntExtra("position", -1);
        final OrderItemModel orderItemModel = DbQueries.orderItemModelList.get(position);

        title = findViewById(R.id.order_details_product_title);
        quantity = findViewById(R.id.order_details_product_qty);
        price = findViewById(R.id.order_details_product_price);

        productImage = findViewById(R.id.order_details_product_image);
        cancelOrderButton = findViewById(R.id.order_cancel_button);

        orderedIndicator = findViewById(R.id.orderd_indicator);
        packedIndicator = findViewById(R.id.packed_indicator);
        shippedIndicator = findViewById(R.id.shipping_indicator);
        deliveredIndicator = findViewById(R.id.deleverd_indicator);

        O_P_Progress = findViewById(R.id.orderd_packed_progress_bar);
        P_S_Progress = findViewById(R.id.packed_shipping_progress_bar);
        S_D_Progress = findViewById(R.id.shipping_diliverd_progress_bar);

        orderedTitle = findViewById(R.id.ordered_title);
        packedTitle = findViewById(R.id.packed_title);
        shippedTitle = findViewById(R.id.shipping_title);
        deliveredTitle = findViewById(R.id.delivered_title);

        orderedDate = findViewById(R.id.ordered_date);
        packedDate = findViewById(R.id.packed_date);
        shippedDate = findViewById(R.id.shipping_date);
        deliveredDate = findViewById(R.id.deleverd_date);

        orderedBody = findViewById(R.id.order_body);
        packedBody = findViewById(R.id.packed_body);
        shippedBody = findViewById(R.id.shipping_body);
        deliveredBody = findViewById(R.id.delivered_body);

        fullName = findViewById(R.id.full_name);
        address = findViewById(R.id.address);
        pinCode = findViewById(R.id.pin_code);

        rateNowContainer = findViewById(R.id.order_details_rate_now_container);

        totalItems = findViewById(R.id.total_items);
        totalItemsPrice = findViewById(R.id.total_items_price);
        deliveryPrice = findViewById(R.id.delivery_price);
        totalAmount = findViewById(R.id.total_price);
        savedAmount = findViewById(R.id.saved_amount);


        title.setText(orderItemModel.getProductTitle());
        if (!orderItemModel.getDiscountedPrice().equals("")) {
            price.setText("Rs." + orderItemModel.getDiscountedPrice() + "/-");
        } else {
            price.setText("Rs." + orderItemModel.getProductPrice() + "/-");
        }
        quantity.setText("Qty " + String.valueOf(orderItemModel.getProductQuantity()));
        Glide.with(this).load(orderItemModel.getProductImage()).into(productImage);

        simpleDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");

        switch (orderItemModel.getOrderStatus()) {
            case "ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getOrderedDate())));

                O_P_Progress.setVisibility(View.GONE);
                P_S_Progress.setVisibility(View.GONE);
                S_D_Progress.setVisibility(View.GONE);

                packedIndicator.setVisibility(View.GONE);
                packedBody.setVisibility(View.GONE);
                packedTitle.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);

                break;
            case "packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getPackedDate())));

                O_P_Progress.setProgress(100);

                P_S_Progress.setVisibility(View.GONE);
                S_D_Progress.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);

                break;
            case "shipped":

                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getShippedDate())));

                P_S_Progress.setProgress(100);
                P_S_Progress.setProgress(100);

                S_D_Progress.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);

                break;
            case "delivered":

                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getShippedDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getDeliveredDate())));

                P_S_Progress.setProgress(100);
                P_S_Progress.setProgress(100);
                S_D_Progress.setProgress(100);

                break;
            case "cancelled":

                if (orderItemModel.getPackedDate().after(orderItemModel.getOrderedDate())) {
                    if (orderItemModel.getShippedDate().after(orderItemModel.getPackedDate())) {

                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getShippedDate())));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        deliveredDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getCancelledDate())));
                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your has been cancelled");

                        P_S_Progress.setProgress(100);
                        P_S_Progress.setProgress(100);
                        S_D_Progress.setProgress(100);


                    } else {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getCancelledDate())));
                        shippedTitle.setText("Cancelled");
                        shippedBody.setText("Your has been cancelled");

                        P_S_Progress.setProgress(100);
                        P_S_Progress.setProgress(100);

                        S_D_Progress.setVisibility(View.GONE);

                        deliveredIndicator.setVisibility(View.GONE);
                        deliveredBody.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);

                    }
                } else {
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                    orderedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getOrderedDate())));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    packedDate.setText(String.valueOf(simpleDateFormat.format(orderItemModel.getCancelledDate())));
                    packedTitle.setText("Cancelled");
                    packedBody.setText("Your has been cancelled");

                    O_P_Progress.setProgress(100);

                    P_S_Progress.setVisibility(View.GONE);
                    S_D_Progress.setVisibility(View.GONE);

                    shippedIndicator.setVisibility(View.GONE);
                    shippedBody.setVisibility(View.GONE);
                    shippedTitle.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);

                    deliveredIndicator.setVisibility(View.GONE);
                    deliveredBody.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                }

                break;
        }


//            rating layout
        rating = orderItemModel.getRating();
        setRaring(rating);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingDialog.show();
                    setRaring(starPosition);
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(orderItemModel.getProductId());
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);

                            if (rating != 0) {
                                Long increase = documentSnapshot.getLong(starPosition + 1 + " star") + 1;
                                Long decrease = documentSnapshot.getLong(rating + 1 + " star") - 1;
                                transaction.update(documentReference, starPosition + 1 + " star", increase);
                                transaction.update(documentReference, rating + 1 + " star", decrease);
                            } else {
                                Long increase = documentSnapshot.getLong(starPosition + 1 + " star") + 1;
                                transaction.update(documentReference, starPosition + 1 + " star", increase);
                            }
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            Map<String, Object> myRating = new HashMap<>();
                            if (DbQueries.myRatedIds.contains(orderItemModel.getProductId())) {
                                myRating.put("rating " + DbQueries.myRatedIds.indexOf(orderItemModel.getProductId()), (long) starPosition + 1);
                            } else {
                                myRating.put("list size", (long) DbQueries.myRatedIds.size() + 1);
                                myRating.put("product id " + DbQueries.myRatedIds.size(), orderItemModel.getProductId());
                                myRating.put("rating " + DbQueries.myRatedIds.size(), (long) starPosition + 1);
                            }
                            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my ratings")
                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        DbQueries.orderItemModelList.get(position).setRating(starPosition);
                                        if (DbQueries.myRatedIds.contains(orderItemModel.getProductId())) {
                                            DbQueries.myRating.set(DbQueries.myRatedIds.indexOf(orderItemModel.getProductId()), Long.parseLong(String.valueOf(starPosition + 1)));
                                        } else {
                                            DbQueries.myRatedIds.add(orderItemModel.getProductId());
                                            DbQueries.myRating.add(Long.parseLong(String.valueOf(starPosition + 1)));
                                        }

                                    } else {

                                        Toast.makeText(OrderDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
        }
//            rating layout


        if (orderItemModel.isCancellationRequested()) {
            cancelOrderButton.setVisibility(View.VISIBLE);
            cancelOrderButton.setEnabled(false);
            cancelOrderButton.setText("Cancellation is process");
            cancelOrderButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            cancelOrderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        } else {
            if (orderItemModel.getOrderStatus().equals("ordered") || orderItemModel.getOrderStatus().equals("packed")) {
                cancelOrderButton.setVisibility(View.VISIBLE);
                cancelOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancelDialog.findViewById(R.id.no_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialog.dismiss();
                            }
                        });
                        cancelDialog.findViewById(R.id.yes_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialog.dismiss();
                                loadingDialog.show();
                                Map<String, Object> cancelledMap = new HashMap<>();
                                cancelledMap.put("product id", orderItemModel.getProductId());
                                cancelledMap.put("order id", orderItemModel.getOrderId());
                                cancelledMap.put("order cancelled", false);
                                FirebaseFirestore.getInstance().collection("cancelled orders").document().set(cancelledMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseFirestore.getInstance().collection("orders").document(String.valueOf(orderItemModel.getOrderId())).collection("order items").document(orderItemModel.getProductId()).update("cancellation requested", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        orderItemModel.setCancellationRequested(true);
                                                        cancelOrderButton.setEnabled(false);
                                                        cancelOrderButton.setText("Cancellation is process");
                                                        cancelOrderButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                        cancelOrderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                                                    } else {
                                                        Toast.makeText(OrderDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    loadingDialog.dismiss();
                                                }
                                            });
                                        } else {
                                            loadingDialog.dismiss();
                                            Toast.makeText(OrderDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        cancelDialog.show();
                    }
                });
            }
        }


        fullName.setText(orderItemModel.getFullName());
        address.setText(orderItemModel.getAddress());
        pinCode.setText(orderItemModel.getPinCode());

        totalItems.setText("Price(" + orderItemModel.getProductQuantity() + ") items");

        Long totalItemsPriceValue;

        if (orderItemModel.getDiscountedPrice().equals("")) {
            totalItemsPriceValue = orderItemModel.getProductQuantity() * Long.valueOf(orderItemModel.getProductPrice());
            totalItemsPrice.setText("Rs." + totalItemsPriceValue + "/-");
        } else {
            totalItemsPriceValue = orderItemModel.getProductQuantity() * Long.valueOf(orderItemModel.getDiscountedPrice());
            totalItemsPrice.setText("Rs." + totalItemsPriceValue + "/-");
        }
        if (orderItemModel.getDeliveryPrice().equals("FREE")) {
            deliveryPrice.setText(orderItemModel.getDeliveryPrice());
            totalAmount.setText(totalItemsPrice.getText());
        } else {
            deliveryPrice.setText("Rs." + orderItemModel.getDeliveryPrice() + "/-");
            totalAmount.setText("Rs." + (totalItemsPriceValue + Long.valueOf(orderItemModel.getDeliveryPrice())) + "/-");
        }

        if (!orderItemModel.getCuttedPrice().equals("")) {
            if (!orderItemModel.getDiscountedPrice().equals("")) {
                savedAmount.setText("You saved Rs." + Long.valueOf(orderItemModel.getProductQuantity()) * (Long.valueOf(orderItemModel.getCuttedPrice()) - Long.valueOf(orderItemModel.getDiscountedPrice())) + "/- on this order");
            } else {
                savedAmount.setText("You saved Rs." + Long.valueOf(orderItemModel.getProductQuantity()) * (Long.valueOf(orderItemModel.getCuttedPrice()) - Long.valueOf(orderItemModel.getProductPrice())) + "/- on this order");
            }
        } else {
            if (orderItemModel.getDiscountedPrice().equals("")) {
                savedAmount.setText("You saved Rs." + Long.valueOf(orderItemModel.getProductQuantity()) * (Long.valueOf(orderItemModel.getProductPrice()) - Long.valueOf(orderItemModel.getDiscountedPrice())) + " on this order");
            } else {
                savedAmount.setText("You saved Rs.0/- on this order");
            }
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

    private void setRaring(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
            } else {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C5C5C5")));
            }
        }
    }
}
