package com.example.mymall.model;

import java.util.Date;

public class RewardsModel {


    private String type;
    private String lowerLimit;
    private String upperLimit;
    private String disORamt;
    private String body;
    private Date timeStamp;
    private Boolean alreadyUsed;
    private String couponId;

    public RewardsModel(String couponId, String type, String lowerLimit, String upperLimit, String disORamt, String body, Date timeStamp, Boolean alreadyUsed) {
        this.couponId=couponId;
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.disORamt = disORamt;
        this.body = body;
        this.timeStamp = timeStamp;
        this.alreadyUsed=alreadyUsed;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Boolean getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(Boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getDisORamt() {
        return disORamt;
    }

    public void setDisORamt(String disORamt) {
        this.disORamt = disORamt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
