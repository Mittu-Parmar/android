package com.example.mymall.model;

import java.util.List;

public class HomeModel {
    public static final int BANNER_SLIDER=0;
    public static final int STRIP_AD=1;
    public static final int HORIZONTAL_PRODUCT_VIEW=2;
    public static final int GRID_PRODUCT_VIEW=3;

    private int type;

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    //    ==============banner slide============
    List<SliderModel> sliderModelList;
    public HomeModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    //    ==============banner slide============

    //    ==============strip ad============
    private String resource;
    public HomeModel(int type, String resource) {
        this.type = type;
        this.resource = resource;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    //    ==============strip ad============

    //    ==============hoorizontal and grid product view============
    String title;
    List<ProductItemModel> productItemModelList;
    List<WishListModel>viewAllProductModelList;

    public HomeModel(int type, String title, List<ProductItemModel> productItemModelList,List<WishListModel>viewAllProductModelList) {
        this.type = type;
        this.title = title;
        this.productItemModelList = productItemModelList;
        this.viewAllProductModelList= viewAllProductModelList;
    }

    public HomeModel(int type, String title, List<ProductItemModel> productItemModelList) {
        this.type = type;
        this.title = title;
        this.productItemModelList = productItemModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProductItemModel> getProductItemModelList() {
        return productItemModelList;
    }

    public void setProductItemModelList(List<ProductItemModel> productItemModelList) {
        this.productItemModelList = productItemModelList;
    }

    public List<WishListModel> getViewAllProductModelList() {
        return viewAllProductModelList;
    }

    public void setViewAllProductModelList(List<WishListModel> viewAllProductModelList) {
        this.viewAllProductModelList = viewAllProductModelList;
    }
    //    ==============hoorizontal and grid product view============

}









