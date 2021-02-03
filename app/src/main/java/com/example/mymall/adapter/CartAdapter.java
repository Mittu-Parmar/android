package com.example.mymall.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.CartFragmentActivity;
import com.example.mymall.activity.DeliveryActivity;
import com.example.mymall.activity.MainActivity;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class CartAdapter extends RecyclerView.Adapter {

    List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    Activity activity;
    private TextView cartTotalAmount;
    boolean showDeleteButton;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, Activity activity, boolean showDeleteButton) {
        this.cartItemModelList = cartItemModelList;
        this.activity = activity;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteButton = showDeleteButton;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
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
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:

                String productId = cartItemModelList.get(position).getProductId();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                long freeCoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                long offerceApplied = cartItemModelList.get(position).getOfferceApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                long productQuantity = cartItemModelList.get(position).getProductQuantity();
                long maxQuantity = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                long stockQty = cartItemModelList.get(position).getStockQuantity();


                ((CartItemViewHolder) holder).setCartItemDetails(productId, resource, title, freeCoupens, productPrice, cuttedPrice, offerceApplied, position, inStock, String.valueOf(productQuantity), maxQuantity, qtyError, qtyIds, stockQty);
                break;
            case CartItemModel.TOTAL_AMOUNT:

                int totalItems = 0;
                int totalItemsPrice = 0;
                String deliveryPrice;
                int totalAmount = 0;
                int savedAmount = 0;

                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {
                        totalItems++;

                        totalItemsPrice = totalItemsPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice());
                    }
                }

                if (totalItemsPrice > 500) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemsPrice;
                } else {
                    deliveryPrice = "60";
                    totalAmount = totalItemsPrice + 60;
                }

                ((CartTotalAmountViewHolder) holder).setDetails(totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount);
                break;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fragment_fade_enter);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout removeItemBtn;
        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView productQuantity;
        private TextView offerceApplied;
        private TextView coupenApplied;
        private LinearLayout coupenRedemptionLayout;
        private Button redeemButton;


        //    =============coupen Dialog================
        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;

        private RecyclerView coupensRecyclerView;
        private LinearLayout selectedCoupen;
        private TextView discountedPrice;
        private TextView originalPrice;
        private Button removeCoupenButton, applyCoupenButton;
        private LinearLayout applyOrRemoveButtonContainer;
        private TextView footerText;
        private String productOriginalPrice;
