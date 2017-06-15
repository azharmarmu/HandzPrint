package com.marmu.handprint.admin.landing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.marmu.handprint.R;
import com.marmu.handprint.admin.landing.activity.create_order.CreateOrderActivity;
import com.marmu.handprint.admin.landing.activity.items_return.ReturnActivity;
import com.marmu.handprint.admin.landing.activity.price_setup.SetAmountActivity;
import com.marmu.handprint.admin.landing.activity.sales.SalesActivity;
import com.marmu.handprint.admin.landing.activity.sales_man.SalesManActivity;
import com.marmu.handprint.admin.landing.activity.taken.TakenActivity;
import com.marmu.handprint.z_common.Constants;
import com.marmu.handprint.z_common.login.LoginActivity;

public class AdminLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        //device Token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", refreshedToken);
    }

    public void salesmanClick(View view) {
        startActivity(new Intent(AdminLandingActivity.this, SalesManActivity.class));
    }

    public void createOrderClick(View view) {
        startActivity(new Intent(AdminLandingActivity.this, CreateOrderActivity.class));
    }

    public void takenClick(View view) {
        startActivity(new Intent(AdminLandingActivity.this, TakenActivity.class));
    }

    public void salesClick(View view) {
        startActivity(new Intent(AdminLandingActivity.this, SalesActivity.class));
    }

    public void priceClick(View view) {
        startActivity(new Intent(AdminLandingActivity.this, SetAmountActivity.class));
    }

    public void returnClick(View view) {
        startActivity(new Intent(AdminLandingActivity.this, ReturnActivity.class));
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void signOut(View view) {
        Constants.AUTH.signOut();
        startActivity(new Intent(AdminLandingActivity.this, LoginActivity.class));
        finish();
    }
}
