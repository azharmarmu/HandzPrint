package com.marmu.handprint.sales_man.landing.activity.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.landing.activity.order.create.CreateOrderActivity;
import com.marmu.handprint.sales_man.landing.activity.order.edit.EditOrderActivity;
import com.marmu.handprint.sales_man.landing.activity.order.view.ViewOrderActivity;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_order);
    }

    public void viewClick(View view) {
        startActivity(new Intent(OrderActivity.this, ViewOrderActivity.class));
    }

    public void createClick(View view) {
        startActivity(new Intent(OrderActivity.this, CreateOrderActivity.class));
    }

    public void editClick(View view) {
        startActivity(new Intent(OrderActivity.this, EditOrderActivity.class));
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
