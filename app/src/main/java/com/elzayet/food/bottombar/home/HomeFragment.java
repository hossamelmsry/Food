package com.elzayet.food.bottombar.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.CartModel;
import com.elzayet.food.FavoriteModel;
import com.elzayet.food.MenuModel;
import com.elzayet.food.PointsModel;
import com.elzayet.food.ProductModel;
import com.elzayet.food.R;
import com.elzayet.food.ShareModel;
import com.elzayet.food.bottombar.CategoryActivity;
import com.elzayet.food.bottombar.ProductDetailsActivity;
import com.elzayet.food.sidebar.registration.LoginActivity;
import com.elzayet.food.sidebar.registration.RegistrationActivity;
import com.elzayet.food.sidebar.registration.SignupActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    //database
    private final DatabaseReference PRODUCTS_DB = FirebaseDatabase.getInstance().getReference("PRODUCTS");
    private final DatabaseReference MENU_DB     = FirebaseDatabase.getInstance().getReference("MENU");
    private final DatabaseReference FAVORITES_DB= FirebaseDatabase.getInstance().getReference("FAVORITES");
    private final DatabaseReference SHARE_DB    = FirebaseDatabase.getInstance().getReference("SHARE");
    private final DatabaseReference CARTS_DB    = FirebaseDatabase.getInstance().getReference("CARTS");
    private View view ;
    //banner slider
    private SliderAdapter sliderAdapter;
    private ArrayList<SliderModel> sliderModelArrayList;
    private FirebaseFirestore db;
    private SliderView f_h_slider ;
    //body
    private RecyclerView f_h_homeContainerRecycler ,a_p_categoryRecycler;
    //user account
    private String phoneNumber ;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        //user account
        SharedPreferences pref = getContext().getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber = pref.getString("phoneNumber", "NOTHING");
        //banner slider
        sliderModelArrayList = new ArrayList<>();
        f_h_slider           = view.findViewById(R.id.f_h_slider);
        db                   = FirebaseFirestore.getInstance();
        loadBanner();
        //Menus
        a_p_categoryRecycler = view.findViewById(R.id.a_p_categoryRecycler);
        a_p_categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        a_p_categoryRecycler.setHasFixedSize(true);
        showMenus();
        //products
        f_h_homeContainerRecycler = view.findViewById(R.id.f_h_homeContainerRecycler);
        f_h_homeContainerRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        f_h_homeContainerRecycler.setHasFixedSize(true);
        showProducts();
        return view;
    }
    //banner slider
    private void loadBanner() {
        db.collection("Slider").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                SliderModel sliderModel = documentSnapshot.toObject(SliderModel.class);
                SliderModel model = new SliderModel();
                model.setImgUrl(sliderModel.getImgUrl());
                sliderModelArrayList.add(model);
                sliderAdapter = new SliderAdapter(getContext(), sliderModelArrayList);
                f_h_slider.setSliderAdapter(sliderAdapter);
                f_h_slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                f_h_slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                f_h_slider.setScrollTimeInSec(3);
                f_h_slider.setAutoCycle(true);
                f_h_slider.startAutoCycle();
            }
        }).addOnFailureListener(e ->  Toast.makeText(getContext(), "Fail to load slider data..", Toast.LENGTH_SHORT).show());
    }
    //Menus
    private void showMenus() {
        FirebaseRecyclerOptions<MenuModel> options =
                new FirebaseRecyclerOptions.Builder<MenuModel>().setQuery(MENU_DB , MenuModel.class).setLifecycleOwner( this).build();
        FirebaseRecyclerAdapter<MenuModel, HomeFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<MenuModel, HomeFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HomeFragmentAdapter holder, int position, @NonNull MenuModel model) {
                        holder.showMenu(model.getMenuImage(),model.getMenuName());
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext() , CategoryActivity.class);
                            intent.putExtra("menuName",model.getMenuName());
                            startActivity(intent);
                        });
                    }
                    @NonNull
                    @Override
                    public HomeFragmentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new HomeFragmentAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category_item, parent, false));
                    }
                };
        a_p_categoryRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }
    //products
    private void showProducts() {
        FirebaseRecyclerOptions<ProductModel> options =
                new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(PRODUCTS_DB , ProductModel.class).setLifecycleOwner((LifecycleOwner) getContext()).build();
        FirebaseRecyclerAdapter<ProductModel, HomeFragmentAdapter> adapter =
                new FirebaseRecyclerAdapter<ProductModel, HomeFragmentAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HomeFragmentAdapter holder, int position, @NonNull ProductModel model) {
                        String productId    = model.getProductId();
                        String productImage = model.getProductImage();
                        String productName  = model.getProductName();
                        String smallSize    = model.getSmallSize();
                        holder.showProduct(productImage,productName);
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext() ,ProductDetailsActivity.class);
                            intent.putExtra("productId",productId);
                            intent.putExtra("productQuantity","1");
                            startActivity(intent);
                        });
                        holder.c_p_i_addToCart.setOnClickListener(v -> addTo(productId,"CARTS",smallSize));
                        holder.c_p_i_favorite.setOnClickListener(v -> addTo(productId,"FAVORITES",smallSize));
                        holder.c_p_i_share.setOnClickListener(v -> addTo(productId,"SHARE",smallSize));
                    }
                    @NonNull
                    @Override
                    public HomeFragmentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new HomeFragmentAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_item, parent, false));
                    }
                };
        f_h_homeContainerRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    private void addTo(String productId,String child,String totalPrice) {
        if(!phoneNumber.equals("NOTHING")) {
            if (child.equals("FAVORITES")) {
                FAVORITES_DB.child(phoneNumber).child(productId).setValue(new FavoriteModel(productId));
                Toast.makeText(getContext(), "Add To Favorites Successfully", Toast.LENGTH_SHORT).show();
            } else if(child.equals("SHARE")){
                SHARE_DB.child(phoneNumber).child(productId).setValue(new ShareModel(productId));
                Toast.makeText(getContext(), "Please Wait To Loading", Toast.LENGTH_SHORT).show();
            }else{
                CARTS_DB.child(phoneNumber).child(productId).setValue(new CartModel(productId,"1",totalPrice));
                Toast.makeText(getContext(), "Add To Cart Successfully", Toast.LENGTH_SHORT).show();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.please_register_frist);
            builder.setIcon(R.drawable.ic_photo_24);
            builder.setPositiveButton(R.string.registration, (dialog, which) -> callRegistration());
            builder.setNeutralButton(R.string.skip, (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    private void callRegistration() {
        startActivity(new Intent(getContext(), RegistrationActivity.class));
        ((Activity)getContext()).finish();
    }

    ///////////////////////////////////
    ///////HomeFragmentAdapter/////////
    ///////////////////////////////////
    private static class HomeFragmentAdapter extends RecyclerView.ViewHolder {

        public HomeFragmentAdapter(@NonNull View itemView) { super(itemView); }

        public void showMenu(String menuImage, String menuName) {
            ImageView c_c_i_menuImage = itemView.findViewById(R.id.c_c_i_menuImage);
            TextView c_c_i_menuName = itemView.findViewById(R.id.c_c_i_menuName);
            Picasso.get().load(menuImage).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_c_i_menuImage);
            c_c_i_menuName.setText(menuName);
        }

        public ImageView c_p_i_favorite ,c_p_i_share,c_p_i_addToCart;
        public void showProduct(String productImage, String productName) {
            c_p_i_addToCart= itemView.findViewById(R.id.c_p_i_addToCart);
            c_p_i_favorite = itemView.findViewById(R.id.c_p_i_favorite);
            c_p_i_share    = itemView.findViewById(R.id.c_p_i_share);
            ImageView c_p_i_productImage= itemView.findViewById(R.id.c_p_i_productImage);
            TextView c_p_i_productName  = itemView.findViewById(R.id.c_p_i_productName);
            Picasso.get().load(productImage).placeholder(R.drawable.ic_photo_24).error(R.drawable.ic_photo_24).into(c_p_i_productImage);
            c_p_i_productName.setText(productName);
        }
    }

}