package com.example.mymall.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.example.mymall.R.*;

public class ProductImagesAdapter extends PagerAdapter {

    List<String> imageResourceList;

    public ProductImagesAdapter(List<String> imageResourceList) {
        this.imageResourceList = imageResourceList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView productImageView=new ImageView(container.getContext());
        Glide.with(container.getContext()).load(imageResourceList.get(position)).apply(new RequestOptions().placeholder(drawable.place_holder_icon)).into(productImageView);

        container.addView(productImageView,0);
        return productImageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public int getCount() {
        return imageResourceList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
