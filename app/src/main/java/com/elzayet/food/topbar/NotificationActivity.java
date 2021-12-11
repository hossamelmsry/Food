package com.elzayet.food.topbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.elzayet.food.NotificationModel;
import com.elzayet.food.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private final DatabaseReference NOTIFICATION_DB = FirebaseDatabase.getInstance().getReference("NOTIFICATIONS");


    private TextView a_n_warningMsg;
    private RecyclerView a_n_recyclerView;

    //user account
    private String phoneNumber ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar a_n_toolbar = findViewById(R.id.a_n_toolbar);
        setSupportActionBar(a_n_toolbar);
//        a_n_toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Notifications");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        a_n_warningMsg = findViewById(R.id.a_n_warningMsg);
        a_n_recyclerView = findViewById(R.id.a_n_recyclerView);
        a_n_recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        a_n_recyclerView.setHasFixedSize(true);
        //user account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber = pref.getString("phoneNumber", "NOTHING");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showNotidication();
    }

    private void showNotidication() {
        FirebaseRecyclerOptions<NotificationModel> options =
                new FirebaseRecyclerOptions.Builder<NotificationModel>().setQuery(NOTIFICATION_DB.child(phoneNumber) , NotificationModel.class).setLifecycleOwner( this).build();
        FirebaseRecyclerAdapter<NotificationModel, NotificationAdapter> adapter =
                new FirebaseRecyclerAdapter<NotificationModel, NotificationAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull NotificationAdapter holder, int position, @NonNull NotificationModel model) {
                        String msg    = model.getMsg();
                        String date   = model.getDate();
                        String time   = model.getTime();
                        String status = model.getStatus();
                        holder.showNotification(status,msg,date,time);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getBaseContext(), "Cklicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public NotificationAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new NotificationAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification_item, parent, false));
                    }
                };
        a_n_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    ///////////////////////////////////
    ///////HomeFragmentAdapter/////////
    ///////////////////////////////////
    private static class NotificationAdapter extends RecyclerView.ViewHolder {

        public NotificationAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public void showNotification(String status, String msg, String date, String time) {

            ImageView c_n_i_statusImage= itemView.findViewById(R.id.c_n_i_statusImage);
            TextView c_n_i_msg         = itemView.findViewById(R.id.c_n_i_msg);
            TextView c_n_i_dateTime    = itemView.findViewById(R.id.c_n_i_dateTime);

            if(status.equals("completed")){
                c_n_i_statusImage.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_check_white));
            }
            else if(status.equals("cancel")){
                c_n_i_statusImage.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_cancel_white));
            }
            else if(status.equals("application")){
                c_n_i_statusImage.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_fastfood_white));
            }
            else{
                c_n_i_statusImage.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_person_white));
            }

            c_n_i_msg.setText(msg);
            c_n_i_dateTime.setText(date+"\n"+time);
        }


    }
}