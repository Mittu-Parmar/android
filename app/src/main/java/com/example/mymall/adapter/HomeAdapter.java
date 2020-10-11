package com.example.mymall.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.activity.ViewAllActivity;
import com.example.mymall.model.HomeModel;
import com.example.mymall.model.ProductItemModel;
import com.example.mymall.model.SliderModel;
import com.example.mymall.model.WishListModel;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter {

    private List<HomeModel> homeModelList;
    RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition=-1;

    public HomeAdapter(List<HomeModel> homeModelList) {
        this.homeModelList = homeModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homeModelList.get(position).getType()) {
            case HomeModel.BANNER_SLIDER:
                return HomeModel.BANNER_SLIDER;
            case HomeModel.STRIP_AD:
                return HomeModel.STRIP_AD;
            case HomeModel.HORIZONTAL_PRODUCT_VIEW:
                return HomeModel.HORIZONTAL_PRODUCT_VIEW;
            case HomeModel.GRID_PRODUCT_VIEW:
                return HomeModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HomeModel.BANNER_SLIDER:
                View bannerSlider = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewHolder(bannerSlider);
            case HomeModel.STRIP_AD:
                View stripAD = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdViewHolder(stripAD);
            case HomeModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewHolder(horizontalProductView);
            case HomeModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewHolder(gridProductView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        List<SliderModel> sliderModelList;
        switch (homeModelList.get(position).getType()) {
            case HomeModel.BANNER_SLIDER:
                sliderModelList = homeModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case HomeModel.STRIP_AD:
                sliderModelList = homeModelList.get(position).getSliderModelList();
                ((StripAdViewHolder) holder).setStripAdImageView(homeModelList.get(position).getResource());
                break;
            case HomeModel.HORIZONTAL_PRODUCT_VIEW:
                String horizontalLayoutTitle = homeModelList.get(position).getTitle();
                List<ProductItemModel> horizontalProductItemModelList = homeModelList.get(position).getProductItemModelList();
                List<WishListModel> viewAllProductsList=homeModelList.get(position).getViewAllProductModelList();
                ((HorizontalProductViewHolder) holder).setHorizontalProductLayout(horizontalLayoutTitle, horizontalProductItemModelList,viewAllProductsList);
                break;
            case HomeModel.GRID_PRODUCT_VIEW:
                String gridLayoutTitle = homeModelList.get(position).getTitle();
                List<ProductItemModel> gridProductItemModelList = homeModelList.get(position).getProductItemModelList();
                ((GridProductViewHolder) holder).setGridProductLayout(gridLayoutTitle, gridProductItemModelList);
                break;
            default:
                return;
        }
        if (lastPosition < position){
            Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fragment_fade_enter);
            holder.itemView.setAnimation(animation);
            lastPosition=position;
        }
    }

    @Override
    public int getItemCount() {
        return homeModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        ViewPager viewPager;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.banner_slider_view_pager);
        }

        private void setBannerSliderViewPager(final List<SliderModel> sliderModelList) {

            viewPager.setAdapter(new SliderAdapter(sliderModelList));
        }
    }

    public class StripAdViewHolder extends RecyclerView.ViewHolder {

        ImageView stripAdImageView;

        public StripAdViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdImageView = itemView.findViewById(R.id.strip_ad_img);
            stripAdImageView.setImageResource(R.drawable.banner_slider_img);
        }

        public void setStripAdImageView(String url) {
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(stripAdImageView);
        }
    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder {

        RecyclerView horizontalRecyclerView;
        TextView horizontalLayoutTitle;
        Button horizontalLayoutButton;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);

            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recycler_view);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutButton = itemView.findViewById(R.id.horizontal_scroll_layout_button);
        }

        private void setHorizontalProductLayout(final String title, List<ProductItemModel> productItemModelList, final List<WishListModel> viewAllProductsList) {
            horizontalLayoutTitle.setText(title);
            if (productItemModelList.size() >= 8) {
                horizontalLayoutButton.setVisibility(View.VISIBLE);
            } else {
                horizontalLayoutButton.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            ProductItemAdapter productItemAdapter = new ProductItemAdapter(productItemModelList);
            horizontalRecyclerView.setAdapter(productItemAdapter);
            productItemAdapter.notifyDataSetChanged();

            horizontalLayoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.wishListModelList =viewAllProductsList;
                    Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra("Layout_code", 0);
                    viewAllIntent.putExtra("title", title);
                    itemView.getContext().startActivity(viewAllIntent);
                }
            });
        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder {

        GridLayout gridLayout;
        TextView gridLayoytTitle;
        Button gridLayoytButton;


        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);

            gridLayout = itemView.findViewById(R.id.grid_product_layout_grid_Layout);
            gridLayoytTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoytButton = itemView.findViewById(R.id.grid_product_layout_view_all_button);
        }

        private void setGridProductLayout(final String title, final List<ProductItemModel> productItemModelList) {
            gridLayoytTitle.setText(title);

            for (int x = 0; x < 4; x++) {
                ImageView productImage = gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_view_product_img);
                TextView productTitle = gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_view_product_title);
                TextView productDiscription = gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_view_product_discription);
                TextView productPrice = gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_view_product_price);

                Glide.with(itemView.getContext()).load(productItemModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(productImage);
                productTitle.setText(productItemModelList.get(x).getProductTitle());
                productDiscription.setText(productItemModelList.get(x).getProductDescription());
                productPrice.setText("Rs." + productItemModelList.get(x).getProductPrice() + "/-");

                if (!title.equals("")){
                    final int finalX = x;
                    gridLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailsIntent.putExtra("Layout_code", 1);
                            productDetailsIntent.putExtra("product id", productItemModelList.get(finalX).getId());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }
            }

            if (!title.equals("")) {
                gridLayoytButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.productItemModelList = productItemModelList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("Layout_code", 1);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            }
        }
    }
}
