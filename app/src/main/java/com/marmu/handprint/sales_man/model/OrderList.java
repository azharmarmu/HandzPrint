package com.marmu.handprint.sales_man.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by azharuddin on 16/6/17.
 */

public class OrderList implements Serializable {
    private String key, partyName, route, salesMan, process, salesDate;
    private HashMap<String, Object> salesOrderQTY;

    public OrderList(String key, String partyName, String route, String salesMan, String process, String salesDate, HashMap<String, Object> salesOrderQTY) {
        this.key = key;
        this.partyName = partyName;
        this.route = route;
        this.salesMan = salesMan;
        this.process = process;
        this.salesDate = salesDate;
        this.salesOrderQTY = salesOrderQTY;
    }

    public String getKey() {
        return key;
    }

    public String getPartyName() {
        return partyName;
    }

    public String getRoute() {
        return route;
    }

    public String getSalesMan() {
        return salesMan;
    }

    public String getProcess() {
        return process;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public HashMap<String, Object> getSalesOrderQTY() {
        return salesOrderQTY;
    }
}
