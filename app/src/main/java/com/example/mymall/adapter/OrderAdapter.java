package com.example.mymall.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.example.mymall.activity.OrderDetailsActivity;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<OrderItemModel> orderItemModelList;
    private Dialog loadingDialog;

    public OrderAdapter(List<OrderItemModel> orderItemModelList, Dialog loadingDialog) {
        this.orderItemModelList = orderItemModelList;
        this.loadingDialog=loadingDialog;
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

        holder.setDeta(Image, title, orderStatus, date, rating, productId, position);
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
        private SimpleDateFormat simpleDateFormat;

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

            simpleDateFormat=new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");

            this.productDeliveryStatus.setText(orderStatus + " " + simpleDateFormat.format(date));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    orderDetailsIntent.putExtra("position", position);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });


//            rating layout
            setRaring(rating);
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        setRaring(starPosition);
                        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("products").document(productId);
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
                                if (DbQueries.myRatedIds.contains(productId)) {
                                    myRating.put("rating " + DbQueries.myRatedIds.indexOf(productId), (long) starPosition + 1);
                                } else {
                                    myRating.put("list size", (long) DbQueries.myRatedIds.size() + 1);
                                    myRating.put("product id " + DbQueries.myRatedIds.size(), productId);
                                    myRating.put("rating " + DbQueries.myRatedIds.size(), (long) starPosition + 1);
                                }
                                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my ratings")
                                        .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            DbQueries.orderItemModelList.get(position).setRating(starPosition);
                                            if (DbQueries.myRatedIds.contains(productId)) {
                                                DbQueries.myRating.set(DbQueries.myRatedIds.indexOf(productId), Long.parseLong(String.valueOf(starPosition + 1)));
                                            } else {
                                                DbQueries.myRatedIds.add(productId);
                                                DbQueries.myRating.add(Long.parseLong(String.valueOf(starPosition + 1)));
                                            }

                                        } else {

                                            Toast.makeText(itemView.getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
