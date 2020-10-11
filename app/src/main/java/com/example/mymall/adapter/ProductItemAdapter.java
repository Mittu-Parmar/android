package com.example.mymall.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.model.ProductItemModel;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {

    List<ProductItemModel> productItemModelsList;

    public ProductItemAdapter(List<ProductItemModel> productItemModels) {
        this.productItemModelsList = productItemModels;
    }

    @NonNull
    @Override
    public ProductItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item,parent,false);
        return new ProductItemAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemAdapter.ViewHolder holder, int position) {
        holder.setProductDetails(productItemModelsList.get(position).getId(),productItemModelsList.get(position).getProductImage(),productItemModelsList.get(position).getProductTitle(),productItemModelsList.get(position).getProductDescription(),productItemModelsList.get(position).getProductPrice());
    }

    @Override
    public int getItemCount() {
        if (productItemModelsList.size()>=8){
            return 8;
        }else
            return productItemModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle;
        TextView productDescription;
        TextView productPrice;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.horizontal_scroll_view_product_img);
            productTitle=itemView.findViewById(R.id.horizontal_scroll_view_product_title);
            productDescription=itemView.findViewById(R.id.horizontal_scroll_view_product_discription);
            productPrice=itemView.findViewById(R.id.horizontal_scroll_view_product_price);

        }

        public void setProductDetails(final String productId, String url, String productTitle, String productDescription, String productPrice){
            Glide.with(itemView.getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(productImage);
            this.productTitle.setText(productTitle);
            this.productDescription.setText(productDescription);
            this.productPrice.setText("Rs."+productPrice+"/-");

            if (!productTitle.equals(""))
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent=new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("product id",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }
    }
}
