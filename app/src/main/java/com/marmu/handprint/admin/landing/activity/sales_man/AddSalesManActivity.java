package com.marmu.handprint.admin.landing.activity.sales_man;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.Constants;

import java.util.HashMap;

public class AddSalesManActivity extends AppCompatActivity {

    DatabaseReference salesManDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN);

    String key, name, phone;

    EditText salesManName, salesManPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_sales_man);

        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");

        salesManName = findViewById(R.id.et_sales_man_name);
        salesManPhone = findViewById(R.id.et_mobile);

        if (name != null) {
            salesManName.setText(name);
        }

        if (phone != null) {
            salesManPhone.setText(phone);
        }

    }

    public void addSalesMan(View view) {

        String salesMan = salesManName.getText().toString();
        String salesPhone = salesManPhone.getText().toString();
        if (!salesMan.isEmpty() && !salesPhone.isEmpty()) {
            HashMap<String, Object> salesManMap = new HashMap<>();
            salesManMap.put("name", salesMan);
            salesManMap.put("phone", salesPhone);
            if (key != null) {
                salesManDBRef.child(key).updateChildren(salesManMap);
            } else {
                salesManDBRef.push().updateChildren(salesManMap);
            }
            finish();
        }

    }

    public void backPress(View view) {
        onBackPressed();
    }
}
