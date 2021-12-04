package com.elzayet.food.bottombar;

import static android.content.Context.MODE_PRIVATE;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.CartModel;
import com.elzayet.food.FavoriteModel;
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

public class CartFragment extends Fragment {
    private final DatabaseReference FAVORITES_DB= FirebaseDatabase.getInstance().getReference("FAVORITES");
    private final DatabaseReference CARTS_DB    = FirebaseDatabase.getInstance().getReference("CARTS");

    private TextView f_c_warningMsg;
    private RecyclerView f_c_recyclerView;
    //user account
    private String phoneNumber ;

    public CartFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view        = inflater.inflate(R.layout.fragment_cart, container, false);
        f_c_warningMsg   = view.findViewById(R.id.f_c_warningMsg);
        f_c_recyclerView = view.findViewById(R.id.f_c_recyclerView);
        f_c_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        f_c_recyclerView.setHasFixedSize(true);
        //user account
        SharedPreferences pref = getContext().getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        showCart();
        return view ;
    }

    private int quantity;
    //Cart
    private void showCart() {
        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>().setQuery(CARTS_DB.child(phoneNumber) , CartModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
        FirebaseRecyclerAdapter<CartModel,CartFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<CartModel, CartFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartFragmentAdapter holder, int position, @NonNull CartModel model) {
                        String productId       = model.getProductId();
                        String productQuantity = model.getProductQuantity();
                        holder.showCart(productId,productQuantity);
                        quantity = Integer.parseInt(productQuantity);
                        holder.c_c_i_addQuantity.setOnClickListener(v -> {
                            if (quantity >= 10){
                                quantity =10 ;
                                holder.c_c_i_quantityNumber.setText(Integer.toString(quantity));
                            }else {
                                quantity ++ ;
                                holder.c_c_i_quantityNumber.setText(Integer.toString(quantity));
                            }
                        });

                        holder.c_c_i_subQuantity.setOnClickListener(v -> {
                            if(quantity == 1){
                                quantity =1 ;
                                holder.c_c_i_quantityNumber.setText(Integer.toString(quantity));
                            }
                            else if (quantity <= 10){
                                quantity -- ;
                                holder.c_c_i_quantityNumber.setText(Integer.toString(quantity));
                            }
                        });

                        holder.c_c_i_addToFavorite.setOnClickListener(v -> {
                            FAVORITES_DB.child(phoneNumber).child(productId).setValue(new FavoriteModel(productId));
                            Toast.makeText(getContext(), "Add To Favorites Successfully", Toast.LENGTH_SHORT).show();
                        });

                        holder.c_c_i_remove.setOnClickListener(v -> {
                            CARTS_DB.child(phoneNumber).child(productId).removeValue();
                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        });
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

    ///////////////////////////////////
    ///////CartFragmentAdapter/////////
    ///////////////////////////////////
    private static class CartFragmentAdapter extends RecyclerView.ViewHolder {

        public CartFragmentAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public ImageView c_c_i_addQuantity,c_c_i_subQuantity;
        public TextView c_c_i_quantityNumber,c_c_i_addToFavorite,c_c_i_remove;
        public void showCart(String productId, String productQuantity) {
            c_c_i_addQuantity= itemView.findViewById(R.id.c_c_i_addQuantity);
            c_c_i_subQuantity= itemView.findViewById(R.id.c_c_i_subQuantity);
            ImageView c_c_i_productImage= itemView.findViewById(R.id.c_c_i_productImage);
            TextView c_c_i_productName  = itemView.findViewById(R.id.c_c_i_productName);
            TextView c_c_i_productPrice = itemView.findViewById(R.id.c_c_i_productPrice);
            TextView c_c_i_productPoints= itemView.findViewById(R.id.c_c_i_productPoints);
            c_c_i_quantityNumber= itemView.findViewById(R.id.c_c_i_quantityNumber);
            c_c_i_addToFavorite = itemView.findViewById(R.id.c_c_i_addToFavorite);
            c_c_i_remove        = itemView.findViewById(R.id.c_c_i_remove);

            FirebaseDatabase.getInstance().getReference("PRODUCTS").child(productId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                ProductModel productModel = snapshot.getValue(ProductModel.class);
                                Picasso.get().load(productModel.getProductImage()).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_c_i_productImage);
                                c_c_i_productName.setText(productModel.getProductName());
                                c_c_i_productPrice.setText(productModel.getProductPrice()+" جنيه ");
                                c_c_i_productPoints.setText(Integer.toString(Integer.parseInt(productModel.getProductPrice()) * 100));
                            } else {
                                Toast.makeText(itemView.getContext(), "لا يوجد هذا المنتج في الوقت الحالي", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(itemView.getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }
}