package com.example.mymall.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.adapter.ProductSpecificationAdapter;
import com.example.mymall.model.ProductSpecificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductSpaeificationFragment extends Fragment {

    RecyclerView productSpecificationRecyclerView;
    public List<ProductSpecificationModel> productSpecificationModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_spaeification, container, false);
        productSpecificationRecyclerView=view.findViewById(R.id.product_specification_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        productSpecificationRecyclerView.setLayoutManager(linearLayoutManager);

        ProductSpecificationAdapter productSpecificationAdapter=new ProductSpecificationAdapter(productSpecificationModelList);
        productSpecificationRecyclerView.setAdapter(productSpecificationAdapter);
        productSpecificationAdapter.notifyDataSetChanged();

        return view;
    }
}
