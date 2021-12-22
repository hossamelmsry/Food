package com.elzayet.food.sidebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PointsActivity extends AppCompatActivity {
    private TextView a_p_userPoints;
    private ViewPager a_p_week;
    private DailyPointsAdapter dailyPointsAdapter;



    //User Account
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        a_p_userPoints = findViewById(R.id.a_p_userPoints);
        a_p_week     = findViewById(R.id.a_p_week);
        dailyPointsAdapter = new DailyPointsAdapter(this);
        a_p_week.setAdapter(dailyPointsAdapter);

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
    }

    ///////////////////////////////
    ////////DailyPointsAdapter/////////
    ///////////////////////////////
    private class DailyPointsAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater ;

        private int[] week = {R.string.saturday,R.string.sunday,R.string.monday,R.string.tuesday,R.string.wednesday,R.string.thursday,R.string.friday};

        public DailyPointsAdapter(Context context){ this.context = context ; }

        @Override
        public int getCount() { return week.length; }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) { return view == (ConstraintLayout) object; }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.card_daily_points_item,container,false);

            ImageView c_d_p_i_locker = view.findViewById(R.id.c_d_p_i_locker);
            TextView c_d_p_i_day     = view.findViewById(R.id.c_d_p_i_day);

            c_d_p_i_day.setText(week[position]);
//            c_s_m_sliderDescription.setText(sliderDescription[position]);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { container.removeView((ConstraintLayout) object); }
    }
}