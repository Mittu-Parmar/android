package com.example.mymall.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymall.R;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.CartItemModel;
import com.example.mymall.model.RewardsModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RewordsAdapter extends RecyclerView.Adapter<RewordsAdapter.ViewHolder> {

    List<RewardsModel> rewardsModelList;
    boolean useMiniLayout = false;
    RecyclerView coupensRecyclerView;
    LinearLayout selectedCoupen;
    private String productOriginalPrice;
    private TextView selectedCoupenTitle;
    private TextView selectedCoupenExpiryDate;
    private TextView selectedCoupenBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List <CartItemModel> cartItemModelList;

    public RewordsAdapter(List<RewardsModel> rewardsModelList, boolean useMiniLayout) {
        this.rewardsModelList = rewardsModelList;
        this.useMiniLayout = useMiniLayout;
    }

    public RewordsAdapter(List<RewardsModel> rewardsModelList, boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice) {
        this.rewardsModelList = rewardsModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
    }

    public RewordsAdapter(int cartItemPosition, List<RewardsModel> rewardsModelList, boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice, List<CartItemModel> cartItemModelList) {
        this.rewardsModelList = rewardsModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartItemModelList=cartItemModelList;
    }

    @NonNull
    @Override
    public RewordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (useMiniLayout) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewords_item_layout, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rewords_item_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RewordsAdapter.ViewHolder holder, int position) {

        String couponId = rewardsModelList.get(position).getCouponId();
        String type = rewardsModelList.get(position).getType();
        Date date = rewardsModelList.get(position).getTimeStamp();
        String body = rewardsModelList.get(position).getBody();
        String lowerLimit = rewardsModelList.get(position).getLowerLimit();
        String upperLimit = rewardsModelList.get(position).getUpperLimit();
        String disORamt = rewardsModelList.get(position).getDisORamt();
        Boolean alreadyUsed = rewardsModelList.get(position).getAlreadyUsed();

        holder.setDetails(couponId, type, date, body, lowerLimit, upperLimit, disORamt, alreadyUsed);

    }

    @Override
    public int getItemCount() {
        return rewardsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.coupenTitle = itemView.findViewById(R.id.rewords_item_coupen_title);
            this.coupenExpiryDate = itemView.findViewById(R.id.rewords_item_coupen_validity);
            this.coupenBody = itemView.findViewById(R.id.rewords_item_coupen_body);
        }

        private void setDetails(final String couponId, final String type, final Date validity, final String body, final String lowerLimit, final String upperLimit, final String disORamt, final Boolean alreadyUsed) {

            if (type.equals("discount")) {
                this.coupenTitle.setText(type);
            } else {
                this.coupenTitle.setText("Flat RS." + disORamt + " OFF");
            }

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
            if (alreadyUsed) {
                this.coupenExpiryDate.setText("Already used");
                this.coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                this.coupenBody.setTextColor(Color.parseColor("#60ffffff"));
                this.coupenTitle.setTextColor(Color.parseColor("#70ffffff"));
            } else {
                this.coupenExpiryDate.setText(" Till " + simpleDateFormat.format(validity));
                this.coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupenPurpal));
                this.coupenBody.setTextColor(Color.parseColor("#ffffff"));
                this.coupenTitle.setTextColor(Color.parseColor("#ffffff"));
            }

            this.coupenBody.setText(body);

            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!alreadyUsed) {
                            selectedCoupenTitle.setText(type);
                            selectedCoupenExpiryDate.setText(simpleDateFormat.format(validity));
                            selectedCoupenBody.setText(body);

                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("discount")) {
                                    long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(disORamt) / 100;
                                    discountedPrice.setText("RS." + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("RS." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(disORamt)) + "/-");
                                }
                                if (cartItemPosition != -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCouponId(couponId);
                                }
                            } else {
                                if (cartItemPosition != -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCouponId(null);
                                }
                                discountedPrice.setText("Invalid");
                                Toast.makeText(itemView.getContext(), "Sorry ! Product does not matches the coupen terms", Toast.LENGTH_SHORT).show();
                            }
                            if (coupensRecyclerView.getVisibility() == View.GONE) {
                                coupensRecyclerView.setVisibility(View.VISIBLE);
                                selectedCoupen.setVisibility(View.GONE);
                            } else {
                                coupensRecyclerView.setVisibility(View.GONE);
                                selectedCoupen.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        }
    }
}
