package com.marmu.handprint.admin.landing.activity.sales_man;

/**
 * Created by azharuddin on 30/6/17.
 */

class SalesManModel {

    private String key, name, phone;

    SalesManModel(String key, String name, String phone) {
        this.key = key;
        this.name = name;
        this.phone = phone;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
