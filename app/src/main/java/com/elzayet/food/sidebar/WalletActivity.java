package com.elzayet.food.sidebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.ArchiveModel;
import com.elzayet.food.CartModel;
import com.elzayet.food.OrderModel;
import com.elzayet.food.PointsModel;
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;

import java.util.Hashtable;

public class WalletActivity extends AppCompatActivity {
    private final DatabaseReference ORDERS_DB   = FirebaseDatabase.getInstance().getReference("ORDERS");
    private final DatabaseReference ARCHIVE_DB  = FirebaseDatabase.getInstance().getReference("ARCHIVE");

    private ImageView a_w_qrImage,a_w_facebook,a_w_instagram,a_w_twitter;
    private TextView a_w_userPoints,a_w_refellarLink;
    private RecyclerView a_w_waletHistory,o_l_l_recyclerView;
    //User Account
    private String phoneNumber,userRefellar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        //user Account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        userRefellar           = pref.getString("userRefellar", "NOTHING");

        a_w_qrImage      = findViewById(R.id.a_w_qrImage);
        a_w_facebook     = findViewById(R.id.a_w_facebook);
        a_w_instagram    = findViewById(R.id.a_w_instagram);
        a_w_twitter      = findViewById(R.id.a_w_twitter);
        a_w_userPoints   = findViewById(R.id.a_w_userPoints);
        a_w_refellarLink = findViewById(R.id.a_w_refellarLink);
        a_w_waletHistory = findViewById(R.id.a_w_waletHistory);
        a_w_waletHistory.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        a_w_waletHistory.setHasFixedSize(true);
        a_w_refellarLink.setText(userRefellar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("WALLETS").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PointsModel pointsModel = snapshot.getValue(PointsModel.class);
                        int x = Integer.parseInt(pointsModel.getPoints());
                        a_w_userPoints.setText(Integer.toString(x / 100) + " جنيه ");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show(); }
                });

        createAcountQR();
        showHistory();
    }

    private void createAcountQR() {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(phoneNumber, BarcodeFormat.QR_CODE, 300, 300);
            Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);
            for (int x = 0; x<300; x++){
                for (int y=0; y<300; y++){ bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE); }
            }
            a_w_qrImage.setImageBitmap(bitmap);
        } catch (Exception e) { e.printStackTrace(); }
    }


    private void showHistory() {
        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>().setQuery(ORDERS_DB.child(phoneNumber),OrderModel.class).setLifecycleOwner(this).build();
        FirebaseRecyclerAdapter<OrderModel, HistoryAdapter> adapter =
                new FirebaseRecyclerAdapter<OrderModel, HistoryAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HistoryAdapter holder, int position, @NonNull OrderModel model) {
                        String orderId   = model.getOrderId();
                        String orderStatus=model.getOrderStatus();
                        String date      = model.getDate();
                        String time      = model.getTime();
                        holder.showHistory(orderId,orderStatus,date,time);
                        holder.itemView.setOnClickListener(v -> {
                            if(!orderId.equals("1")){ showOrderList(orderId); }
                        });
                    }
                    @NonNull
                    @Override
                    public HistoryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new HistoryAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history_item, parent, false));
                    }
                };
        a_w_waletHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    private void showOrderList(String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                new FirebaseRecyclerOptions.Builder<CartModel>().setQuery(ARCHIVE_DB.child(phoneNumber).child(orderId) , CartModel.class).setLifecycleOwner(this).build();
        FirebaseRecyclerAdapter<CartModel,HistoryAdapter> adapter =
                new FirebaseRecyclerAdapter<CartModel,HistoryAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HistoryAdapter holder, int position, @NonNull CartModel model) {
                        String productId       = model.getProductId();
                        String productQuantity = model.getProductQuantity();
                        String productSize     = model.getProductSize();
                        String productTopping  = model.getProductTopping();
                        String productPrice    = model.getProductPrice();
                        holder.showList(productId,productQuantity,productSize,productTopping,productPrice);
                    }
                    @NonNull
                    @Override
                    public HistoryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new HistoryAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_list_item, parent, false));
                    }
                };
        o_l_l_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }


    ///////////////////////////////////
    ////////  HistoryAdapter  /////////
    ///////////////////////////////////
    private static class HistoryAdapter extends RecyclerView.ViewHolder {

        public HistoryAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public void showHistory(String orderId, String orderStatus, String date, String time) {
            TextView c_h_i_historyDateTime   = itemView.findViewById(R.id.c_h_i_historyDateTime);
            TextView c_h_i_historyDescription= itemView.findViewById(R.id.c_h_i_historyDescription);
            c_h_i_historyDateTime.setText(date+"\n"+time);
            if(orderId.equals("1")){c_h_i_historyDescription.setText("OrderStatus:"+orderStatus); }
            else{ c_h_i_historyDescription.setText("OrderId:"+orderId+"\nOrderStatus:"+orderStatus); }
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