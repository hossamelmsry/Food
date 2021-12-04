package com.elzayet.food.bottombar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elzayet.food.R;

public class OrdersFragment extends Fragment {

    private TextView f_o_warningMsg;
    private RecyclerView f_o_recyclerView;

    public OrdersFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        f_o_warningMsg = view.findViewById(R.id.f_o_warningMsg);
        f_o_recyclerView = view.findViewById(R.id.f_o_recyclerView);
        f_o_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        f_o_recyclerView.setHasFixedSize(true);

        return  view;
    }
}