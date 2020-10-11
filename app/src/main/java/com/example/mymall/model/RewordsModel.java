package com.example.mymall.model;

public class RewordsModel {

    String rewordsTitle;
    String rewordsValidity;
    String rewordsBody;

    public RewordsModel(String rewordsTitle, String rewordsValidity, String rewordsBody) {
        this.rewordsTitle = rewordsTitle;
        this.rewordsValidity = rewordsValidity;
        this.rewordsBody = rewordsBody;
    }

    public String getRewordsTitle() {
        return rewordsTitle;
    }

    public void setRewordsTitle(String rewordsTitle) {
        this.rewordsTitle = rewordsTitle;
    }

    public String getRewordsValidity() {
        return rewordsValidity;
    }

    public void setRewordsValidity(String rewordsValidity) {
        this.rewordsValidity = rewordsValidity;
    }

    public String getRewordsBody() {
        return rewordsBody;
    }

    public void setRewordsBody(String rewordsBody) {
        this.rewordsBody = rewordsBody;
    }
}
