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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.example.mymall.activity.OrderDetailsActivity;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.OrderItemModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

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
        String productId = orderItemModelList.get(position).getProductId();
        int rating = orderItemModelList.get(position).getRating();
        String title = orderItemModelList.get(position).getProductTitle();
        String orderStatus = orderItemModelList.get(position).getOrderStatus();
        Date date;
        switch (orderStatus) {
            case "ordered":
                date = orderItemModelList.get(position).getOrderedDate();
                break;
            case "packed":
                date = orderItemModelList.get(position).getPackedDate();
                break;
            case "shipped":
                date = orderItemModelList.get(position).getShippedDate();
                break;
            case "delivered":
                date = orderItemModelList.get(position).getDeliveredDate();
                break;
            case "cancelled":
                date = orderItemModelList.get(position).getCancelledDate();
                break;
            default:
                date = orderItemModelList.get(position).getCancelledDate();

        }

        holder.setDeta(Image, title, orderStatus, date, rating, productId,position);
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
            productTitle = itemView.findViewById(R.id.order_product_title);
            productDeliveryStatus = itemView.findViewById(R.id.order_deleverd_date);
            rateNowContainer = itemView.findViewById(R.id.rate_now_container);
        }

        private void setDeta(String image, String title, String orderStatus, Date date, final int rating, final String productId, final int position) {
            Glide.with(itemView.getContext()).load(image).into(this.productImage);
            this.productTitle.setText(title);

            if (orderStatus.equals("cancelled")) {
                this.productDeliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            } else {
                this.productDeliveryIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
            }

            this.productDeliveryStatus.setText(orderStatus +" "+ (date));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    orderDetailsIntent.putExtra("position",position);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });


            setRaring(rating);
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRaring(starPosition);
                        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
                        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                            @Nullable
                            @Override
                            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                DocumentSnapshot documentSnapshot = transaction.get(documentReference);

                                if (rating != 0) {
                                    Long increase = documentSnapshot.getLong(starPosition + " star") + 1;
                                    Long decrease = documentSnapshot.getLong(rating + " star") - 1;
                                    transaction.update(documentReference,starPosition + " star",increase);
                                    transaction.update(documentReference,rating + " star",decrease);
                                } else {
                                    Long increase = documentSnapshot.getLong(starPosition + " star") + 1;
                                    transaction.update(documentReference,starPosition + " star",increase);
                                }
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                DbQueries.orderItemModelList.get(position).setRating(starPosition);
                                if (DbQueries.myRatedIds.contains(productId)){
                                    DbQueries.myRating.set(DbQueries.myRatedIds.indexOf(productId),Long.parseLong(String.valueOf(starPosition)));
                                }else {
                                    DbQueries.myRatedIds.add(productId);
                                    DbQueries.myRating.add(Long.parseLong(String.valueOf(starPosition)));
                                }
                            }
                        });
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
