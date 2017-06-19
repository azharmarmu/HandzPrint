package com.marmu.handprint.sales_man.landing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.adapter.OrderAdapter;
import com.marmu.handprint.sales_man.landing.activity.billing.BillingActivity;
import com.marmu.handprint.sales_man.landing.activity.price.PriceActivity;
import com.marmu.handprint.sales_man.landing.activity.view_stock.ViewStockActivity;
import com.marmu.handprint.sales_man.model.OrderList;
import com.marmu.handprint.z_common.Constants;
import com.marmu.handprint.z_common.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class SalesManLandingActivity extends AppCompatActivity {

    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);
    DatabaseReference orderDBRef = Constants.DATABASE.getReference(Constants.ADMIN_ORDER);

    private String salesKey;
    private String salesMan;
    private String salesRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_landing);

        salesKey = getIntent().getStringExtra("sales_key");
        salesMan = getIntent().getStringExtra("sales_man_name");
        salesRoute = getIntent().getStringExtra("sales_route");

        checkOrder();
    }

    private void checkOrder() {
        orderDBRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelativeLayout orderContainer = (RelativeLayout) findViewById(R.id.order_container);
                if (dataSnapshot.getValue() != null) {
                    orderContainer.setVisibility(View.VISIBLE);

                    List<OrderList> orderLists = new ArrayList<>();
                    Iterator i = dataSnapshot.getChildren().iterator();

                    while (i.hasNext()) {
                        String key = ((DataSnapshot) i.next()).getKey();
                        String partyName, route, salesMan, process, salesDate;
                        HashMap<String, Object> salesOrderQTY;
                        try {
                            partyName = dataSnapshot.child(key).child("party_name").getValue().toString();
                            route = dataSnapshot.child(key).child("sales_route").getValue().toString();
                            salesMan = dataSnapshot.child(key).child("sales_man_name").getValue().toString();
                            process = dataSnapshot.child(key).child("process").getValue().toString();
                            salesDate = String.valueOf(dataSnapshot.child(key).child("sales_date").getValue());
                            salesOrderQTY = (HashMap<String, Object>) dataSnapshot.child(key).child("sales_order_qty").getValue();

                            if (process.equalsIgnoreCase("start")) {
                                orderLists.add(new OrderList(key, partyName, route, salesMan, process, salesDate, salesOrderQTY));
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }


                    RecyclerView orderView = (RecyclerView) findViewById(R.id.recycler_view);
                    OrderAdapter mAdapter = new OrderAdapter(orderLists, getApplicationContext());

                    //Recycler-view LayoutManager
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    orderView.setLayoutManager(layoutManager);

                    //Recycler-view Animator
                    RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                    itemAnimator.setAddDuration(1000);
                    itemAnimator.setRemoveDuration(1000);
                    orderView.setItemAnimator(itemAnimator);
                    orderView.setAdapter(mAdapter);

                } else {
                    orderContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void billingClick(View view) {
        Intent billing = new Intent(SalesManLandingActivity.this, BillingActivity.class);
        billing.putExtra("sales_key", salesKey);
        billing.putExtra("sales_man_name", salesMan);
        billing.putExtra("sales_route", salesRoute);
        startActivity(billing);
    }

    public void priceClick(View view) {
        Intent price = new Intent(SalesManLandingActivity.this, PriceActivity.class);
        price.putExtra("sales_key", salesKey);
        price.putExtra("sales_man_name", salesMan);
        price.putExtra("sales_route", salesRoute);
        startActivity(price);
    }

    public void viewStockClick(View view) {
        Intent viewStock = new Intent(SalesManLandingActivity.this, ViewStockActivity.class);
        viewStock.putExtra("sales_key", salesKey);
        viewStock.putExtra("sales_man_name", salesMan);
        viewStock.putExtra("sales_route", salesRoute);
        startActivity(viewStock);
    }

    public void closeClick(View view) {
        takenDBRef.child(salesKey).child("process").setValue("closed");
        finish();
    }

    public void signOut(View view) {
        Constants.AUTH.signOut();
        startActivity(new Intent(SalesManLandingActivity.this, LoginActivity.class));
        finish();
    }
}
