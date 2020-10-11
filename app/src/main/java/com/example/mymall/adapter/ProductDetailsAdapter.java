package com.example.mymall.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mymall.fragment.ProductDiscriptionFragment;
import com.example.mymall.fragment.ProductSpaeificationFragment;
import com.example.mymall.model.ProductSpecificationModel;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    public String productDescription;
    public String productOtherDetails;
    List<ProductSpecificationModel> productSpecificationModelList;

    public ProductDetailsAdapter(@NonNull FragmentManager fm, int totalTabs, String productDescription, String productOtherDetails, List<ProductSpecificationModel> productSpecificationModelList) {
        super(fm, totalTabs);
        this.totalTabs = totalTabs;
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ProductDiscriptionFragment productDiscriptionFragment1= new ProductDiscriptionFragment();
                productDiscriptionFragment1.body=productDescription;
                return productDiscriptionFragment1;
            case 2:
                ProductDiscriptionFragment productDiscriptionFragment2= new ProductDiscriptionFragment();
                productDiscriptionFragment2.body=productOtherDetails;
                return productDiscriptionFragment2;
            case 1:
                ProductSpaeificationFragment productSpaeificationFragment= new ProductSpaeificationFragment();
                productSpaeificationFragment.productSpecificationModelList=productSpecificationModelList;
                return productSpaeificationFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
