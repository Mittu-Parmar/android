package com.example.mymall.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.fragment.CartFragment;
import com.example.mymall.model.CartItemModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    List<CartItemModel> cartModelList;
    private int lastPosition=-1;
    Activity activity;
    private TextView cartTotalAmount;
    boolean showDeleteButton;

    public CartAdapter(List<CartItemModel> cartModelList,TextView cartTotalAmount,Activity activity,boolean showDeleteButton) {
        this.cartModelList = cartModelList;
        this.activity=activity;
        this.cartTotalAmount=cartTotalAmount;
        this.showDeleteButton=showDeleteButton;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                return CartItemModel.CART_ITEM;
            case CartItemModel.TOTAL_AMOUNT:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(cartItemLayout);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalAmountLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewHolder(cartTotalAmountLayout);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                ((CartItemViewHolder) holder).setCartItemDetails(position,cartModelList.get(position).getProductId(),cartModelList.get(position).getProductImage(), cartModelList.get(position).getProductTitle(), cartModelList.get(position).getFreeCoupens(), cartModelList.get(position).getProductPrice(), cartModelList.get(position).getCuttedPrice(), cartModelList.get(position).getOfferceApplied(),cartModelList.get(position).isInStock());
                break;
            case CartItemModel.TOTAL_AMOUNT:

                int totalItems=0;
                int totalItemsPrice=0;
                String deliveryPrice;
                int totalAmount=0;
                int savedAmount=0;

                for (int x = 0; x < cartModelList.size() ; x++) {
                    if (cartModelList.get(x).getType()==CartItemModel.CART_ITEM && cartModelList.get(x).isInStock()){
                        totalItems++;
                        totalItemsPrice=totalItemsPrice + Integer.parseInt(cartModelList.get(x).getProductPrice());
                    }
                }
                if (totalItemsPrice>500){
                    deliveryPrice="FREE";
                    totalAmount=totalItemsPrice;
                }else {
                    deliveryPrice="60";
                    totalAmount=totalItemsPrice+60;
                }


                ((CartTotalAmountViewHolder) holder).setDetails(totalItems, totalItemsPrice, deliveryPrice, totalAmount,savedAmount);
                break;
        }
        if (lastPosition > position){
            Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fragment_fade_enter);
            holder.itemView.setAnimation(animation);
            lastPosition=position;
        }
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout removeItemBtn;
        ImageView productImage;
        TextView productTitle;
        TextView freeCoupens;
        TextView productPrice;
        TextView cuttedPrice;
        TextView productQuantity;
        TextView offerceApplied;
        TextView coupenApplied;
        LinearLayout coupenRedemptionLayout;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.removeItemBtn=itemView.findViewById(R.id.remove_item_btn);
            this.productImage = itemView.findViewById(R.id.cart_product_image);
            this.productTitle = itemView.findViewById(R.id.cart_product_title);
            this.freeCoupens = itemView.findViewById(R.id.cart_free_coupan);
            this.productPrice = itemView.findViewById(R.id.cart_product_price);
            this.cuttedPrice = itemView.findViewById(R.id.cart_cutted_price);
            this.productQuantity = itemView.findViewById(R.id.cart_product_quantity);
            this.offerceApplied = itemView.findViewById(R.id.cart_offer_applied);
            this.coupenApplied = itemView.findViewById(R.id.cart_coupen_applied);
            this.coupenRedemptionLayout=itemView.findViewById(R.id.coupen_redemption_layout);
        }

        void setCartItemDetails(final int index, String productId, String productImage, String productTitle, Long freeCoupens, String productPrice, String cuttedPrice, Long offerceApplied, boolean inStock) {


            Glide.with(itemView.getContext()).load(productImage).apply(new RequestOptions()).placeholder(R.drawable.place_holder_icon);

            this.productTitle.setText(productTitle);

            if (inStock) {
                if (freeCoupens > 0) {
                    this.freeCoupens.setVisibility(View.VISIBLE);
                    if (freeCoupens == 1) {
                        this.freeCoupens.setText("Free " + freeCoupens + " Coupen");
                    } else {
                        this.freeCoupens.setText("Free " + freeCoupens + " Coupens");
                    }
                } else {
                    this.freeCoupens.setVisibility(View.INVISIBLE);
                }

                this.productPrice.setText("Rs."+productPrice+"/-");
                this.productPrice.setTextColor(Color.parseColor("#000000"));
                this.cuttedPrice.setText("Rs."+cuttedPrice+"/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);

                this.productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);

                        Button cancelButton = quantityDialog.findViewById(R.id.cancel_btn);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });
                        Button okButton = quantityDialog.findViewById(R.id.ok_btn);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productQuantity.setText("Qty." + ((EditText) quantityDialog.findViewById(R.id.quantity_edit_text)).getText());
                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });

                if (offerceApplied > 0) {
                    this.offerceApplied.setVisibility(View.VISIBLE);
                    this.offerceApplied.setText(offerceApplied + " Offers Applied");
                } else {
                    this.offerceApplied.setVisibility(View.INVISIBLE);
                }
            }else {
                this.productPrice.setText("Out of Stock");
                this.productPrice.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                this.cuttedPrice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);

                productQuantity.setVisibility(View.INVISIBLE);
                this.freeCoupens.setVisibility(View.INVISIBLE);
                coupenApplied.setVisibility(View.GONE);
                this.offerceApplied.setVisibility(View.GONE);
            }

            if (showDeleteButton){
                removeItemBtn.setVisibility(View.VISIBLE);
            }else {
                removeItemBtn.setVisibility(View.GONE);
            }

            removeItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.runningCartQuery){
                        ProductDetailsActivity.runningCartQuery=true;
                        DbQueries.removeFromCart(index,itemView.getContext(),cartTotalAmount);
                        activity.invalidateOptionsMenu();
                    }
                }
            });


            //this.coupenApplied.setText(coupenApplied);
        }

    }


    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {

        TextView totalAmount;
        TextView deleveryPrice;
        TextView totalItem;
        TextView savedAmount;
        TextView totalItemPrice;

        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItem = itemView.findViewById(R.id.total_items);
            totalAmount = itemView.findViewById(R.id.total_price);
            deleveryPrice = itemView.findViewById(R.id.delivery_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
        }

        void setDetails(int totalItemText, int totalItemPriceText, String deleveryPriceText, int totalAmountText, int savedAmountText) {
            this.totalItem.setText("Price("+totalItemText+" items)");
            this.totalItemPrice.setText("Rs."+totalItemPriceText+"/-");
            if (deleveryPriceText.equals("FREE")){
                this.deleveryPrice.setText(deleveryPriceText);
            }else {
                this.deleveryPrice.setText("Rs."+deleveryPriceText+"/-");
            }
            this.totalAmount.setText("Rs."+totalAmountText+"/-");
            this.savedAmount.setText("You saved Rs." + savedAmountText + "/- on this order.");


            LinearLayout parent= (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText==0){
                DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
