package com.example.mymall.model;

public class OrderModel {

    int productImage;
    int rating;
    String productTitle;
    String productDeliveryStatus;

    public OrderModel(int productImage, int rating, String productTitle, String productDeliveryStatus) {
        this.productImage = productImage;
        this.rating = rating;
        this.productTitle = productTitle;
        this.productDeliveryStatus = productDeliveryStatus;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDeliveryStatus() {
        return productDeliveryStatus;
    }

    public void setProductDeliveryStatus(String productDeliveryStatus) {
        this.productDeliveryStatus = productDeliveryStatus;
    }
}
