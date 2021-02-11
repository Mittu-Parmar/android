package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.OrderItemModel;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        position = getIntent().getIntExtra("position", -1);
        OrderItemModel orderItemModel = DbQueries.orderItemModelList.get(position);

        title = findViewById(R.id.order_details_product_title);
        quantity = findViewById(R.id.order_details_product_qty);
        price = findViewById(R.id.order_details_product_price);
        productImage = findViewById(R.id.order_details_product_image);

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


        title.setText(orderItemModel.getProductTitle());
        if (orderItemModel.getDiscountedPrice() != null) {
            price.setText(orderItemModel.getDiscountedPrice());
        } else {
            price.setText(orderItemModel.getProductTitle());
        }
        quantity.setText(String.valueOf(orderItemModel.getProductQuantity()));
        Glide.with(this).load(orderItemModel.getProductImage()).into(productImage);

        switch (orderItemModel.getOrderStatus()) {
            case "ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

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
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

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
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(orderItemModel.getShippedDate()));

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
                orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(orderItemModel.getShippedDate()));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(orderItemModel.getDeliveredDate()));

                P_S_Progress.setProgress(100);
                P_S_Progress.setProgress(100);
                S_D_Progress.setProgress(100);

                break;
            case "cancelled":

                if (orderItemModel.getPackedDate().after(orderItemModel.getOrderedDate())) {
                    if (orderItemModel.getShippedDate().after(orderItemModel.getPackedDate())) {

                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        shippedDate.setText(String.valueOf(orderItemModel.getShippedDate()));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        deliveredDate.setText(String.valueOf(orderItemModel.getCancelledDate()));
                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your has been cancelled");

                        P_S_Progress.setProgress(100);
                        P_S_Progress.setProgress(100);
                        S_D_Progress.setProgress(100);


                    } else {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(orderItemModel.getPackedDate()));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        shippedDate.setText(String.valueOf(orderItemModel.getCancelledDate()));
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
                    orderedDate.setText(String.valueOf(orderItemModel.getOrderedDate()));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    packedDate.setText(String.valueOf(orderItemModel.getCancelledDate()));
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
