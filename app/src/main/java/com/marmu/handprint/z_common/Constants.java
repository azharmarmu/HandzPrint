package com.marmu.handprint.z_common;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by azharuddin on 26/5/17.
 */

public class Constants {
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
    public static final String USERS = "users";
    public static final String ADMIN_TAKEN = "taken";
    public static final String ADMIN_ORDER = "order";
    public static final String ADMIN_PRODUCT_PRICE = "product_and_price";
    public static final String ADMIN_PRODUCT_QTY = "product_and_qty";
    public static final String SALES_MAN = "sales_man";
    public static final String SALES_MAN_BILLING = "billing";
}
