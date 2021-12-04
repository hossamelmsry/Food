package com.elzayet.food.topbar.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.elzayet.food.R;

public class NotificationActivity extends AppCompatActivity {

    private TextView a_n_warningMsg;
    private RecyclerView a_n_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        a_n_warningMsg = findViewById(R.id.a_n_warningMsg);
        a_n_recyclerView = findViewById(R.id.a_n_recyclerView);
    }
}