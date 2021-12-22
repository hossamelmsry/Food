package com.elzayet.food.sidebar.registration;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elzayet.food.ArchiveModel;
import com.elzayet.food.MainActivity;
import com.elzayet.food.NotificationModel;
import com.elzayet.food.OrderModel;
import com.elzayet.food.PointsModel;
import com.elzayet.food.R;
import com.elzayet.food.UserModel;
import com.elzayet.food.databinding.ActivityVerifyOTPBinding;
import com.elzayet.food.tools.NotificationApp;
import com.elzayet.food.tools.Session;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {
    //User Account
    private String userName , phoneNumber , userPassword, userRefellar;

    private final DatabaseReference ACCOUNTS_DB     = FirebaseDatabase.getInstance().getReference("ACCOUNTS");
    private final DatabaseReference ORDERS_DB       = FirebaseDatabase.getInstance().getReference("ORDERS");
    private final DatabaseReference WALLETS_DB      = FirebaseDatabase.getInstance().getReference("WALLETS");
    private final DatabaseReference NOTIFICATION_DB = FirebaseDatabase.getInstance().getReference("NOTIFICATIONS");

    private ActivityVerifyOTPBinding binding ;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private FirebaseAuth auth;
    private static final String TAG = "main_tag";
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOTPBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //User Account
        userName    = getIntent().getStringExtra("userName");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        userPassword= getIntent().getStringExtra("userPassword");
        userRefellar= generateRefellar(phoneNumber + getString(R.string.app_name)+"abcdefghijklmnopqrstuvxyz");
        //
        auth = FirebaseAuth.getInstance();
        //
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        //
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId,forceResendingToken);
                Log.d(TAG,"onCodeSent : "+verificationId);
                mVerificationId     = verificationId;
                forceResendingToken = token;
                progressDialog.dismiss();
                Toast.makeText(VerifyOTPActivity.this, "Verification Code sening", Toast.LENGTH_SHORT).show();
            }
        };

        findViewById(R.id.a_v_otpBack).setOnClickListener(v -> onBackPressed());
        binding.resentCode.setOnClickListener(v -> resentVerification(phoneNumber,forceResendingToken));
        binding.codeSubmitBtn.setOnClickListener(v -> {
            String code = binding.codeEt.getText().toString().trim();
            verificationCode(mVerificationId,code);
        });
        startPhoneNumberVerification();
    }

    static String generateRefellar(String refellar) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int index = (int)(refellar.length()* Math.random());
            sb.append(refellar.charAt(index));
        }
        return sb.toString();
    }

    private void startPhoneNumberVerification() {
        progressDialog.setTitle("verifying phone number");
        progressDialog.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resentVerification(String phoneNumber , PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setTitle("Resending Code");
        progressDialog.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)              // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                        // Activity (for callback binding)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)            // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verificationCode(String mVerificationId ,String code) {
        progressDialog.setTitle("Verfyinf Code");
        progressDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        @SuppressLint("SimpleDateFormat") String signupDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        @SuppressLint("SimpleDateFormat") String signupTime = new SimpleDateFormat("hh:mm:ss a").format(new Date());
        progressDialog.setTitle("Signup Please Wait");
        progressDialog.show();

        auth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Signup Successfuly", Toast.LENGTH_SHORT).show();
                    ACCOUNTS_DB.child(phoneNumber).setValue(new UserModel(phoneNumber,userName,userPassword,"000000",userRefellar,signupDate));
                    ORDERS_DB.child(phoneNumber).child("1").setValue(new OrderModel("1","تسجيل في التطبيق = 500 نقطة في محفظتك",signupDate,signupTime));
                    NOTIFICATION_DB.child(phoneNumber).child("1").setValue(new NotificationModel("1","تسجيل في التطبيق = 500 نقطة في محفظتك",signupDate,signupTime,"application"));
                    WALLETS_DB.child(phoneNumber).setValue(new PointsModel("500"));
                    Session.updateUserAccount(VerifyOTPActivity.this, phoneNumber, userName,userPassword,"000000",userRefellar);
                    NotificationApp.addNotificationWithAction(VerifyOTPActivity.this, "مبروك تم اضافة 500 في محفظتك, ");
                    startActivity(new Intent(VerifyOTPActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}