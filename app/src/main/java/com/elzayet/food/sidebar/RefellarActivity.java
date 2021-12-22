package com.elzayet.food.sidebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.PointsModel;
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.elzayet.food.bottombar.ProductDetailsActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RefellarActivity extends AppCompatActivity {

    private TextView a_r_userPoints,a_r_totalInvitaions,a_r_userRefellarLink;
    private ImageView a_r_invite_facebook,a_r_invite_instagram,a_r_invite_twitter;
    private RecyclerView a_r_recyclerView;
    //User Account
    private String phoneNumber,userRefellar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refellar);
        //user Account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        userRefellar           = pref.getString("userRefellar", "NOTHING");

        a_r_userPoints       = findViewById(R.id.a_r_userPoints);
        a_r_totalInvitaions  = findViewById(R.id.a_r_totalInvitaions);
        a_r_userRefellarLink = findViewById(R.id.a_r_userRefellarLink);
        a_r_invite_facebook  = findViewById(R.id.a_r_invite_facebook);
        a_r_invite_instagram = findViewById(R.id.a_r_invite_instagram);
        a_r_invite_twitter   = findViewById(R.id.a_r_invite_twitter);
        a_r_recyclerView     = findViewById(R.id.a_r_recyclerView);

        a_r_recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        a_r_recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        a_r_userRefellarLink.setText(userRefellar);

        FirebaseDatabase.getInstance().getReference("WALLETS").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PointsModel pointsModel = snapshot.getValue(PointsModel.class);
                        a_r_userPoints.setText(pointsModel.getPoints());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show(); }
                });

        FirebaseDatabase.getInstance().getReference("REFELLARS").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            long x = snapshot.getChildrenCount();
                            a_r_totalInvitaions.setText(Integer.toString((int) x));
                        }else{ a_r_totalInvitaions.setText("Total Invitaions = 0"); }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show(); }
                });

    }

//    private void showProducts() {
//        FirebaseRecyclerOptions<ProductModel> options =
//                new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(PRODUCTS_DB , ProductModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
//
//        FirebaseRecyclerAdapter<ProductModel, HomeFragmentAdapter> adapter =
//                new FirebaseRecyclerAdapter<ProductModel, HomeFragmentAdapter>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull HomeFragmentAdapter holder, int position, @NonNull ProductModel model) {
//                    }
//                    @NonNull
//                    @Override
//                    public HomeFragmentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        return new HomeFragmentAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_item, parent, false));
//                    }
//                };
//        f_h_homeContainerRecycler.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        adapter.startListening();
//    }
//    ////
//    private static class ProductsAdapter extends RecyclerView.ViewHolder {
//
//        public HomeFragmentAdapter(@NonNull View itemView) {
//            super(itemView);
//        }
//
//        public ImageView c_p_i_favorite ;
//        public ImageView c_p_i_share ;
//        public void showProduct(String productImage, String productName, String productPrice, String productPoints) {
//            c_p_i_favorite = itemView.findViewById(R.id.c_p_i_favorite);
//            c_p_i_share = itemView.findViewById(R.id.c_p_i_share);
//            ImageView c_p_i_productImage= itemView.findViewById(R.id.c_p_i_productImage);
//            TextView c_p_i_productName  = itemView.findViewById(R.id.c_p_i_productName);
//            TextView c_p_i_productPrice = itemView.findViewById(R.id.c_p_i_productPrice);
//            TextView c_p_i_productPoints= itemView.findViewById(R.id.c_p_i_productPoints);
//
//            Picasso.get().load(productImage).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_p_i_productImage);
//            c_p_i_productName.setText(productName);
//            c_p_i_productPrice.setText(" جنيه "+productPrice);
//            c_p_i_productPoints.setText(productPoints);
//        }
//    }

}