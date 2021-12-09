package com.elzayet.food.bottombar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.CartModel;
import com.elzayet.food.FavoriteModel;
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.elzayet.food.ShareModel;
import com.elzayet.food.UserModel;
import com.elzayet.food.sidebar.registration.LoginActivity;
import com.elzayet.food.sidebar.registration.RegistrationActivity;
import com.elzayet.food.sidebar.registration.SignupActivity;
import com.elzayet.food.tools.Session;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    //database initialization
    private final DatabaseReference FAVORITES_DB= FirebaseDatabase.getInstance().getReference("FAVORITES");
    private final DatabaseReference SHARE_DB    = FirebaseDatabase.getInstance().getReference("SHARE");
    private final DatabaseReference CARTS_DB    = FirebaseDatabase.getInstance().getReference("CARTS");
    //xml initialization
    private ImageView a_p_d_productImage;
    private TextView a_p_d_productName,a_p_d_quantityNumber,a_p_d_totalPrice;
    private CheckBox a_p_d_topping1,a_p_d_topping2,a_p_d_topping3;
    private Button a_p_d_buyProduct;
    private MaterialButtonToggleGroup a_p_d_toggleSize;
    //product Details initialization
    private String productId,productImage,productName,smallSize,mediumSize,largeSize;
    //cart initialization
    private String totalPrice ,productQuantity,productSize;
    private int sizePrice=0,quantity=0;
    //user account
    String phoneNumber ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //intent initialization
        productId      = getIntent().getStringExtra("productId");
        productQuantity=getIntent().getStringExtra("productQuantity");
        if(productQuantity != null){ quantity = Integer.parseInt(productQuantity); }
        else{ quantity = 1; }
        //user account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber = pref.getString("phoneNumber", "NOTHING");
        //xml initialization
        a_p_d_productImage = findViewById(R.id.a_p_d_productImage);
        a_p_d_productName  = findViewById(R.id.a_p_d_productName);
        a_p_d_quantityNumber=findViewById(R.id.a_p_d_quantityNumber);
        a_p_d_totalPrice   = findViewById(R.id.a_p_d_totalPrice);
        a_p_d_toggleSize   = findViewById(R.id.a_p_d_toggleSize);
        a_p_d_buyProduct   = findViewById(R.id.a_p_d_buyProduct);
        a_p_d_topping1     = findViewById(R.id.a_p_d_topping1);
        a_p_d_topping2     = findViewById(R.id.a_p_d_topping2);
        a_p_d_topping3     = findViewById(R.id.a_p_d_topping3);
        //
        findViewById(R.id.a_p_d_back).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.a_p_d_addQuantity).setOnClickListener(v -> addQuantitySystem());
        findViewById(R.id.a_p_d_subQuantity).setOnClickListener(v -> subQuantitySystem());
        findViewById(R.id.a_p_d_addToCart).setOnClickListener(v -> addTo(productId,"CAETS"));
        findViewById(R.id.a_p_d_share).setOnClickListener(v -> addTo(productId,"SHARE"));
        findViewById(R.id.a_p_d_favorite).setOnClickListener(v -> addTo(productId,"FAVORITES"));

        //

    }

    @Override
    protected void onStart() {
        super.onStart();
        //
        FirebaseDatabase.getInstance().getReference("PRODUCTS").child(productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ProductModel productModel = snapshot.getValue(ProductModel.class);
                            productName = productModel.getProductName();
                            productImage= productModel.getProductImage();
                            smallSize   = productModel.getSmallSize();
                            mediumSize  = productModel.getMediumSize();
                            largeSize   = productModel.getLargeSize();
                            Picasso.get().load(productImage).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(a_p_d_productImage);
                            a_p_d_productName.setText(productName);
                            sizePrice = Integer.parseInt(smallSize);
                        }else {  Toast.makeText(getBaseContext(), "not exists", Toast.LENGTH_SHORT).show();  }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {   Toast.makeText(getBaseContext(), error.getCode(), Toast.LENGTH_SHORT).show();   }
                });

        a_p_d_toggleSize.check(R.id.a_p_d_smallSize);


    }

    private void subQuantitySystem() {
        if (quantity == 1) {
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
            productQuantity = Integer.toString(quantity);
        }
        else if (quantity <= 10){
            quantity -- ;
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
            productQuantity = Integer.toString(quantity);
        }
    }

    private void addQuantitySystem() {
        if (quantity >= 10) {
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
            productQuantity = Integer.toString(quantity);
        }
        else {
            quantity ++ ;
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
            productQuantity = Integer.toString(quantity);
        }
    }

    private void addTo(String productId,String child) {
        if(!phoneNumber.equals("NOTHING")) {
            if (child.equals("FAVORITES")) {
                FAVORITES_DB.child(phoneNumber).child(productId).setValue(new FavoriteModel(productId));
                Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_SHORT).show();
            } else if(child.equals("SHARE")){
                SHARE_DB.child(phoneNumber).child(productId).setValue(new ShareModel(productId));
                Toast.makeText(getBaseContext(), "Please Wait To Loading", Toast.LENGTH_SHORT).show();
            }else{
                CARTS_DB.child(phoneNumber).child(productId).setValue(new CartModel(productId,productQuantity,productSize,"topping",totalPrice));
                Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
            builder.setTitle(R.string.please_register_frist);
            builder.setIcon(R.drawable.ic_photo_24);
            builder.setPositiveButton(R.string.registration, (dialog, which) -> callRegistration());
            builder.setNeutralButton(R.string.skip, (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    private void callRegistration() {
        startActivity(new Intent(getBaseContext(), RegistrationActivity.class));
        finish();
    }

    public void calculateTotalPrice(View view) {
        a_p_d_toggleSize.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                switch (checkedId) {
                    case R.id.a_p_d_smallSize : sizePrice = Integer.parseInt(smallSize);
                        break;
                    case R.id.a_p_d_mediumSize: sizePrice = Integer.parseInt(mediumSize);
                        break;
                    case R.id.a_p_d_largeSize : sizePrice = Integer.parseInt(largeSize);
                        break;
                }
            }
        });
        int topping_ = 0;
        String topping="";
        if(a_p_d_topping1.isChecked()){
            topping_+=2;
            topping =" tomato ";
        }
        if(a_p_d_topping2.isChecked()){
            topping_+=2;
            topping = topping + "+ catchap ";
        }
        if(a_p_d_topping3.isChecked()){
            topping_+=2;
            topping = topping + "+ meat ";
        }
        int y = quantity * sizePrice + topping_ ;
        Toast.makeText(getBaseContext(), ""+y, Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(), topping, Toast.LENGTH_SHORT).show();
        productQuantity = Integer.toString(quantity);
    }
}