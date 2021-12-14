package com.elzayet.food.bottombar;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.ArchiveModel;
import com.elzayet.food.CartModel;
import com.elzayet.food.FavoriteModel;
import com.elzayet.food.OrderModel;
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.elzayet.food.UserModel;
import com.elzayet.food.bottombar.home.HomeFragment;
import com.elzayet.food.sidebar.registration.LoginActivity;
import com.elzayet.food.tools.Session;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CartFragment extends Fragment {
    // database initia
    private final DatabaseReference FAVORITES_DB= FirebaseDatabase.getInstance().getReference("FAVORITES");
    private final DatabaseReference CARTS_DB    = FirebaseDatabase.getInstance().getReference("CARTS");
    private final DatabaseReference ORDERS_DB   = FirebaseDatabase.getInstance().getReference("ORDERS");
    private final DatabaseReference ARCHIVE_DB  = FirebaseDatabase.getInstance().getReference("ARCHIVE");
    // xml initia
    private RecyclerView f_c_recyclerView;
    private LinearLayout f_c_ll_orderDetails;
    private TextView f_c_warningMsg,f_c_o_d_id,f_c_o_d_price,f_c_o_d_usePromoCode,f_c_o_d_phoneNumber;
    // product ininti
    private String productId,productQuantity,productSize;
    // order initia
    private String orderId ,orderPrice,orderTopping;
    private int price ;
    // user account
    private String phoneNumber ;

    public CartFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view           = inflater.inflate(R.layout.fragment_cart, container, false);
        f_c_warningMsg      = view.findViewById(R.id.f_c_warningMsg);
        f_c_ll_orderDetails = view.findViewById(R.id.f_c_ll_orderDetails);
        f_c_o_d_id          = view.findViewById(R.id.f_c_o_d_id);
        f_c_o_d_price       = view.findViewById(R.id.f_c_o_d_price);
        f_c_o_d_phoneNumber = view.findViewById(R.id.f_c_o_d_phoneNumber);
//        f_c_o_d_usePromoCode= view.findViewById(R.id.f_c_o_d_usePromoCode);
        f_c_recyclerView    = view.findViewById(R.id.f_c_recyclerView);
        f_c_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        f_c_recyclerView.setHasFixedSize(true);
        //user account
        SharedPreferences pref = getContext().getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        view.findViewById(R.id.f_c_o_d_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrder();
            }
        });
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        f_c_o_d_phoneNumber.setText("phoneNumber:"+phoneNumber);
        CARTS_DB.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    orderId = CARTS_DB.child(phoneNumber).push().getKey();
                    f_c_ll_orderDetails.setVisibility(View.VISIBLE);
                    f_c_o_d_id.setText("Order Num:"+orderId);
                }else{  f_c_ll_orderDetails.setVisibility(View.GONE); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        price = 0;
        showCart();
    }

    //Cart
    private void showCart() {
        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>().setQuery(CARTS_DB.child(phoneNumber) , CartModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
        FirebaseRecyclerAdapter<CartModel,CartFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<CartModel, CartFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartFragmentAdapter holder, int position, @NonNull CartModel model) {
                        productId       = model.getProductId();
                        productQuantity = model.getProductQuantity();
                        productSize     = model.getProductSize();
                        orderTopping    = model.getOrderTopping();
                        orderPrice      = model.getOrderPrice();
                        price += Integer.parseInt(orderPrice);
                        holder.showCart(productId,productQuantity,productSize,orderTopping,orderPrice);
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext() , ProductDetailsActivity.class);
                            intent.putExtra("productId"      ,productId);
                            intent.putExtra("productQuantity",productQuantity);
                            intent.putExtra("productSize"    ,productSize);
                            intent.putExtra("orderTopping"   ,orderTopping);
                            intent.putExtra("orderPrice"     ,orderPrice);
                            startActivity(intent);
                        });
                        holder.c_c_i_addToFavorite.setOnClickListener(v -> {
                            FAVORITES_DB.child(phoneNumber).child(productId).setValue(new FavoriteModel(productId));
                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        });
                        holder.c_c_i_remove.setOnClickListener(v -> {
                            CARTS_DB.child(phoneNumber).child(productId).removeValue();
                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                            onStart();
                        });
                        f_c_o_d_price.setText("Order Price:"+price+"|"+Integer.toString(price*100));
                    }
                    @NonNull
                    @Override
                    public CartFragmentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new CartFragmentAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart_item, parent, false));
                    }
                };
        f_c_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    private void confirmOrder() {
        Toast.makeText(getContext(), "تم تاكيد الطلب", Toast.LENGTH_SHORT).show();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("hh:mm:ss a").format(new Date());
        CARTS_DB.child(phoneNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ORDERS_DB.child(phoneNumber).child(orderId).setValue(new OrderModel(orderId,orderPrice,orderTopping,productId,date,time));
//                ARCHIVE_DB.child(phoneNumber).child(orderId).setValue(new ArchiveModel(orderId,orderId,date,time));

                for(DataSnapshot ds : snapshot.getChildren()) {
                    CartModel cartModel = ds.getValue(CartModel.class);
                    productId       = cartModel.getProductId();
                    productQuantity = cartModel.getProductQuantity();
                    productSize     = cartModel.getProductSize();
                    orderTopping    = cartModel.getOrderTopping();
                    orderPrice      = cartModel.getOrderPrice();
                    ARCHIVE_DB.child(phoneNumber).child(orderId).child(productId).setValue(new ArchiveModel(orderId,orderPrice,orderTopping,productId,productQuantity,productSize,date,time));
                    CARTS_DB.child(phoneNumber).child(productId).removeValue();
                }
                onStart();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    ///////////////////////////////////
    ///////CartFragmentAdapter/////////
    ///////////////////////////////////
    private static class CartFragmentAdapter extends RecyclerView.ViewHolder {

        public CartFragmentAdapter(@NonNull View itemView) { super(itemView);  }

        public ImageView c_c_i_remove;
        public TextView c_c_i_addToFavorite;
        public void showCart(String productId, String productQuantity, String productSize, String orderTopping, String orderPrice) {
            ImageView c_c_i_productImage  = itemView.findViewById(R.id.c_c_i_productImage);
            TextView c_c_i_productName    = itemView.findViewById(R.id.c_c_i_productName);
            TextView c_c_i_cartDescription= itemView.findViewById(R.id.c_c_i_cartDescription);
            c_c_i_addToFavorite           = itemView.findViewById(R.id.c_c_i_addToFavorite);
            c_c_i_remove                  = itemView.findViewById(R.id.c_c_i_remove);

            FirebaseDatabase.getInstance().getReference("PRODUCTS").child(productId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                ProductModel productModel = snapshot.getValue(ProductModel.class);
                                Picasso.get().load(productModel.getProductImage()).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_c_i_productImage);
                                c_c_i_productName.setText(productQuantity + "|"+productModel.getProductName()+"|"+productSize);
                                c_c_i_cartDescription.setText( "Topping:"+orderTopping+"\n"+"Order Price:"+orderPrice);
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