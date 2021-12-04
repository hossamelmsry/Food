package com.elzayet.food.sidebar.registration;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elzayet.food.MainActivity;
import com.elzayet.food.R;
import com.elzayet.food.UserModel;
import com.elzayet.food.tools.Session;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout a_l_inputPhone , a_l_inputPassword  ;
    private Button a_l_forgotPassword , a_l_login  ;
    //user account
    private String phone , userPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        a_l_inputPhone     = findViewById(R.id.a_l_inputPhone);
        a_l_inputPassword  = findViewById(R.id.a_l_inputPassword);
        a_l_forgotPassword = findViewById(R.id.a_l_forgotPassword);
        a_l_login          = findViewById(R.id.a_l_login);

    }

    @Override
    public void onStart() {
        super.onStart();
        findViewById(R.id.a_l_loginBack).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.a_l_signup).setOnClickListener(v -> onBackPressed());

        a_l_forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SupportPhoneActivity.class));
            finish();
        });

        a_l_login.setOnClickListener(v -> validation());

    }

    private void validation() {
        phone        = a_l_inputPhone.getEditText().getText().toString().trim();
        userPassword = a_l_inputPassword.getEditText().getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            a_l_inputPhone.setError(null);
            if (phone.startsWith("01")) {
                a_l_inputPhone.setError(null);
                if (phone.length() == 11) {
                    a_l_inputPhone.setError(null);
                    if (!TextUtils.isEmpty(userPassword)) {
                        a_l_inputPassword.setError(null);
                        login();
                    } else {
                        a_l_inputPassword.getEditText().setError("empty");
                        a_l_inputPassword.getEditText().requestFocus();
                    }
                } else {
                    a_l_inputPhone.getEditText().setError(getString(R.string.phone_number_incorrect));
                    a_l_inputPhone.getEditText().requestFocus();
                }
            } else {
                a_l_inputPhone.getEditText().setError(getString(R.string.phone_number_incorrect));
                a_l_inputPhone.getEditText().requestFocus();
            }
        } else {
            a_l_inputPhone.getEditText().setError("empty");
            a_l_inputPhone.getEditText().requestFocus();
        }
    }

    private void login(){
        String phoneNumber = "+2"+phone;
        TextView a_s_lgoinError = findViewById(R.id.a_s_lgoinError);
        FirebaseDatabase.getInstance().getReference("ACCOUNTS").child(phoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            if(userPassword.equals(userModel.getUserPassword())){
                                Toast.makeText(LoginActivity.this, getString(R.string.welcome_again), Toast.LENGTH_SHORT).show();
                                Session.updateUserAccount(LoginActivity.this, phoneNumber, userModel.getUserName(),userPassword,userModel.getUserPinCode(),userModel.getUserRefellar());
                                onBackPressed();
                            } else { a_s_lgoinError.setText(getString(R.string.username_or_password_is_not_correct)); }
                        } else { a_s_lgoinError.setText(getString(R.string.username_not_exists)); }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {a_s_lgoinError.setText(error.getMessage()); }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { hideSystemUI(); }
        else { showSystemUI(); }
    }
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}