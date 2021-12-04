package com.elzayet.food.bottombar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CategoryActivity extends AppCompatActivity {
    private final DatabaseReference PRODUCTS_DB = FirebaseDatabase.getInstance().getReference("PRODUCTS");
    private RecyclerView a_c_recyclerView ;
    private String menuName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        menuName = getIntent().getStringExtra("menuName");

        a_c_recyclerView = findViewById(R.id.a_c_recyclerView);
        a_c_recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
        a_c_recyclerView.setHasFixedSize(true);
        showProducts();
    }
    private void showProducts() {
        DatabaseReference PRODUCTS_DB = FirebaseDatabase.getInstance().getReference("PRODUCTS");
        FirebaseRecyclerOptions<ProductModel> options =
                new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(PRODUCTS_DB.orderByChild("menuName").equalTo(menuName) , ProductModel.class).setLifecycleOwner((LifecycleOwner) this).build();

        FirebaseRecyclerAdapter<ProductModel, productAdapter> adapter =
                new FirebaseRecyclerAdapter<ProductModel, productAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull productAdapter holder, int position, @NonNull ProductModel model) {
                        String menuName    = model.getMenuName();
                        String productId    = model.getProductId();
                        String productImage = model.getProductImage();
                        String productName  = model.getProductName();
                        String productDescription  = model.getProductDescription();
                        String productPrice = model.getProductPrice();
                        holder.showProduct(productImage,productName,productPrice);
                        holder.itemView.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), ProductDetailsActivity.class)));
                    }
                    @NonNull
                    @Override
                    public productAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new productAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_item, parent, false));
                    }
                };
        a_c_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    private static class productAdapter extends RecyclerView.ViewHolder {

        public productAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public ImageView c_p_i_favorite ;
        public ImageView c_p_i_share ;
        public void showProduct(String productImage, String productName, String productPrice ) {
            c_p_i_favorite = itemView.findViewById(R.id.c_p_i_favorite);
            c_p_i_share = itemView.findViewById(R.id.c_p_i_share);
            ImageView c_p_i_productImage = itemView.findViewById(R.id.c_p_i_productImage);
            TextView c_p_i_productName = itemView.findViewById(R.id.c_p_i_productName);
            TextView c_p_i_productPrice = itemView.findViewById(R.id.c_p_i_productPrice);
            TextView c_p_i_productPoints = itemView.findViewById(R.id.c_p_i_productPoints);

            Picasso.get().load(productImage).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_p_i_productImage);
            c_p_i_productName.setText(productName);
            c_p_i_productPrice.setText(" جنيه " + productPrice);
            c_p_i_productPoints.setText(Integer.parseInt(productPrice)*100);
        }

    }

}