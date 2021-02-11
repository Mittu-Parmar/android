package com.example.mymall.model;

import java.util.Date;

public class OrderItemModel {

    private String productId;
    private String productImage;
    private String productTitle;

    private String orderStatus;
    private String address;
    private String couponId;
    private String cuttedPrice;
    private Date orderedDate;
    private Date packedDate;
    private Date ShippedDate;
    private Date DeliveredDate;
    private Date cancelledDate;
    private String discountedPrice;
    private Long freeCoupons;
    private String fullName;
    private String orderId;
    private String paymentMethod;
    private String pinCode;
    private String productPrice;
    private Long productQuantity;
    private String userId;
    private String deliveryPrice;

    private int rating=0;

    public OrderItemModel(String productId, String orderStatus, String address, String couponId, String cuttedPrice, Date orderedDate, Date packedDate, Date shippedDate, Date deliveredDate, Date cancelledDate, String discountedPrice, Long freeCoupons, String fullName, String orderId, String paymentMethod, String pinCode, String productPrice, Long productQuantity, String userId, String productImage, String productTitle,String deliveryPrice) {
        this.productId = productId;
        this.orderStatus = orderStatus;
        this.address = address;
        this.couponId = couponId;
        this.cuttedPrice = cuttedPrice;
        this.orderedDate = orderedDate;
        this.packedDate = packedDate;
        ShippedDate = shippedDate;
        DeliveredDate = deliveredDate;
        this.cancelledDate = cancelledDate;
        this.discountedPrice = discountedPrice;
        this.freeCoupons = freeCoupons;
        this.fullName = fullName;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.pinCode = pinCode;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.userId = userId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.deliveryPrice=deliveryPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShippedDate() {
        return ShippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        ShippedDate = shippedDate;
    }

    public Date getDeliveredDate() {
        return DeliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        DeliveredDate = deliveredDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(Long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
