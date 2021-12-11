package com.elzayet.food.bottombar;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.CartModel;
import com.elzayet.food.FavoriteModel;
import com.elzayet.food.OrderModel;
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OrdersFragment extends Fragment {
    private final DatabaseReference ORDERS_DB   = FirebaseDatabase.getInstance().getReference("ORDERS");

    private TextView f_o_warningMsg;
    private RecyclerView f_o_recyclerView;
    //user account
    private String phoneNumber,userName ;

    public OrdersFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        f_o_warningMsg = view.findViewById(R.id.f_o_warningMsg);
        f_o_recyclerView = view.findViewById(R.id.f_o_recyclerView);
        f_o_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        f_o_recyclerView.setHasFixedSize(true);


        //user account
        SharedPreferences pref = getContext().getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        userName               = pref.getString("userName", "NOTHING");
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showOrders();
    }

    private void showOrders() {
        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>().setQuery(ORDERS_DB.child(phoneNumber) , OrderModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
        FirebaseRecyclerAdapter<OrderModel, OrdersFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<OrderModel, OrdersFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersFragmentAdapter holder, int position, @NonNull OrderModel model) {
                        String date = model.getDate();
                        String time = model.getTime();
                        String orderId = model.getOrderId();
                        holder.showOrders(date,time,orderId,phoneNumber,userName);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public OrdersFragmentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new OrdersFragmentAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_item, parent, false));
                    }
                };
        f_o_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }





    ///////////////////////////////////
    ///////CartFragmentAdapter/////////
    ///////////////////////////////////
    private static class OrdersFragmentAdapter extends RecyclerView.ViewHolder {

        public OrdersFragmentAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public void showOrders(String date, String time, String orderId, String phoneNumber, String userName) {
            TextView c_o_i_dateTime   = itemView.findViewById(R.id.c_o_i_dateTime);
            TextView c_o_i_orderId    = itemView.findViewById(R.id.c_o_i_orderId);
            TextView c_o_i_userAccount= itemView.findViewById(R.id.c_o_i_userAccount);

            c_o_i_dateTime.setText(date+"\n"+time);
            c_o_i_orderId.setText("Order Num : "+orderId);
            c_o_i_userAccount.setText("phoneNumber "+phoneNumber+"\nuserName "+userName);
        }
    }

}