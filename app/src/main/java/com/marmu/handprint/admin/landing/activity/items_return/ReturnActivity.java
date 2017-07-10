package com.marmu.handprint.admin.landing.activity.items_return;

import android.annotation.SuppressLint;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        date = findViewById(R.id.et_date);
        spinner = findViewById(R.id.sp_route);
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

    @SuppressLint("SimpleDateFormat")
    public void datePicker(View view) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String pYear = String.valueOf(year);
                        String pMonth = String.valueOf(monthOfYear + 1);
                        String pDay = String.valueOf(dayOfMonth);

                        String cYear = String.valueOf(mYear);
                        String cMonth = String.valueOf(mMonth + 1);
                        String cDay = String.valueOf(mDay);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date pickedDate = formatter.parse((pDay + "-" + pMonth + "-" + pYear));
                            Date currentDate = formatter.parse((cDay + "-" + cMonth + "-" + cYear));
                            if (pickedDate.compareTo(currentDate) <= 0) {
                                date.setText(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
                                date.clearFocus();
                            } else {
                                date.setError("Choose Valid date");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
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
