package com.marmu.handprint.admin.landing.activity.sales_man;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.Constants;

import java.util.HashMap;

public class SalesManActivity extends AppCompatActivity {

    DatabaseReference salesManDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sales_man);
    }

    public void addSalesMan(View view) {
        EditText salesManName = (EditText) findViewById(R.id.et_sales_man_name);
        EditText phone = (EditText) findViewById(R.id.et_mobile);

        String salesMan = salesManName.getText().toString();
        String salesPhone = phone.getText().toString();

        if (!salesMan.isEmpty() && !salesPhone.isEmpty()) {
            HashMap<String, Object> salesManMap = new HashMap<>();
            salesManMap.put("name", salesMan);
            salesManMap.put("phone", salesPhone);
            salesManDBRef.push().updateChildren(salesManMap);
            finish();
        }

    }

    public void backPress(View view) {
        onBackPressed();
    }
}
