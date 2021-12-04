package com.elzayet.food.sidebar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elzayet.food.R;

public class FQAActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView a_f_q_a_aboutApp,a_f_q_a_use,
            a_f_q_a_register,a_f_q_a_wallet,a_f_q_a_points,a_f_q_a_refeller;

    private ImageButton a_f_q_a_aboutApp_arrow,a_f_q_a_use_arrow,
            a_f_q_a_register_arrow,a_f_q_a_wallet_arrow,a_f_q_a_points_arrow,a_f_q_a_refeller_arrow;

    private TextView a_f_q_a_aboutApp_description,a_f_q_a_use_description ,
                    a_f_q_a_register_description, a_f_q_a_wallet_description,a_f_q_a_points_description,a_f_q_a_refeller_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_q_a);

        a_f_q_a_aboutApp = findViewById(R.id.a_f_q_a_aboutApp);
        a_f_q_a_aboutApp_description = findViewById(R.id.a_f_q_a_aboutApp_description);
        a_f_q_a_aboutApp_arrow  = findViewById(R.id.a_f_q_a_aboutApp_arrow);

        a_f_q_a_use      = findViewById(R.id.a_f_q_a_use);
        a_f_q_a_use_description      = findViewById(R.id.a_f_q_a_use_description);
        a_f_q_a_use_arrow       = findViewById(R.id.a_f_q_a_use_arrow);

        a_f_q_a_register = findViewById(R.id.a_f_q_a_register);
        a_f_q_a_register_description = findViewById(R.id.a_f_q_a_register_description);
        a_f_q_a_register_arrow  = findViewById(R.id.a_f_q_a_register_arrow);

        a_f_q_a_wallet   = findViewById(R.id.a_f_q_a_wallet);
        a_f_q_a_wallet_description   = findViewById(R.id.a_f_q_a_wallet_description);
        a_f_q_a_wallet_arrow    = findViewById(R.id.a_f_q_a_wallet_arrow);

        a_f_q_a_points   = findViewById(R.id.a_f_q_a_points);
        a_f_q_a_points_description   = findViewById(R.id.a_f_q_a_points_description);
        a_f_q_a_points_arrow    = findViewById(R.id.a_f_q_a_points_arrow);

        a_f_q_a_refeller = findViewById(R.id.a_f_q_a_refeller);
        a_f_q_a_refeller_description = findViewById(R.id.a_f_q_a_refeller_description);
        a_f_q_a_refeller_arrow  = findViewById(R.id.a_f_q_a_refeller_arrow);

        a_f_q_a_aboutApp.setOnClickListener(this);
        a_f_q_a_use.setOnClickListener(this);
        a_f_q_a_register.setOnClickListener(this);
        a_f_q_a_wallet.setOnClickListener(this);
        a_f_q_a_points.setOnClickListener(this);
        a_f_q_a_refeller.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.a_f_q_a_aboutApp:generalMethod(a_f_q_a_aboutApp,a_f_q_a_aboutApp_description,a_f_q_a_aboutApp_arrow);
                break;

            case  R.id.a_f_q_a_use:generalMethod(a_f_q_a_use,a_f_q_a_use_description,a_f_q_a_use_arrow);
                break;

            case  R.id.a_f_q_a_register:generalMethod(a_f_q_a_register,a_f_q_a_register_description,a_f_q_a_register_arrow);
                break;

            case  R.id.a_f_q_a_wallet:generalMethod(a_f_q_a_wallet,a_f_q_a_wallet_description,a_f_q_a_wallet_arrow);
                break;

            case  R.id.a_f_q_a_points:generalMethod(a_f_q_a_points,a_f_q_a_points_description,a_f_q_a_points_arrow);
                break;

            case  R.id.a_f_q_a_refeller:generalMethod(a_f_q_a_refeller,a_f_q_a_refeller_description,a_f_q_a_refeller_arrow);
                break;

        }
    }

    private void generalMethod(CardView cv,TextView tv,ImageButton ib){
        if (tv.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cv, new AutoTransition());
            tv.setVisibility(View.GONE);
            ib.setImageResource(R.drawable.ic_arrow_up_gray);
        } else {
            TransitionManager.beginDelayedTransition(cv, new AutoTransition());
            tv.setVisibility(View.VISIBLE);
            ib.setImageResource(R.drawable.ic_arrow_down_gray);
        }
    }

}