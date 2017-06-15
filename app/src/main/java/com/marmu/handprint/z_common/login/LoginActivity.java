package com.marmu.handprint.z_common.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.admin.landing.activity.AdminLandingActivity;
import com.marmu.handprint.sales_man.taken.TakenActivity;
import com.marmu.handprint.z_common.Constants;
import com.marmu.handprint.z_common.Permissions;
import com.marmu.handprint.z_common.ProgressBarHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"deprecation", "unchecked", "ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference salesManDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN);
    DatabaseReference usersDBRef = Constants.DATABASE.getReference(Constants.USERS);

    TextView adminTab, salesManTab;
    LinearLayout adminLayout, salesManLayout;
    List<String> salesManName = new ArrayList<>();
    List<String> salesManPhone = new ArrayList<>();
    Spinner sp_name;

    List<String> adminPhoneNumber = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isLoggedIn();

        //add phone number
        adminPhoneNumber.add("9487266427");
        adminPhoneNumber.add("8681034261");
        adminPhoneNumber.add("9449802606");

        //permissions
        Permissions.SMS(LoginActivity.this);
    }

    private void isLoggedIn() {
        if (Constants.AUTH.getCurrentUser() != null) {
            String uid = Constants.AUTH.getCurrentUser().getUid();
            getUserDetails(uid);
        } else {
            setContentView(R.layout.activity_z_login);
            adminTab = (TextView) findViewById(R.id.tv_admin);
            salesManTab = (TextView) findViewById(R.id.tv_sales_man);

            adminLayout = (LinearLayout) findViewById(R.id.admin_layout);
            salesManLayout = (LinearLayout) findViewById(R.id.sales_man_layout);

            adminTab.setOnClickListener(this);
            salesManTab.setOnClickListener(this);
            getSalesManDetails();
        }
    }

    private void getUserDetails(String id) {
        usersDBRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    HashMap<String, Object> userDetails = (HashMap<String, Object>) dataSnapshot.getValue();
                    String user = userDetails.get("user").toString();
                    if (user.equalsIgnoreCase("admin")) {
                        Intent adminLandingActivity = new Intent(LoginActivity.this, AdminLandingActivity.class);
                        adminLandingActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(adminLandingActivity);
                        finish();
                    } else {
                        Intent salesTakenActivity = new Intent(LoginActivity.this, TakenActivity.class);
                        salesTakenActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        salesTakenActivity.putExtra("sales_man_name", userDetails.get("sales_man_name").toString());
                        startActivity(salesTakenActivity);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void getSalesManDetails() {
        //get salesman_name
        salesManDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sp_name = (Spinner) findViewById(R.id.sp_name);
                if (dataSnapshot.getValue() != null) {
                    HashMap<String, Object> salesManDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : salesManDetails.keySet()) {
                        HashMap<String, Object> sales = (HashMap<String, Object>) salesManDetails.get(key);
                        String name = sales.get("name").toString();
                        String phone = sales.get("phone").toString();
                        if (!salesManName.contains(name)) {
                            salesManName.add(name);
                            salesManPhone.add(phone);
                        }
                    }

                    //sp_name
                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(LoginActivity.this,
                            android.R.layout.simple_spinner_item, salesManName);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_name.setAdapter(nameAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void loginAdmin(View view) {
        EditText et_phone = (EditText) findViewById(R.id.et_mobile_admin);
        String phone = et_phone.getText().toString();

        if (!phone.isEmpty() && phone.length() == 10) {
            if (adminPhoneNumber.contains(phone)) {
                phoneNumberVerification(phone, "admin", null);
            } else {
                Toast.makeText(getApplicationContext(), "You didn't register as Admin", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginSalesMan(View view) {
        if (sp_name.getChildCount() > 0) {
            String salesMan = sp_name.getSelectedItem().toString();
            EditText et_phone = (EditText) findViewById(R.id.et_mobile_sales_man);
            String phone = et_phone.getText().toString();
            String orgPhone = salesManPhone.get(salesManName.indexOf(salesMan));

            if (!phone.isEmpty() && phone.equals(orgPhone)) {
                phoneNumberVerification(phone, "salesMan", salesMan);
            } else {
                Toast.makeText(getApplicationContext(), "Enter registered mobile number", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No sales man added", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneNumberVerification(String phoneNumber, final String user, final String salesMan) {
        final ProgressBarHandler progressBarHandler = new ProgressBarHandler(LoginActivity.this);
        progressBarHandler.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        //Instant verification or Auto-retrieval.
                        progressBarHandler.hide();
                        Log.d("Success", "onVerificationCompleted:" + credential);
                        FirebasePhoneLogin.signInWithPhoneAuthCredential(getApplicationContext(),
                                credential,
                                user,
                                salesMan);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        progressBarHandler.hide();
                        Log.w("Failed", "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            Toast.makeText(getApplicationContext(),
                                    "The SMS quota for the project has been exceeded",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        // Show a message and update the UI
                        // ...
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.

                        progressBarHandler.hide();
                        Log.d("OTP Code", "onCodeSent:" + verificationId);

                        Intent otpActivity = new Intent(LoginActivity.this, OTPActivity.class);
                        otpActivity.putExtra("verificationId", verificationId);
                        otpActivity.putExtra("ForceResendingToken", token);

                        if (user.equalsIgnoreCase("admin")) {
                            otpActivity.putExtra("user", "admin");
                        } else {
                            otpActivity.putExtra("user", "sales man");
                            otpActivity.putExtra("salesMan", salesMan);
                        }
                        startActivity(otpActivity);
                    }
                }); // OnVerificationStateChangedCallbacks
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_admin:

                //change color for active tab and change color for inactive tab
                adminTab.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                salesManTab.setBackgroundColor(getResources().getColor(R.color.colorGrey));

                //change font color of active tab and inactive tab
                adminTab.setTextColor(getResources().getColor(R.color.colorWhite));
                salesManTab.setTextColor(getResources().getColor(R.color.colorBlack));

                //show relevant container and hide other
                adminLayout.setVisibility(View.VISIBLE);
                salesManLayout.setVisibility(View.GONE);
                break;
            case R.id.tv_sales_man:

                //change color for active tab and change color for inactive tab
                adminTab.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                salesManTab.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                //change font color of active tab and inactive tab
                adminTab.setTextColor(getResources().getColor(R.color.colorBlack));
                salesManTab.setTextColor(getResources().getColor(R.color.colorWhite));

                //show relevant container and hide other
                adminLayout.setVisibility(View.GONE);
                salesManLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
