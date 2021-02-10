package com.example.mymall.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.example.mymall.activity.OrderDetailsActivity;
import com.example.mymall.model.OrderItemModel;

import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<OrderItemModel> orderItemModelList;

    public OrderAdapter(List<OrderItemModel> orderItemModelList) {
        this.orderItemModelList = orderItemModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {


        String Image = orderItemModelList.get(position).getProductImage();
        //int rating  = orderItemModelList.get(position);
        String title = orderItemModelList.get(position).getProductTitle();
        String orderStatus = orderItemModelList.get(position).getOrderStatus();
        Date date;
        switch (orderStatus) {
            case "ordered":
                date = orderItemModelList.get(position).getOrderedDate();
            case "packed":
                date = orderItemModelList.get(position).getPackedDate();
            case "shopped":
                date = orderItemModelList.get(position).getShippedDate();
            case "delivered":
                date = orderItemModelList.get(position).getDeliveredDate();
            case "cancelled":
                date = orderItemModelList.get(position).getCancelledDate();
            default:
                date = orderItemModelList.get(position).getCancelledDate();
                break;
        }

        holder.setDeta(Image, title, orderStatus, date);
    }

    @Override
    public int getItemCount() {
        return orderItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView productDeliveryIndicator;
        private TextView productTitle;
        private TextView productDeliveryStatus;
        private LinearLayout rateNowContainer;
        private int rating;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productDeliveryIndicator = itemView.findViewById(R.id.order_indicator);
            productTitle = itemView.findViewById(R.id.cart_product_title);
            productDeliveryStatus = itemView.findViewById(R.id.order_deleverd_date);
            rateNowContainer = itemView.findViewById(R.id.rate_now_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });
        }

        private void setDeta(String image, String title, String orderStatus, Date date) {
            Glide.with(itemView.getContext()).load(image).into(this.productImage);
            this.productTitle.setText(title);

            if (orderStatus.equals("cancelled")) {
                this.productDeliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            } else {
                this.productDeliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
            }

            this.productDeliveryStatus.setText(orderStatus + String.valueOf(date));

//            setRaring(rating);
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRaring(starPosition);
                    }
                });
            }
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
}
