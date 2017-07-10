package com.marmu.handprint.admin.landing.activity.taken;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class TakenActivity extends AppCompatActivity {

    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);
    EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_taken);
        date = findViewById(R.id.et_date);

        Date currentDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setText(day + "/" + "0" + (month) + "/" + year);

        populateTaken();

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
                                populateTaken();
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

    @SuppressLint("SimpleDateFormat")
    private void populateTaken() {
        takenDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    List<TakenModel> takenList = new ArrayList<>();
                    HashMap<String, Object> takenMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : takenMap.keySet()) {
                        HashMap<String, Object> takenOrder = (HashMap<String, Object>) takenMap.get(key);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date salesDate = formatter.parse(takenOrder.get("sales_date").toString());
                            Date pickedDate = formatter.parse(date.getText().toString());
                            if (pickedDate.compareTo(salesDate) == 0) {
                                takenList.add(new TakenModel(key, takenOrder));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    TakenAdapter adapter = new TakenAdapter(getApplicationContext(), takenList);

                    RecyclerView takenView = findViewById(R.id.taken_view);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    takenView.setLayoutManager(layoutManager);
                    takenView.setItemAnimator(new DefaultItemAnimator());
                    takenView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void setTakenClick(View view) {
        startActivity(new Intent(TakenActivity.this, SetTakenActivity.class));
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
