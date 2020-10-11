package com.example.mymall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymall.R;
import com.example.mymall.model.ProductSpecificationModel;

import java.util.List;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case ProductSpecificationModel.SPECIFICTION_TITLE:
                return ProductSpecificationModel.SPECIFICTION_TITLE;
            case ProductSpecificationModel.SPECIFICTION_BODY:
                return ProductSpecificationModel.SPECIFICTION_BODY;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case ProductSpecificationModel.SPECIFICTION_TITLE:
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                setDp(layoutParams, parent.getContext(), 16, 16, 16, 8);
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);

            case ProductSpecificationModel.SPECIFICTION_BODY:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item_layout, parent, false);
                return new ViewHolder(view);

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {
        switch (productSpecificationModelList.get(position).getType()) {

            case ProductSpecificationModel.SPECIFICTION_TITLE:
                holder.setTitle(productSpecificationModelList.get(position).getTitle());
                break;
            case ProductSpecificationModel.SPECIFICTION_BODY:
                holder.setFeatures(productSpecificationModelList.get(position).getFeatureName(), productSpecificationModelList.get(position).getFeatureValue());
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    private void setDp(LinearLayout.LayoutParams layoutParams, Context context, int left, int top, int right, int bottom) {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, context.getResources().getDisplayMetrics());

        layoutParams.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, context.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, context.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, right, context.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottom, context.getResources().getDisplayMetrics()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView featureName;
        TextView featureValue;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void setTitle(String titleText) {
            title = (TextView) itemView;
            title.setText(titleText);
        }

        public void setFeatures(String featureName, String featureDetail) {
            this.featureName = itemView.findViewById(R.id.feature_name);
            this.featureValue = itemView.findViewById(R.id.feature_value);

            this.featureName.setText(featureName);
            this.featureValue.setText(featureDetail);
        }
    }
}
