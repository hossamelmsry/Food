package com.elzayet.food.topbar.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {

    private TextView a_s_warningMsg;
    private RecyclerView a_s_recyclerView;
    private EditText a_s_searchInput;
    private ImageView a_s_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        a_s_warningMsg  = findViewById(R.id.a_s_warningMsg);
        a_s_searchInput = findViewById(R.id.a_s_searchInput);
        a_s_search      = findViewById(R.id.a_s_search);
        a_s_recyclerView= findViewById(R.id.a_s_recyclerView);
        a_s_recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
        a_s_recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        a_s_search.setOnClickListener(v -> validation());
    }

    private void validation() {
        String productSearch= a_s_searchInput.getText().toString().trim();
        if(!TextUtils.isEmpty(productSearch)) {
            a_s_searchInput.setError(null);
            result(productSearch);
        }else{
            a_s_searchInput.setError(getString(R.string.search));
            a_s_searchInput.requestFocus();
        }
    }

    private void result(String productSearch) {
        DatabaseReference PRODUCTS_DB = FirebaseDatabase.getInstance().getReference("PRODUCTS");
        FirebaseRecyclerOptions<ProductModel> options =
                new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(PRODUCTS_DB.orderByChild("productName").equalTo(productSearch) , ProductModel.class).setLifecycleOwner(this).build();

        FirebaseRecyclerAdapter<ProductModel, productsAdapter> adapter =
                new FirebaseRecyclerAdapter<ProductModel, productsAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull productsAdapter holder, int position, @NonNull ProductModel model) {
                        String productImage = model.getProductImage();
                        String productName = model.getProductName();
                        String productPrice = model.getProductPrice();
                        holder.showProduct(productImage,productName,productPrice);

                    }
                    @NonNull
                    @Override
                    public productsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new productsAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_item, parent, false));
                    }
                };
        a_s_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    private static class productsAdapter extends RecyclerView.ViewHolder {
        private final DatabaseReference PRODUCTS_DB = FirebaseDatabase.getInstance().getReference("PRODUCTS");
        private String count ;

        public productsAdapter(@NonNull View itemView) {
            super(itemView);
        }

        public ImageView c_p_i_favorite ;
        public ImageView c_p_i_share ;
        public void showProduct(String productImage, String productName, String productPrice) {
            c_p_i_favorite = itemView.findViewById(R.id.c_p_i_favorite);
            c_p_i_share = itemView.findViewById(R.id.c_p_i_share);
            ImageView c_p_i_productImage= itemView.findViewById(R.id.c_p_i_productImage);
            TextView c_p_i_productName  = itemView.findViewById(R.id.c_p_i_productName);
            TextView c_p_i_productPrice = itemView.findViewById(R.id.c_p_i_productPrice);
            TextView c_p_i_productPoints= itemView.findViewById(R.id.c_p_i_productPoints);

            Picasso.get().load(productImage).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_p_i_productImage);
            c_p_i_productName.setText(productName);
            c_p_i_productPrice.setText(productPrice+" جنيه ");
            c_p_i_productPoints.setText(Integer.toString(Integer.parseInt(productPrice) * 100));
        }

    }
}