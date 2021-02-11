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
    private long MaxQuantity;
    private long stockQuantity;
    private long offerceApplied;
    private long coupenApplied;
    private boolean inStock;
    private List<String> qtyIDs;
    private boolean qtyError;
    private String selectedCouponId;
    private String discountedPrice;
    private boolean cod;

    public CartItemModel(int type, String productId, String productImage, String productTitle, long freeCoupens, String productPrice, String cuttedPrice, long productQuantity, long offerceApplied, long coupenApplied, boolean inStock,long MaxQuantity, long stockQuantity,boolean cod) {
        this.type = type;
        this.productId=productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQuantity = productQuantity;
        this.stockQuantity=stockQuantity;
        this.offerceApplied = offerceApplied;
        this.coupenApplied = coupenApplied;
        this.inStock=inStock;
        this.MaxQuantity = MaxQuantity;
        this.qtyIDs=new ArrayList<>();
        this.qtyError=qtyError;
        this.cod=cod;

    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getSelectedCouponId() {
        return selectedCouponId;
    }

    public void setSelectedCouponId(String selectedCouponId) {
        this.selectedCouponId = selectedCouponId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public long getMaxQuantity() {
        return MaxQuantity;
    }

    public void setMaxQuantity(long maxQuantity) {
        this.MaxQuantity = maxQuantity;
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
    private int totalItems;
    private int totalItemsPrice;
    private String deliveryPrice;
    private int totalAmount;
    private int savedAmount;

    public CartItemModel(int type){
        this.type=type;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public void setTotalItemsPrice(int totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    //==========cart total amount========

}
