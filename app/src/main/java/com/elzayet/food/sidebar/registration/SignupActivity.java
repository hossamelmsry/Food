package com.elzayet.food.sidebar.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.elzayet.food.R;
import com.elzayet.food.tools.InternetConnection;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout a_s_inputUserName , a_s_inputPhone , a_s_inputPassword,a_s_reInputPassword;
    //user Account
    private String userName , phone , userPassword ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //
        setContentView(R.layout.activity_signup);
        a_s_inputUserName   = findViewById(R.id.a_s_inputUserName);
        a_s_inputPhone      = findViewById(R.id.a_s_inputPhone);
        a_s_inputPassword   = findViewById(R.id.a_s_inputPassword);
        a_s_reInputPassword = findViewById(R.id.a_s_reInputPassword);

        findViewById(R.id.a_s_signupBack).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.a_s_sendTOP).setOnClickListener(v -> validation());
        findViewById(R.id.a_s_login).setOnClickListener(v -> onBackPressed());
    }

    public void validation() {
        phone              = a_s_inputPhone.getEditText().getText().toString().trim();
        userName           = a_s_inputUserName.getEditText().getText().toString().trim();
        userPassword       = a_s_inputPassword.getEditText().getText().toString().trim();
        String re_password = a_s_reInputPassword.getEditText().getText().toString().trim();
        if(!TextUtils.isEmpty(phone)){
            a_s_inputPhone.setError(null);
            if (phone.length() == 11) {
                a_s_inputPhone.setError(null);
                if (phone.startsWith("01")) {
                    a_s_inputPhone.setError(null);
                    if(!TextUtils.isEmpty(userName)){
                        a_s_inputUserName.setError(null);
                        if(!TextUtils.isEmpty(userPassword)){
                            a_s_inputPassword.setError(null);
                            if (userPassword.length() >= 6) {
                                a_s_inputPassword.setError(null);
                                if(!TextUtils.isEmpty(re_password)){
                                    a_s_reInputPassword.setError(null);
                                    if(userPassword.equals(re_password)){
                                        a_s_inputPassword.setError(null);
                                        a_s_reInputPassword.setError(null);
                                        sendOTP();
                                    } else{
                                        a_s_reInputPassword.getEditText().setError(getString(R.string.not_equal));
                                        a_s_inputPassword.getEditText().setError(getString(R.string.not_equal));
                                    }
                                } else{
                                    a_s_reInputPassword.getEditText().setError(getString(R.string.Empty));
                                    a_s_reInputPassword.getEditText().requestFocus();
                                }
                            } else{
                                a_s_inputPassword.getEditText().setError(getString(R.string.too_weak));
                                a_s_inputPassword.getEditText().requestFocus();
                            }
                        } else{
                            a_s_inputPassword.getEditText().setError(getString(R.string.Empty));
                            a_s_inputPassword.getEditText().requestFocus();
                        }
                    }else{
                        a_s_inputUserName.getEditText().setError(getString(R.string.Empty));
                        a_s_inputUserName.getEditText().requestFocus();
                    }
                } else{
                    a_s_inputPhone.getEditText().setError(getString(R.string.phone_number_incorrect));
                    a_s_inputPhone.getEditText().requestFocus();
                }
            } else{
                a_s_inputPhone.getEditText().setError(getString(R.string.phone_number_incorrect));
                a_s_inputPhone.getEditText().requestFocus();
            }
        } else{
            a_s_inputPhone.getEditText().setError(getString(R.string.Empty));
            a_s_inputPhone.getEditText().requestFocus();
        }
    }

    private void sendOTP() {
        String phoneNumber = "+2" + phone;
        TextView a_s_registrationError = findViewById(R.id.a_s_registrationError);
        if (InternetConnection.isConnected(SignupActivity.this)) {
            FirebaseDatabase.getInstance().getReference("USERS").child("ACCOUNTS").child(phoneNumber)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) { a_s_registrationError.setText(phoneNumber + " Is Aleardy Exists "); }
                    else {
                        Intent intent = new Intent(SignupActivity.this, VerifyOTPActivity.class);
                        intent.putExtra("userName"    , userName);
                        intent.putExtra("phoneNumber" , phoneNumber);
                        intent.putExtra("userPassword", userPassword);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { a_s_registrationError.setText(error.getMessage()); }
            });
        } else { InternetConnection.noConnection(SignupActivity.this); }
    }
}