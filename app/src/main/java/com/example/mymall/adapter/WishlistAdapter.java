package com.example.mymall.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.WishListModel;

import java.util.List;

import static com.example.mymall.activity.ProductDetailsActivity.addToWishlistButton;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private boolean fromSearch;
    List<WishListModel> wishListModelList;
    boolean isFromViewAll = false;
    private int lastPosition = -1;


    public List<WishListModel> getWishListModelList() {
        return wishListModelList;
    }

    public void setWishListModelList(List<WishListModel> wishListModelList) {
        this.wishListModelList = wishListModelList;
    }

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishlistAdapter(List<WishListModel> wishListModelList, boolean isFromViewAll) {
        this.wishListModelList = wishListModelList;
        this.isFromViewAll = isFromViewAll;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        holder.setDetails(position, wishListModelList.get(position).getProductId(), wishListModelList.get(position).getImage(), wishListModelList.get(position).getProductTitle(), wishListModelList.get(position).getFreeCoupens(), wishListModelList.get(position).getRating(), wishListModelList.get(position).getTotalRatings(), wishListModelList.get(position).getProductPrice(), wishListModelList.get(position).getCuttedPrice(), wishListModelList.get(position).isCod(), wishListModelList.get(position).isInStock());

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fragment_fade_enter);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String productId;
        private ImageView image;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView rating;
        private TextView totalRatings;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageButton deleteImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.image = itemView.findViewById(R.id.wishlist_item_image);
            this.productTitle = itemView.findViewById(R.id.wishlist_item_title);
            this.freeCoupens = itemView.findViewById(R.id.wishlist_item_free_coupen);
            this.rating = itemView.findViewById(R.id.wishlist_item_star_rating);
            this.totalRatings = itemView.findViewById(R.id.wishlist_item_total_rating);
            this.productPrice = itemView.findViewById(R.id.wishlist_item_price);
            this.cuttedPrice = itemView.findViewById(R.id.wishlist_item_cutted_price);
            this.paymentMethod = itemView.findViewById(R.id.wishlist_item_cash_on_delevery);
            this.deleteImageButton = itemView.findViewById(R.id.wishlist_item_delete_btn);

        }

        private void setDetails(final int index, final String productId, String image, String productTitle, long freeCoupens, String rating, long totalRatings, String productPrice, String cuttedPrice, boolean paymentMethod, boolean inStock) {
            Glide.with(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(this.image);
            this.productTitle.setText(productTitle);
            if (freeCoupens != 0 && inStock) {
                this.freeCoupens.setVisibility(View.VISIBLE);
                if (freeCoupens == 1) {
                    this.freeCoupens.setText("Free " + freeCoupens + " Coupen");
                } else {
                    this.freeCoupens.setText("Free " + freeCoupens + " Coupens");
                }
            } else {
                this.freeCoupens.setVisibility(View.INVISIBLE);
            }
            LinearLayout ratingLayout = (LinearLayout) this.rating.getParent();
            if (inStock) {

                ratingLayout.setVisibility(View.VISIBLE);
                this.rating.setVisibility(View.VISIBLE);
                this.totalRatings.setVisibility(View.VISIBLE);
                this.productPrice.setTextColor(Color.parseColor("#000000"));
                this.cuttedPrice.setVisibility(View.VISIBLE);

                this.rating.setText(rating);
                this.totalRatings.setText("Total ratings (" + totalRatings + ")");
                this.productPrice.setText(productPrice);
                this.cuttedPrice.setText(cuttedPrice);
                if (paymentMethod) {
                    this.paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    this.paymentMethod.setVisibility(View.INVISIBLE);
                }
            } else {
                ratingLayout.setVisibility(View.INVISIBLE);
                this.rating.setVisibility(View.INVISIBLE);
                this.totalRatings.setVisibility(View.INVISIBLE);
                this.productPrice.setText("Out of stock");
                this.productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                this.cuttedPrice.setVisibility(View.INVISIBLE);
                this.paymentMethod.setVisibility(View.INVISIBLE);
            }

            if (!isFromViewAll) {
                deleteImageButton.setVisibility(View.VISIBLE);
            } else {
                deleteImageButton.setVisibility(View.GONE);
            }

            deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.runningWishlistQuery) {
                        ProductDetailsActivity.runningWishlistQuery = true;
                        deleteImageButton.setEnabled(false);
                        DbQueries.removeFromWishList(index, itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fromSearch) {
                        ProductDetailsActivity.fromSearch = true;
                    }
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("product id", productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }
    }
}
