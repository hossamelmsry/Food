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
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.elzayet.food.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FavoritesFragment extends Fragment {
    //database
    private final DatabaseReference PRODUCTS_DB = FirebaseDatabase.getInstance().getReference("PRODUCTS");
    private final DatabaseReference FAVORITES_DB= FirebaseDatabase.getInstance().getReference("FAVORITES");
    private final DatabaseReference CARTS_DB    = FirebaseDatabase.getInstance().getReference("CARTS");

    private TextView f_f_warningMsg;
    private RecyclerView f_f_recyclerView;
    //user account
    private String phoneNumber ;
    public FavoritesFragment() {   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        f_f_warningMsg   = view.findViewById(R.id.f_f_warningMsg);
        f_f_recyclerView = view.findViewById(R.id.f_f_recyclerView);
        f_f_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        f_f_recyclerView.setHasFixedSize(true);

        //user account
        SharedPreferences pref = getContext().getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        showFavorites();
        return view;
    }

    //Cart
    private void showFavorites() {
        FirebaseRecyclerOptions<FavoriteModel> options =
                new FirebaseRecyclerOptions.Builder<FavoriteModel>().setQuery(FAVORITES_DB.child(phoneNumber) , FavoriteModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
        FirebaseRecyclerAdapter<FavoriteModel, FavoritesFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<FavoriteModel, FavoritesFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FavoritesFragmentAdapter holder, int position, @NonNull FavoriteModel model) {
                        String productId = model.getProductId();
                        holder.showCart(productId);

                        PRODUCTS_DB.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    ProductModel productModel = snapshot.getValue(ProductModel.class);
                                    String totlaPrice = productModel.getSmallSize();
                                    holder.c_f_i_addToCart.setOnClickListener(v -> {
                                        CARTS_DB.child(phoneNumber).child(productId).setValue(new CartModel(productId));
                                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                    });
                                }else {
                                    Toast.makeText(getContext(), "sorry ,this product not avalible", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        holder.c_f_i_remove.setOnClickListener(v -> {
                            FAVORITES_DB.child(phoneNumber).child(productId).removeValue();
                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        });

                    }
                    @NonNull
                    @Override
                    public FavoritesFragmentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new FavoritesFragmentAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorite_item, parent, false));
                    }
                };
        f_f_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }


    ///////////////////////////////////
    ///////CartFragmentAdapter/////////
    ///////////////////////////////////
    private static class FavoritesFragmentAdapter extends RecyclerView.ViewHolder {

        public FavoritesFragmentAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public TextView c_f_i_addToCart;
        public ImageView c_f_i_remove;
        public void showCart(String productId) {
            ImageView c_f_i_productImage= itemView.findViewById(R.id.c_f_i_productImage);
            TextView c_f_i_productName  = itemView.findViewById(R.id.c_f_i_productName);
            TextView c_f_i_productPrice = itemView.findViewById(R.id.c_f_i_productPrice);
            c_f_i_addToCart             = itemView.findViewById(R.id.c_f_i_addToCart);
            c_f_i_remove                = itemView.findViewById(R.id.c_f_i_remove);

            FirebaseDatabase.getInstance().getReference("PRODUCTS").child(productId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                ProductModel productModel = snapshot.getValue(ProductModel.class);
                                Picasso.get().load(productModel.getProductImage()).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_f_i_productImage);
                                c_f_i_productName.setText(productModel.getProductName());
//                                c_f_i_productPrice.setText(productModel.getProductPrice()+" جنيه ");
                            } else { Toast.makeText(itemView.getContext(), "لا يوجد هذا المنتج في الوقت الحالي", Toast.LENGTH_SHORT).show(); }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(itemView.getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}