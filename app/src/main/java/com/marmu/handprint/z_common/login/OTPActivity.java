package com.marmu.handprint.z_common.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.crash.FirebaseCrash;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.sms.SMSReceiver;
import com.marmu.handprint.z_common.sms.SmsListener;


@SuppressWarnings({"ConstantConditions", "ThrowableResultOfMethodCallIgnored"})
public class OTPActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String user, verificationId, ForceResendingToken, salesMan;

    EditText et_OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.report(new Exception("OTP Activity Exception"));
        setContentView(R.layout.activity_z_otp);
        mAuth = FirebaseAuth.getInstance();

        user = getIntent().getExtras().getString("user");
        verificationId = getIntent().getExtras().getString("verificationId");
        ForceResendingToken = getIntent().getExtras().getString("ForceResendingToken");

        try {
            salesMan = getIntent().getExtras().getString("salesMan");
        } catch (NullPointerException e) {
            salesMan = null;
        }
        et_OTP = findViewById(R.id.et_otp);
        autoFetchOTP();
    }

    private void autoFetchOTP() {
        Animation blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        TextView fetchOTP = findViewById(R.id.try_fetch);
        fetchOTP.startAnimation(blink);
        SMSReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                et_OTP.setText(messageText);
                String code = et_OTP.getText().toString();
                et_OTP.setSelection(code.length());
                verifyOTP(code);
            }
        });
    }

    public void OTPSubmit(View view) {

        String code = et_OTP.getText().toString();
        if (!code.isEmpty()) {
            verifyOTP(code);
        } else {
            Toast.makeText(getApplicationContext(), "OTP cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOTP(String code) {
        FirebasePhoneLogin.signInWithPhoneAuthCredential(OTPActivity.this,
                new PhoneAuthCredential(verificationId, code),
                user,
                salesMan);
        finish();
    }


    public void resendOTP(View view) {

    }
}
