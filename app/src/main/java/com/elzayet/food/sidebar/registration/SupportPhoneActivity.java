package com.elzayet.food.sidebar.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elzayet.food.MainActivity;
import com.elzayet.food.R;
import com.elzayet.food.UserModel;
import com.elzayet.food.tools.InternetConnection;
import com.elzayet.food.tools.Session;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SupportPhoneActivity extends AppCompatActivity {
    private TextInputLayout a_s_p_inputPincode , a_s_p_inputPhoneNumber ;
    private Button a_s_p_submit ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_phone);
        a_s_p_inputPincode     = findViewById(R.id.a_s_p_inputPincode);
        a_s_p_inputPhoneNumber = findViewById(R.id.a_s_p_inputPhoneNumber);
        a_s_p_submit           = findViewById(R.id.a_s_p_submit);

    }

    @Override
    protected void onStart() {
        super.onStart();

        findViewById(R.id.a_p_n_phoneBack).setOnClickListener(v -> onBackPressed());
        a_s_p_submit.setOnClickListener(v -> validation());

    }

    private void validation() {
        String pincode     = a_s_p_inputPincode.getEditText().getText().toString().trim();
        String phoneNumber = a_s_p_inputPhoneNumber.getEditText().getText().toString().trim();
        if(!TextUtils.isEmpty(pincode)) {
            a_s_p_inputPincode.setError(null);
            if (pincode.length() == 6) {
                a_s_p_inputPincode.setError(null);
                if(!TextUtils.isEmpty(phoneNumber)) {
                    a_s_p_inputPhoneNumber.setError(null);
                    if (phoneNumber.length() == 11) {
                        a_s_p_inputPhoneNumber.setError(null);
                        if (phoneNumber.startsWith("01")) {
                            a_s_p_inputPhoneNumber.setError(null);
                            resetUserAccount(pincode ,phoneNumber);
                        } else {
                            a_s_p_inputPhoneNumber.getEditText().setError(getString(R.string.phone_number_incorrect));
                            a_s_p_inputPhoneNumber.getEditText().requestFocus();
                        }
                    }else{
                        a_s_p_inputPhoneNumber.getEditText().setError(getString(R.string.phone_number_incorrect));
                        a_s_p_inputPhoneNumber.getEditText().requestFocus();
                    }
                }else{
                    a_s_p_inputPhoneNumber.getEditText().setError(getString(R.string.Empty));
                    a_s_p_inputPhoneNumber.getEditText().requestFocus();
                }
            }else{
                a_s_p_inputPincode.getEditText().setError(getString(R.string.number_6));
                a_s_p_inputPincode.getEditText().requestFocus();
            }
        }else{
            a_s_p_inputPincode.getEditText().setError(getString(R.string.Empty));
            a_s_p_inputPincode.getEditText().requestFocus();
        }
    }

    private void resetUserAccount(String pincode ,String phone) {
        String phoneNumber = "+2" + phone;
        TextView a_s_p_supportError = findViewById(R.id.a_s_p_supportError);
        if(InternetConnection.isConnected(SupportPhoneActivity.this)){
            DatabaseReference USERS_BD = FirebaseDatabase.getInstance().getReference("USERS").child("ACCOUNTS").child(phoneNumber);
            USERS_BD.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if(pincode.equals(userModel.getUserPinCode())){
                            Toast.makeText(SupportPhoneActivity.this, getString(R.string.welcome_again), Toast.LENGTH_SHORT).show();
                            Session.updateUserAccount(SupportPhoneActivity.this, phoneNumber, userModel.getUserName(), userModel.getUserPassword(),userModel.getUserPinCode(),userModel.getUserRefellar());
                            startActivity(new Intent(SupportPhoneActivity.this, MainActivity.class));
                            finish();
                        } else { a_s_p_supportError.setText(getString(R.string.incorrect_pinecode_please_connect_us)); }
                    } else { a_s_p_supportError.setText(getString(R.string.username_not_exists)); }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { a_s_p_supportError.setText(error.getMessage()); }
            });
        }else{ InternetConnection.noConnection(SupportPhoneActivity.this); }
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