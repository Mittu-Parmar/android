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

import com.example.mymall.R;
import com.example.mymall.activity.OrderDetailsActivity;
import com.example.mymall.model.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<OrderModel> orderModelList;

    public OrderAdapter(List<OrderModel> orderModelList) {
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        holder.setDeta(orderModelList.get(position).getProductImage(), orderModelList.get(position).getProductTitle(), orderModelList.get(position).getProductDeliveryStatus(), orderModelList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
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
            rateNowContainer=itemView.findViewById(R.id.rate_now_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailsIntent=new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });
        }

        private void setDeta(int productImage, String productTitle, String productDeliveryStatus,int rating) {
            this.productImage.setImageResource(productImage);
            if (productDeliveryStatus.equals("cancelled"))
            {
                this.productDeliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            }
            else {
                this.productDeliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
            }
            this.productTitle.setText(productTitle);
            this.productDeliveryStatus.setText(productDeliveryStatus);
            this.rating=rating;

            setRaring(rating);
            for (int x=0;x<rateNowContainer.getChildCount();x++){
                final int starPosition=x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRaring(starPosition);
                    }
                });
            }
        }

        private void setRaring(int starPosition) {
            for (int x=0;x<rateNowContainer.getChildCount();x++){
                ImageView starBtn= (ImageView) rateNowContainer.getChildAt(x);
                if (x <= starPosition){
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                }else {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C5C5C5")));
                }
            }
        }
    }
}
