package com.example.mymall.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.model.ProductItemModel;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<ProductItemModel> productItemModelList;

    public GridProductLayoutAdapter(List<ProductItemModel> productItemModelList) {
        this.productItemModelList = productItemModelList;
    }

    @Override
    public int getCount() {
        return productItemModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view;
        if (convertView==null){
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item,null);

            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent=new Intent(parent.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("product id",productItemModelList.get(position).getId());
                    parent.getContext().startActivity(productDetailsIntent);
                }
            });

            ImageView productImage=view.findViewById(R.id.horizontal_scroll_view_product_img);
            TextView productTitle=view.findViewById(R.id.horizontal_scroll_view_product_title);
            TextView productDescription=view.findViewById(R.id.horizontal_scroll_view_product_discription);
            TextView productPrice=view.findViewById(R.id.horizontal_scroll_view_product_price);

            Glide.with(parent.getContext()).load(productItemModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(productImage);
            productTitle.setText(productItemModelList.get(position).getProductTitle());
            productDescription.setText(productItemModelList.get(position).getProductDescription());
            productPrice.setText("Rs."+productItemModelList.get(position).getProductPrice()+"/-");

        }else {
            view=convertView;
        }
        return view;
    }
}
