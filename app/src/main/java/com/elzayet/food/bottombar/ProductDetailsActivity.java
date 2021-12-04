package com.elzayet.food.bottombar;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.CartModel;
import com.elzayet.food.FavoriteModel;
import com.elzayet.food.R;
import com.elzayet.food.ShareModel;
import com.elzayet.food.sidebar.registration.LoginActivity;
import com.elzayet.food.sidebar.registration.RegistrationActivity;
import com.elzayet.food.sidebar.registration.SignupActivity;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    // database
    private final DatabaseReference FAVORITES_DB= FirebaseDatabase.getInstance().getReference("FAVORITES");
    private final DatabaseReference SHARE_DB    = FirebaseDatabase.getInstance().getReference("SHARE");
    private final DatabaseReference CARTS_DB    = FirebaseDatabase.getInstance().getReference("CARTS");
    //xml
    private ImageView a_p_d_productImage;
    private TextView a_p_d_productName ,a_p_d_productPrice, a_p_d_quantityNumber;
    private Button a_p_d_buyProduct ;
    //product Details
    private String productId,productImage,productName,productPrice ;
    private int quantity = 1;
    //user account
    String phoneNumber ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //intent initialization
        productId     = getIntent().getStringExtra("productId");
        productImage  = getIntent().getStringExtra("productImage");
        productName   = getIntent().getStringExtra("productName");
        productPrice  = getIntent().getStringExtra("productPrice");
        //user account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber = pref.getString("phoneNumber", "NOTHING");
        //xml initialization
        a_p_d_productImage = findViewById(R.id.a_p_d_productImage);
        a_p_d_productName  = findViewById(R.id.a_p_d_productName);
        a_p_d_productPrice = findViewById(R.id.a_p_d_productPrice);
        a_p_d_quantityNumber= findViewById(R.id.a_p_d_quantityNumber);
        a_p_d_buyProduct   = findViewById(R.id.a_p_d_buyProduct);

        Picasso.get().load(productImage).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(a_p_d_productImage);
        a_p_d_productName.setText(productName);
        a_p_d_productPrice.setText(productPrice + " " + Integer.parseInt(productPrice) * 100);

        findViewById(R.id.a_p_d_back).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.a_p_d_addQuantity).setOnClickListener(v -> addQuantitySystem());
        findViewById(R.id.a_p_d_subQuantity).setOnClickListener(v -> subQuantitySystem());
        findViewById(R.id.a_p_d_addToCart).setOnClickListener(v -> addTo(productId,"CAETS"));
        findViewById(R.id.a_p_d_share).setOnClickListener(v -> addTo(productId,"SHARE"));
        findViewById(R.id.a_p_d_favorite).setOnClickListener(v -> addTo(productId,"FAVORITES"));

        MaterialButtonToggleGroup toggleButton = findViewById(R.id.toggleButton);
        toggleButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                    if (isChecked) {
                        switch (checkedId){
                            case R.id.btnAndroid:
                                Toast.makeText(getBaseContext(), "btnAndroid", Toast.LENGTH_SHORT).show();
                            break;

                            case R.id.btnFlutter:
                                Toast.makeText(getBaseContext(), "btnFlutter", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.btnWeb:
                                Toast.makeText(getBaseContext(), "btnWeb", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else{
                        Toast.makeText(getBaseContext(), "nooooooooo", Toast.LENGTH_SHORT).show();
                    }
        });



//        toggleButton.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
//
//            if (isChecked) {
//                when (checkedId) {
//
//                    R.id.btnFlutter -> showToast("It's a Butterfly thing.")
//                    R.id.btnWeb -> showToast("You still exist?")
//                }
//            } else {
//
//                    showToast("Nothing Selected")
//                }
//            }
//        }

    }

    /*

     */

    private void subQuantitySystem() {
        if(quantity == 1){
            quantity =1 ;
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
        }
        else if (quantity <= 10){
            quantity -- ;
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
        }
    }

    private void addQuantitySystem() {
        if (quantity >= 10){
            quantity =10 ;
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
        }else {
            quantity ++ ;
            a_p_d_quantityNumber.setText(Integer.toString(quantity));
        }
    }

    private void addTo(String productId,String child) {
        if(!phoneNumber.equals("NOTHING")) {
            if (child.equals("FAVORITES")) {
                FAVORITES_DB.child(phoneNumber).child(productId).setValue(new FavoriteModel(productId));
                Toast.makeText(getBaseContext(), "Add To Favorites Successfully", Toast.LENGTH_SHORT).show();
            } else if(child.equals("SHARE")){
                SHARE_DB.child(phoneNumber).child(productId).setValue(new ShareModel(productId));
                Toast.makeText(getBaseContext(), "Please Wait To Loading", Toast.LENGTH_SHORT).show();
            }else{
                CARTS_DB.child(phoneNumber).child(productId).setValue(new CartModel(productId,"1"));
                Toast.makeText(getBaseContext(), "Add To Favorites Successfully", Toast.LENGTH_SHORT).show();
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


}