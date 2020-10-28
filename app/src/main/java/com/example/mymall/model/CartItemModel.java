package com.example.mymall.model;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel {
    public static final int CART_ITEM=0;
    public static final int TOTAL_AMOUNT=1;

    //==========cart item========
    private int type;
    private String productId;
    private String productImage;
    private String productTitle;
    private long freeCoupens;
    private String productPrice;
    private String cuttedPrice;
    private long productQuantity;
    private long productMaxQuantity;
    private long offerceApplied;
    private long coupenApplied;
    private boolean inStock;
    private List<String> qtyIDs;

    public CartItemModel(int type, String productId, String productImage, String productTitle, long freeCoupens, String productPrice, String cuttedPrice, long productQuantity, long offerceApplied, long coupenApplied, boolean inStock,long productMaxQuantity) {
        this.type = type;
        this.productId=productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQuantity = productQuantity;
        this.offerceApplied = offerceApplied;
        this.coupenApplied = coupenApplied;
        this.inStock=inStock;
        this.productMaxQuantity=productMaxQuantity;
        this.qtyIDs=new ArrayList<>();
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public long getProductMaxQuantity() {
        return productMaxQuantity;
    }

    public void setProductMaxQuantity(long productMaxQuantity) {
        this.productMaxQuantity = productMaxQuantity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(int freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        cuttedPrice = cuttedPrice;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public long getOfferceApplied() {
        return offerceApplied;
    }

    public void setOfferceApplied(int offerceApplied) {
        this.offerceApplied = offerceApplied;
    }

    public long getCoupenApplied() {
        return coupenApplied;
    }

    public void setCoupenApplied(int coupenApplied) {
        this.coupenApplied = coupenApplied;
    }
    //==========cart item========


    //==========cart total amount========
    public CartItemModel(int type){
        this.type=type;
    }
    //==========cart total amount========

}
