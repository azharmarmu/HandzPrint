package com.marmu.handprint.admin.landing.activity.items_return;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.admin.landing.activity.items_return.report.ReturnReportActivity;
import com.marmu.handprint.z_common.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class ReturnActivity extends AppCompatActivity {

    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);

    private EditText date;
    private Spinner spinner;

    List<String> salesRoute = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_return);
        date = (EditText) findViewById(R.id.et_date);
        spinner = (Spinner) findViewById(R.id.sp_route);
        setRoute();
    }

    private void setRoute() {
        takenDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    HashMap<String, Object> salesManDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                    assert salesManDetails != null;
                    for (String key : salesManDetails.keySet()) {
                        HashMap<String, Object> sales = (HashMap<String, Object>) salesManDetails.get(key);
                        if (!salesRoute.contains(sales.get("sales_route").toString())) {
                            salesRoute.add((String) sales.get("sales_route"));
                        }
                    }

                    //sp_route
                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(ReturnActivity.this,
                            android.R.layout.simple_spinner_item, salesRoute);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(nameAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void datePicker(View view) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar now = Calendar.getInstance();
                        if (dayOfMonth <= now.get(Calendar.DAY_OF_MONTH)) {
                            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            date.clearFocus();
                        } else {
                            date.setError("Choose Valid date");
                            date.requestFocus();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void checkReturn(View view) {
        String dte = date.getText().toString();
        if (!dte.isEmpty() && spinner.getSelectedItem() != null) {
            Intent productReport = new Intent(ReturnActivity.this, ReturnReportActivity.class);
            productReport.putExtra("date", dte);
            productReport.putExtra("route", spinner.getSelectedItem().toString());
            startActivity(productReport);
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
