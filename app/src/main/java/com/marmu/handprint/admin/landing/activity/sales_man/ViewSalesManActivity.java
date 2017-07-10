package com.marmu.handprint.admin.landing.activity.sales_man;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ViewSalesManActivity extends AppCompatActivity {

    DatabaseReference salesManDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_sales_man);
        populateView();
    }

    private void populateView() {

        salesManDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    List<SalesManModel> salesManLists = new ArrayList<>();
                    Iterator i = dataSnapshot.getChildren().iterator();
                    while (i.hasNext()) {
                        String key = ((DataSnapshot) i.next()).getKey();
                        String salesManName, salesManPhone;
                        try {
                            salesManName = dataSnapshot.child(key).child("name").getValue().toString();
                            salesManPhone = dataSnapshot.child(key).child("phone").getValue().toString();

                            salesManLists.add(new SalesManModel(key, salesManName, salesManPhone));

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                    ViewSalesManAdapter adapter = new ViewSalesManAdapter(getApplicationContext(),salesManLists);

                    RecyclerView salesManView = findViewById(R.id.sales_man_view);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    salesManView.setLayoutManager(layoutManager);
                    salesManView.setItemAnimator(new DefaultItemAnimator());
                    salesManView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });


    }

    public void backPress(View view) {
        onBackPressed();
    }
}
