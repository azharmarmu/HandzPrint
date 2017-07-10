package com.marmu.handprint.z_common.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DatabaseReference;
import com.marmu.handprint.admin.landing.activity.AdminLandingActivity;
import com.marmu.handprint.sales_man.taken.TakenActivity;
import com.marmu.handprint.z_common.Constants;

import java.util.HashMap;

/**
 * Created by azharuddin on 8/6/17.
 */

@SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
class FirebasePhoneLogin {
    private static DatabaseReference usersDBRef = Constants.DATABASE.getReference(Constants.USERS);

    static void signInWithPhoneAuthCredential(final Context context,
                                              PhoneAuthCredential credential,
                                              final String user,
                                              final String salesMan) {

        Constants.AUTH.signInWithCredential(credential)
                .addOnCompleteListener((new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInWithCredential:success");

                            if (user.equalsIgnoreCase("admin")) {

                                HashMap<String, Object> adminDetails = new HashMap<>();
                                adminDetails.put("user", user);
                                usersDBRef.child(Constants.AUTH.getCurrentUser().getUid()).updateChildren(adminDetails);

                                Intent adminLandingActivity = new Intent(context, AdminLandingActivity.class);
                                adminLandingActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                adminLandingActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(adminLandingActivity);
                            } else {

                                HashMap<String, Object> adminDetails = new HashMap<>();
                                adminDetails.put("user", user);
                                adminDetails.put("sales_man_name", salesMan);
                                usersDBRef.child(Constants.AUTH.getCurrentUser().getUid()).updateChildren(adminDetails);

                                Intent salesTakenActivity = new Intent(context, TakenActivity.class);
                                salesTakenActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                salesTakenActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                salesTakenActivity.putExtra("sales_man_name", salesMan);
                                context.startActivity(salesTakenActivity);
                            }
                            // ...
                        } else {
                            Log.w("Failed", "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(context,
                                        "Verification code is wrong",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }));
    }
}
