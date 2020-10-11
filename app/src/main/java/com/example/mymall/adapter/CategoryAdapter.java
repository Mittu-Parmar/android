package com.example.mymall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.CategoryActivity;
import com.example.mymall.model.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CategoryModel> categoryModelList;
    private int lastPosition=-1;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String icon = categoryModelList.get(position).getCategoryIconLink();
        String name = categoryModelList.get(position).getCategoryName();
        holder.setCategoryIcon(icon);
        holder.setCategory(name,position);

        if (lastPosition < position){
            Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fragment_fade_enter);
            holder.itemView.setAnimation(animation);
            lastPosition=position;
        }
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryIcon;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon=itemView.findViewById(R.id.category_icon);
            categoryName=itemView.findViewById(R.id.category_name);
        }

        public void setCategoryIcon(String categoryIcon) {
                Glide.with(itemView.getContext()).load(categoryIcon).apply(new RequestOptions().placeholder(R.drawable.home_icon)).into(this.categoryIcon);
        }

        public void setCategory(final String categoryName, final int position) {
            this.categoryName.setText(categoryName);

            if (!categoryName.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != 0) {
                            Intent categoryIntent = new Intent(itemView.getContext(), CategoryActivity.class);
                            categoryIntent.putExtra("categoryName", categoryName);
                            itemView.getContext().startActivity(categoryIntent);
                        }
                    }
                });
            }
        }
    }
}
