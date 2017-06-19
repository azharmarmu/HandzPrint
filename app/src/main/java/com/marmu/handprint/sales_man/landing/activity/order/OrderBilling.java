package com.marmu.handprint.sales_man.landing.activity.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.model.OrderList;

public class OrderBilling extends AppCompatActivity {

    OrderList orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_order_biliing);
        orderList = (OrderList) getIntent().getSerializableExtra("orderBill");

    }

    public void generateBill(View view) {

    }

    public void backPress(View view) {
        onBackPressed();
    }
}
