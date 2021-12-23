package com.elzayet.food.sidebar.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elzayet.food.MainActivity;
import com.elzayet.food.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    private RecyclerView a_r_s_u_s_recyclerView;
    private RegistrationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        findViewById(R.id.a_r_s_u_s_back).setOnClickListener(v -> finish());
        a_r_s_u_s_recyclerView = findViewById(R.id.a_r_s_u_s_recyclerView);
        a_r_s_u_s_recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        a_r_s_u_s_recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //user account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        String phoneNumber = pref.getString("phoneNumber", "NOTHING");
        if(!phoneNumber.equals("NOTHING")){
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
        List<RegistrationModel> registrationModelList = new ArrayList<>();
        registrationModelList.add(new RegistrationModel(R.mipmap.desh1));
        registrationModelList.add(new RegistrationModel(R.mipmap.desh2));
        registrationModelList.add(new RegistrationModel(R.mipmap.desh3));
        registrationModelList.add(new RegistrationModel(R.mipmap.desh1));
        registrationModelList.add(new RegistrationModel(R.mipmap.desh2));
        registrationModelList.add(new RegistrationModel(R.mipmap.desh3));

        adapter = new RegistrationAdapter(registrationModelList);
        a_r_s_u_s_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        autoSliderScroll();
    }

    private void autoSliderScroll() {
        final int speedScroll = 0;
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if (count == adapter.getItemCount()) count = 0;
                if (count < adapter.getItemCount()) {
                    a_r_s_u_s_recyclerView.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    public void callLoginFromRetilar(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.a_r_login), "transation_login");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegistrationActivity.this, pairs);
            startActivity(intent, options.toBundle());
        } else { startActivity(intent); }
        new Thread() {
            @Override
            public void run() {
                try { sleep(1000); }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }.start();
    }

    public void callSignupFromRetilar(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.a_r_signup), "transation_signup");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegistrationActivity.this, pairs);
            startActivity(intent, options.toBundle());
        } else { startActivity(intent); }
        new Thread() {
            @Override
            public void run() {
                try { sleep(1000); }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }.start();
    }

    ///////////////////////////////
    //RegistrationActivityAdapter//
    ///////////////////////////////
    static class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.ViewHolder> {
        private List<RegistrationModel> registrationModelList;

        public RegistrationAdapter(List<RegistrationModel> plateModelList) { this.registrationModelList = plateModelList; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_plates_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RegistrationModel plateModel = registrationModelList.get(position);
            Picasso.get().load(plateModel.getPlateImage()).placeholder(R.drawable.ic_info_24).error(R.drawable.ic_info_24).into(holder.l_p_imageView);
        }

        @Override
        public int getItemCount() {
            return registrationModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView l_p_imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                l_p_imageView = itemView.findViewById(R.id.l_p_imageView);
            }
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}