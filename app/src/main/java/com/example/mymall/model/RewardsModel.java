package com.example.mymall.model;

import java.util.Date;

public class RewardsModel {


    private String type;
    private String lowerLimit;
    private String upperLimit;
    private String disORamt;
    private String body;
    private Date date;

    public RewardsModel(String type, String lowerLimit, String upperLimit, String disORamt, String body, Date date) {
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.disORamt = disORamt;
        this.body = body;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