//    =============coupen Dialog================

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.removeItemBtn = itemView.findViewById(R.id.remove_item_btn);
            this.productImage = itemView.findViewById(R.id.cart_product_image);
            this.productTitle = itemView.findViewById(R.id.cart_product_title);
            this.freeCoupens = itemView.findViewById(R.id.cart_free_coupan);
            this.productPrice = itemView.findViewById(R.id.cart_product_price);
            this.cuttedPrice = itemView.findViewById(R.id.cart_cutted_price);
            this.productQuantity = itemView.findViewById(R.id.cart_product_quantity);
            this.offerceApplied = itemView.findViewById(R.id.cart_offer_applied);
            this.coupenApplied = itemView.findViewById(R.id.cart_coupen_applied);
            this.coupenRedemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            this.redeemButton = itemView.findViewById(R.id.coupen_redeemption_button);
        }


        void setCartItemDetails(final String productId, String productImage, String productTitle, long freeCoupens, final String productPriceText, String cuttedPrice, long offerceApplied, final int position, boolean inStock, final String quantity, final long maxQuantity, boolean qtyError, final List<String> qtyIds, final long stockQty) {

            Glide.with(itemView.getContext()).load(productImage).apply(new RequestOptions()).placeholder(R.drawable.place_holder_icon).into(this.productImage);

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

                this.productPrice.setText("Rs." + productPriceText + "/-");
                this.productPrice.setTextColor(Color.parseColor("#000000"));
                this.cuttedPrice.setText("Rs." + cuttedPrice + "/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);

                productQuantity.setText("Qty." + quantity);
                if (!showDeleteButton) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));
                    }
                }
                this.productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_edit_text);
                        Button cancelButton = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okButton = quantityDialog.findViewById(R.id.ok_btn);
                        quantityNo.setHint("Max " + maxQuantity);

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantityNo.getText())) {
                                    if (Long.parseLong(quantityNo.getText().toString()) <= maxQuantity && Long.parseLong(quantityNo.getText().toString()) != 0) {
                                        if (itemView.getContext() instanceof MainActivity || itemView.getContext() instanceof CartFragmentActivity) {
                                            DbQueries.cartItemModelList.get(position).setProductQuantity(Integer.parseInt(quantityNo.getText().toString()));
                                        } else {
                                            if (DeliveryActivity.fromCart) {
                                                DbQueries.cartItemModelList.get(position).setProductQuantity(Integer.parseInt(quantityNo.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Integer.parseInt(quantityNo.getText().toString()));
                                            }
                                        }
                                        productQuantity.setText("Qty." + (quantityNo.getText()));

                                        if (!showDeleteButton) {
                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(false);
                                            final int initialQty = Integer.parseInt(quantity);
                                            final int finalQty = Integer.parseInt(quantityNo.getText().toString());

                                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                            if (finalQty > initialQty) {

                                                for (int y = 0; y < finalQty - initialQty; y++) {
                                                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                    Map<String, Object> timeStamp = new HashMap<>();
                                                    timeStamp.put("time", FieldValue.serverTimestamp());
                                                    final int finalY = y;
                                                    firebaseFirestore.collection("products").document(productId).collection("quantity").document(quantityDocumentName).set(timeStamp)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.add(quantityDocumentName);
                                                                    if (finalY + 1 == finalQty - initialQty) {
                                                                        firebaseFirestore.collection("products").document(productId).collection("quantity").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            List<String> serverQuantity = new ArrayList<>();
                                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                                serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                            }

                                                                                            long availableQty = 0;
                                                                                            for (String qtyId : qtyIds) {

                                                                                                if (!serverQuantity.contains(qtyId)) {
                                                                                                    DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                                    DeliveryActivity.cartItemModelList.get(position).setMaxQuantity(availableQty);
                                                                                                    Toast.makeText(itemView.getContext(), "Sorry !, All products may not be available in required", Toast.LENGTH_SHORT).show();
                                                                                                    DeliveryActivity.allProductAvailable = false;
                                                                                                } else {
                                                                                                    availableQty++;
                                                                                                }
                                                                                            }
                                                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                        } else {
                                                                                            Toast.makeText(itemView.getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            });
                                                }
                                            } else if (initialQty > finalQty) {

                                                for (int x = 0; x < initialQty - finalQty; x++) {
                                                    final String qtyId = qtyIds.get(qtyIds.size() - 1 - x);
                                                    firebaseFirestore.collection("products").document(productId).collection("quantity").document(qtyId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.remove(qtyId);
                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                }
                                                            });
                                                }
                                            }


                                        }
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max Quantity " + maxQuantity, Toast.LENGTH_SHORT).show();
                                    }
                                }
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
            } else {
                this.productPrice.setText("Out of Stock");
                this.productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                this.cuttedPrice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);

                productQuantity.setVisibility(View.INVISIBLE);
                this.freeCoupens.setVisibility(View.INVISIBLE);
                coupenApplied.setVisibility(View.GONE);
                this.offerceApplied.setVisibility(View.GONE);
            }

            if (showDeleteButton) {
                removeItemBtn.setVisibility(View.VISIBLE);
            } else {
                removeItemBtn.setVisibility(View.GONE);
            }

            redeemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //    =============coupen Dialog================
                    final Dialog checkCoupenPriceDialog = new Dialog(itemView.getContext());
                    checkCoupenPriceDialog.setContentView(R.layout.coupem_redeem_dialog);
                    checkCoupenPriceDialog.setCancelable(true);
                    checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    ImageView toggalRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggal_recycler_view);
                    coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recycler_view);
                    selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
                    coupenTitle = checkCoupenPriceDialog.findViewById(R.id.rewords_item_coupen_title);
                    coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.rewords_item_coupen_validity);
                    coupenBody = checkCoupenPriceDialog.findViewById(R.id.rewords_item_coupen_body);

                    removeCoupenButton = checkCoupenPriceDialog.findViewById(R.id.remove_btn);
                    applyCoupenButton = checkCoupenPriceDialog.findViewById(R.id.apply_btn);
                    footerText = checkCoupenPriceDialog.findViewById(R.id.footer_text);
                    applyOrRemoveButtonContainer = checkCoupenPriceDialog.findViewById(R.id.apply_or_remove_buttons_container);

                    footerText.setVisibility(View.GONE);
                    applyOrRemoveButtonContainer.setVisibility(View.VISIBLE);

                    originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
                    discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    coupensRecyclerView.setLayoutManager(layoutManager);

                    //    =============coupen Dialog================
                    originalPrice.setText(productPrice.getText());
                    productOriginalPrice = productPriceText;
                    RewordsAdapter rewordsAdapter = new RewordsAdapter(DbQueries.rewardsModelList, true, coupensRecyclerView, selectedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice);
                    coupensRecyclerView.setAdapter(rewordsAdapter);
                    rewordsAdapter.notifyDataSetChanged();
                    //    =============coupen Dialog================


                    toggalRecyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialogRecyclerView();
                        }
                    });

                    checkCoupenPriceDialog.show();

//    =============coupen Dialog================


                }
            });

            removeItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.runningCartQuery) {
                        ProductDetailsActivity.runningCartQuery = true;
                        DbQueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                        activity.invalidateOptionsMenu();
                    }
                }
            });
        }

        private void showDialogRecyclerView() {

            if (coupensRecyclerView.getVisibility() == View.GONE) {
                coupensRecyclerView.setVisibility(View.VISIBLE);
                selectedCoupen.setVisibility(View.GONE);
            } else {
                coupensRecyclerView.setVisibility(View.GONE);
                selectedCoupen.setVisibility(View.VISIBLE);
            }

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
            this.totalItem.setText("Price(" + totalItemText + " items)");
            this.totalItemPrice.setText("Rs." + totalItemPriceText + "/-");
            if (deleveryPriceText.equals("FREE")) {
                this.deleveryPrice.setText(deleveryPriceText);
            } else {
                this.deleveryPrice.setText("Rs." + deleveryPriceText + "/-");
            }
            this.totalAmount.setText("Rs." + totalAmountText + "/-");
            cartTotalAmount.setText(String.valueOf(totalAmountText));
            this.savedAmount.setText("You saved Rs." + savedAmountText + "/- on this order.");


            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0) {
                if (DeliveryActivity.fromCart) {
                    DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(DbQueries.cartItemModelList.size() - 1);
                }
                if (showDeleteButton) {
                    DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
