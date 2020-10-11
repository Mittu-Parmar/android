package com.example.mymall.model;

public class AddressesModel {
    public static final int DELIVERY_ACTIVITY=0;
    public static final int ACCOUNT_FRAGMENT=1;

    String name;
    String address;
    String pinCode;
    boolean selected;

    public AddressesModel(String name, String address, String pinCode, boolean selected) {
        this.name = name;
        this.address = address;
        this.pinCode = pinCode;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
