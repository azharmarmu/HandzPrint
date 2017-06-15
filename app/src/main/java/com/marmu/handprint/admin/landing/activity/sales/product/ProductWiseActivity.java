package com.marmu.handprint.admin.landing.activity.sales.product;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.marmu.handprint.R;
import com.marmu.handprint.admin.landing.activity.sales.product.report.ProductReportActivity;

import java.util.Calendar;

@SuppressWarnings("unchecked")
public class ProductWiseActivity extends AppCompatActivity {
    private EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_wise);
        date = (EditText) findViewById(R.id.et_date);
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
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void generateSalesReport(View view) {
        Intent productReport = new Intent(ProductWiseActivity.this, ProductReportActivity.class);
        productReport.putExtra("date", date.getText().toString());
        startActivity(productReport);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
