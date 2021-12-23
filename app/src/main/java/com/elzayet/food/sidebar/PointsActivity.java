package com.elzayet.food.sidebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.elzayet.food.PointsModel;
import com.elzayet.food.R;
import com.elzayet.food.sidebar.registration.RegistrationActivity;
import com.elzayet.food.sidebar.registration.RegistrationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        a_p_userPoints = findViewById(R.id.a_p_userPoints);
        a_p_week       = findViewById(R.id.a_p_week);
        a_p_week.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        a_p_week.setHasFixedSize(true);

        //user Account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("WALLETS").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PointsModel pointsModel = snapshot.getValue(PointsModel.class);
                        a_p_userPoints.setText(pointsModel.getPoints());
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
        dailyPointsAdapter = new DailyPointsAdapter(pointsModelList);
        a_p_week.setAdapter(dailyPointsAdapter);
        dailyPointsAdapter.notifyDataSetChanged();
    }

    ///////////////////////////////
    //////DailyPointsAdapter///////
    ///////////////////////////////
    private class DailyPointsAdapter extends RecyclerView.Adapter<DailyPointsAdapter.ViewHolder> {
        private List<PointsModel> pointsModelList;

        public DailyPointsAdapter(List<PointsModel> pointsModelList) { this.pointsModelList = pointsModelList; }

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
                    SimpleDateFormat sdf = new SimpleDateFormat("A");
                    Toast.makeText(getBaseContext(), ""+sdf.format(new Date()), Toast.LENGTH_SHORT).show();
                }
            });
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