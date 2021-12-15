package com.elzayet.food.bottombar;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.AccounterModel;
import com.elzayet.food.CartModel;
import com.elzayet.food.FavoriteModel;
import com.elzayet.food.MainActivity;
import com.elzayet.food.OrderModel;
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OrdersFragment extends Fragment {
    private final DatabaseReference ORDERS_DB   = FirebaseDatabase.getInstance().getReference("ORDERS");
    private final DatabaseReference ARCHIVE_DB  = FirebaseDatabase.getInstance().getReference("ARCHIVE");
    private final DatabaseReference ACCOUNTER_DB= FirebaseDatabase.getInstance().getReference("ACCOUNTER");
    private final DatabaseReference KITCHEN_DB  = FirebaseDatabase.getInstance().getReference("KITCHEN");

    private TextView f_o_warningMsg;
    private RecyclerView f_o_recyclerView,o_l_l_recyclerView;
    //user account
    private String phoneNumber ;

    public OrdersFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        f_o_warningMsg   = view.findViewById(R.id.f_o_warningMsg);
        f_o_recyclerView = view.findViewById(R.id.f_o_recyclerView);
        f_o_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        f_o_recyclerView.setHasFixedSize(true);

        //user account
        SharedPreferences pref = getContext().getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showOrders();
    }

    private void showOrders() {
        FirebaseRecyclerOptions<OrderModel> options = new FirebaseRecyclerOptions.Builder<OrderModel>()
                        .setQuery(ORDERS_DB.child(phoneNumber).orderByChild("orderStatus").equalTo("processed") ,OrderModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
        FirebaseRecyclerAdapter<OrderModel, OrdersFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<OrderModel, OrdersFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersFragmentAdapter holder, int position, @NonNull OrderModel model) {
                        String date        = model.getDate();
                        String time        = model.getTime();
                        String orderId     = model.getOrderId();
                        String orderStatus = model.getOrderStatus();
                        holder.showOrders(date,time,orderId,orderStatus);
                        holder.itemView.setOnClickListener(view -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle(R.string.options);
                            builder.setIcon(R.drawable.ic_photo_24);
                            builder.setPositiveButton(R.string.show_this_order, (dialog, which) -> showOrderList(orderId));
                            builder.setNeutralButton(R.string.cancel_this_order, (dialog, which) -> cancelOrder(orderId,date,time,"Cancel" ));
                            builder.show();

                        });
                    }

                    private void cancelOrder(String orderId, String date, String time, String orderStatus) {
                        ORDERS_DB.child(phoneNumber).child(orderId).setValue(new OrderModel(orderId,date,time,orderStatus ));
                        KITCHEN_DB.child(orderId).removeValue();
                        ACCOUNTER_DB.child(orderId).removeValue();
                        ARCHIVE_DB.child(phoneNumber).child(orderId).removeValue();
                        onStart();
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

    private void showOrderList(String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View view = getLayoutInflater().inflate(R.layout.order_list_layout, null);

        o_l_l_recyclerView = view.findViewById(R.id.o_l_l_recyclerView);
        o_l_l_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        o_l_l_recyclerView.setHasFixedSize(true);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        showList(orderId);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showList(String orderId) {
        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>().setQuery(ARCHIVE_DB.child(phoneNumber).child(orderId) , CartModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
        FirebaseRecyclerAdapter<CartModel,OrdersFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<CartModel,OrdersFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersFragmentAdapter holder, int position, @NonNull CartModel model) {
                        String productId       = model.getProductId();
                        String productQuantity = model.getProductQuantity();
                        String productSize     = model.getProductSize();
                        String productTopping  = model.getProductTopping();
                        String productPrice    = model.getProductPrice();
                        holder.showList(productId,productQuantity,productSize,productTopping,productPrice);
                    }
                    @NonNull
                    @Override
                    public OrdersFragmentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new OrdersFragmentAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_list_item, parent, false));
                    }
                };
        o_l_l_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }


    ///////////////////////////////////
    ///////OrdersFragmentAdapter///////
    ///////////////////////////////////
    private static class OrdersFragmentAdapter extends RecyclerView.ViewHolder {

        public OrdersFragmentAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public void showOrders(String date, String time, String orderId, String orderStatus) {
            TextView c_o_i_dateTime   = itemView.findViewById(R.id.c_o_i_dateTime);
            TextView c_o_i_orderId    = itemView.findViewById(R.id.c_o_i_orderId);
            TextView c_o_i_orderStatus= itemView.findViewById(R.id.c_o_i_orderStatus);

            c_o_i_dateTime.setText(date+"\n"+time);
            c_o_i_orderId.setText("Order Num : "+orderId);
            c_o_i_orderStatus.setText("order status :"+orderStatus);
        }

        public void showList(String productId, String productQuantity, String productSize, String productTopping, String productPrice) {
            ImageView c_o_l_i_productImage  = itemView.findViewById(R.id.c_o_l_i_productImage);
            TextView c_o_l_i_productName    = itemView.findViewById(R.id.c_o_l_i_productName);
            TextView c_o_l_i_cartDescription= itemView.findViewById(R.id.c_o_l_i_cartDescription);
            if(productTopping == null){ productTopping = "لا يوجد "; }
            String finalOrderTopping = productTopping;
            FirebaseDatabase.getInstance().getReference("PRODUCTS").child(productId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                ProductModel productModel = snapshot.getValue(ProductModel.class);
                                Picasso.get().load(productModel.getProductImage()).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_o_l_i_productImage);
                                c_o_l_i_productName.setText(productQuantity + "|"+productModel.getProductName()+"|"+productSize);
                                c_o_l_i_cartDescription.setText( "Topping:"+ finalOrderTopping +"\n"+"Order Price:"+productPrice);
                            } else {   Toast.makeText(itemView.getContext(), "لا يوجد هذا المنتج في الوقت الحالي", Toast.LENGTH_SHORT).show();  }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(itemView.getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}