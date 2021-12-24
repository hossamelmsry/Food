package com.elzayet.food.sidebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.MainActivity;
import com.elzayet.food.PointsModel;
import com.elzayet.food.R;
import com.elzayet.food.sidebar.registration.RegistrationActivity;
import com.elzayet.food.sidebar.registration.RegistrationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PointsActivity extends AppCompatActivity {
    private TextView a_p_userPoints;
    private RecyclerView a_p_week;
    private DailyPointsAdapter dailyPointsAdapter;
    //User Account
    private String phoneNumber;
    //
    private String userPoints;
    //
    private String dayOfWeek;

    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        currentDay = new SimpleDateFormat("EEEE").format(new Date());

        a_p_userPoints = findViewById(R.id.a_p_userPoints);
        a_p_week       = findViewById(R.id.a_p_week);
        a_p_week.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        a_p_week.setHasFixedSize(true);

        //user Account
        SharedPreferences prefAccount = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber                   = prefAccount.getString("phoneNumber", "NOTHING");
        SharedPreferences pref        = getSharedPreferences("WEEK", MODE_PRIVATE);
        dayOfWeek                     = pref.getString(currentDay,"NOTHING");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("WALLETS").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PointsModel pointsModel = snapshot.getValue(PointsModel.class);
                        userPoints = pointsModel.getPoints();
                        a_p_userPoints.setText(userPoints);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show(); }
                });
        List<PointsModel> pointsModelList = new ArrayList<>();
        pointsModelList.add(new PointsModel(getString(R.string.saturday),R.drawable.ic_close_lock_gray));
        pointsModelList.add(new PointsModel(getString(R.string.sunday),R.drawable.ic_close_lock_gray));
        pointsModelList.add(new PointsModel(getString(R.string.monday),R.drawable.ic_close_lock_gray));
        pointsModelList.add(new PointsModel(getString(R.string.tuesday),R.drawable.ic_close_lock_gray));
        pointsModelList.add(new PointsModel(getString(R.string.wednesday),R.drawable.ic_close_lock_gray));
        pointsModelList.add(new PointsModel(getString(R.string.thursday),R.drawable.ic_close_lock_gray));
        pointsModelList.add(new PointsModel(getString(R.string.friday),R.drawable.ic_close_lock_gray));
        dailyPointsAdapter = new DailyPointsAdapter(pointsModelList,currentDay);
        a_p_week.setAdapter(dailyPointsAdapter);
        dailyPointsAdapter.notifyDataSetChanged();
    }

    ///////////////////////////////
    //////DailyPointsAdapter///////
    ///////////////////////////////
    private class DailyPointsAdapter extends RecyclerView.Adapter<DailyPointsAdapter.ViewHolder> {
        private final DatabaseReference WALLETS_DB  = FirebaseDatabase.getInstance().getReference("WALLETS");
        private List<PointsModel> pointsModelList;
        private String currentDay;

        public DailyPointsAdapter(List<PointsModel> pointsModelList, String currentDay) {
            this.pointsModelList = pointsModelList;
            this.currentDay      = currentDay;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_daily_points_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PointsModel pointsModel = pointsModelList.get(position);
            Picasso.get().load(pointsModel.getDayLocker()).placeholder(R.drawable.ic_close_lock_gray).error(R.drawable.ic_close_lock_gray).into(holder.c_d_p_i_locker);
            holder.c_d_p_i_day.setText(pointsModel.getDayOfWeek());
            holder.c_d_p_i_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentDay.equals(pointsModel.getDayOfWeek())){
                        if(dayOfWeek.equals("NOTHING")){givePoints();}
                        else{
                            Toast.makeText(getBaseContext(), "noooot", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getBaseContext(), "not", Toast.LENGTH_SHORT).show();
                    }
                    onStart();
                }
            });
        }

        private void givePoints() {
            SharedPreferences pref = getSharedPreferences("WEEK", Context.MODE_PRIVATE);
            SharedPreferences.Editor handeler = pref.edit();
            handeler.putString(currentDay ,currentDay);
            handeler.apply();

            final ProgressDialog progressdialog = ProgressDialog.show(PointsActivity.this, "Please wait", "Loading please wait..", true);
            progressdialog.setCancelable(false);
            progressdialog.setIcon(R.drawable.ic_launcher_background);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userPoints = Integer.toString(Integer.parseInt(userPoints)+5);
                    WALLETS_DB.child(phoneNumber).setValue(new PointsModel(userPoints));
                    try {
                        // put the thread to sleep for 2 seconds
                        Thread.sleep(2000);
                    } catch (Exception e) { }
                    progressdialog.dismiss();
                }
            }).start();

        }

        @Override
        public int getItemCount() { return pointsModelList.size(); }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ConstraintLayout c_d_p_i_container;
            private ImageView c_d_p_i_locker;
            private TextView c_d_p_i_day;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                c_d_p_i_container = itemView.findViewById(R.id.c_d_p_i_container);
                c_d_p_i_locker    = itemView.findViewById(R.id.c_d_p_i_locker);
                c_d_p_i_day       = itemView.findViewById(R.id.c_d_p_i_day);
            }
        }
    }
}