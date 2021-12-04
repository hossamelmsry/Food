package com.elzayet.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elzayet.food.bottombar.AccountFragment;
import com.elzayet.food.sidebar.FQAActivity;
import com.elzayet.food.sidebar.PointsActivity;
import com.elzayet.food.sidebar.RefellarActivity;
import com.elzayet.food.sidebar.registration.RegistrationActivity;
import com.elzayet.food.sidebar.SettingsActivity;
import com.elzayet.food.sidebar.WalletActivity;
import com.elzayet.food.bottombar.CartFragment;
import com.elzayet.food.bottombar.FavoritesFragment;
import com.elzayet.food.bottombar.OrdersFragment;
import com.elzayet.food.bottombar.home.HomeFragment;
import com.elzayet.food.topbar.notification.NotificationActivity;
import com.elzayet.food.topbar.search.SearchActivity;
import com.elzayet.food.tools.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Drawer Menu
    private DrawerLayout a_h_drawer_layout;
    private NavigationView a_h_navigation_view;
    private View navigationHeader ;
    private TextView m_h_accountPalance;
    //body
    private ConstraintLayout a_m_pageContainer;
    private BottomNavigationView bottom_navigation ;
    //User Account
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // drawer menu
        a_h_drawer_layout   = findViewById(R.id.a_h_drawer_layout);
        a_h_navigation_view = findViewById(R.id.a_h_navigation_view);
        navigationHeader    = a_h_navigation_view.getHeaderView(0);
        m_h_accountPalance  = navigationHeader.findViewById(R.id.m_h_accountPalance);
        //body
        a_m_pageContainer = findViewById(R.id.a_m_pageContainer);
        ImageView a_m_notifications = findViewById(R.id.a_m_notifications);
        ImageView a_m_menu          = findViewById(R.id.a_m_menu);
        ImageView a_m_search        = findViewById(R.id.a_m_search);
        TextView a_c_appName       = findViewById(R.id.a_c_appName);

        a_m_notifications.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), NotificationActivity.class)));
        a_m_menu.setOnClickListener(v -> {
            if (a_h_drawer_layout.isDrawerVisible(GravityCompat.START)) { a_h_drawer_layout.closeDrawer(GravityCompat.START); }
            else { a_h_drawer_layout.openDrawer(GravityCompat.START); }
        });
        a_m_search.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), SearchActivity.class)));
        // bottomBar
        bottom_navigation = findViewById(R.id.bottom_navigation);

        bottom_navigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bott_nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.a_m_fragment, new HomeFragment()).commit();
                    return true;

                case R.id.bott_nav_cart:
                    getSupportFragmentManager().beginTransaction().replace(R.id.a_m_fragment, new CartFragment()).commit();
                    return true;

                case R.id.bott_nav_orders:
                    getSupportFragmentManager().beginTransaction().replace(R.id.a_m_fragment, new OrdersFragment()).commit();
                    return true;

                case R.id.bott_nav_favorites:
                    getSupportFragmentManager().beginTransaction().replace(R.id.a_m_fragment, new FavoritesFragment()).commit();
                    return true;
                case R.id.bott_nav_account:
                    getSupportFragmentManager().beginTransaction().replace(R.id.a_m_fragment, new AccountFragment()).commit();
                    return true;
            }
            return false;
        });
        //user Account
        SharedPreferences pref = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        phoneNumber            = pref.getString("phoneNumber", "NOTHING");
        if(!phoneNumber.equals("NOTHING")) {
            String userName = pref.getString("userName","NOTHING");
            a_c_appName.setText("HI ," + userName);
        }
        navigationDrawer();
        setupNavigationBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.a_m_fragment, new HomeFragment()).commit();
    }

    //check for registration
    private void setupNavigationBar() {
        Menu menu = a_h_navigation_view.getMenu();
        if (phoneNumber.equals("NOTHING")) {
            menu.findItem(R.id.slide_nav_wallet).setVisible(false);
            menu.findItem(R.id.slide_nav_refellar).setVisible(false);
            menu.findItem(R.id.slide_nav_logout).setVisible(false);
            menu.findItem(R.id.slide_nav_points).setVisible(false);
            m_h_accountPalance.setText(R.string.please_login_first);
        } else {
            FirebaseDatabase.getInstance().getReference("WALLETS").child(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PointsModel pointsModel = snapshot.getValue(PointsModel.class);
                    String points           = pointsModel.getPoints();
                    m_h_accountPalance.setText(points);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show(); }
            });
            menu.findItem(R.id.slide_nav_registration).setVisible(false);
        }
    }

    //Drawer Menu
    private void navigationDrawer() {
        a_h_navigation_view.bringToFront();
        a_h_navigation_view.setNavigationItemSelectedListener(this);
        animationNavigationDrawer();
    }

    //Animation Drawer Menu
    private static final float END_SCALE = 0.7f;
    private void animationNavigationDrawer() {
        a_h_drawer_layout.setScrimColor(getResources().getColor(R.color.purple_200));
        a_h_drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScal       = 1 - diffScaledOffset;
                a_m_pageContainer.setScaleX(offsetScal);
                a_m_pageContainer.setScaleY(offsetScal);

                final float xOffset     = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = a_m_pageContainer.getWidth() * diffScaledOffset / 2;
                final float xTranslate  = xOffset - xOffsetDiff;
                a_m_pageContainer.setTranslationX(xTranslate);
            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) { }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) { }
            @Override
            public void onDrawerStateChanged(int newState) { }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.slide_nav_home        :
                a_h_drawer_layout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(R.id.a_m_fragment, new HomeFragment()).commit();
                break;
            //nav_profile
            case R.id.slide_nav_registration: startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                break;
            case R.id.slide_nav_wallet      : startActivity(new Intent(MainActivity.this, WalletActivity.class));
                break;
            case R.id.slide_nav_refellar    : startActivity(new Intent(MainActivity.this, RefellarActivity.class));
                break;
            case R.id.slide_nav_points      : startActivity(new Intent(MainActivity.this, PointsActivity.class));
                break;
            case R.id.slide_nav_logout      : Session.loguot(MainActivity.this);
                break;
            //app_name
            case R.id.slide_nav_facebook    : facebookPage("115011693524535");
                break;
            case R.id.slide_nav_twitter     : twitterPage("nourela24242114");
                break;
            case R.id.slide_nav_instagram   : instagramPage("nour.elain.940641");
                break;
            case R.id.slide_nav_tiktok      : tiktokPage("@hossamelzayet2021");
                break;
            case R.id.slide_nav_youtube     : youtubePage("UCLfXSwHamIQfjCfUCkNkklg");
                break;
            //app_name
            case R.id.slide_nav_rate        : rateApp();
                break;
            case R.id.slide_nav_settings    : startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.slide_nav_f_q_a         : startActivity(new Intent(MainActivity.this, FQAActivity.class));
                break;
            case R.id.slide_nav_verstion     : Toast.makeText(getBaseContext(), "1.0.0", Toast.LENGTH_SHORT).show();
                break;
            default: throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }

    private void rateApp() {
        AppRate.with(this)
                .setInstallDays(1) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
        AppRate.with(this).showRateDialog(this);
    }

    private void youtubePage(String mTwitterHandle) {
        Uri uri = Uri.parse("http://youtube.com/channel/" + mTwitterHandle);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.youtube.android");
        //https://www.youtube.com/channel/UCLfXSwHamIQfjCfUCkNkklg

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtube.com/channel/" + mTwitterHandle))); }
    }

    private void tiktokPage(String mTwitterHandle) {
        Uri uri = Uri.parse("http://tiktok.com/" + mTwitterHandle);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.tiktok.android");
        //https://www.tiktok.com/@hossamelzayet2021

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://tiktok.com/" + mTwitterHandle))); }
    }

    private void instagramPage(String mTwitterHandle) {
        Uri uri = Uri.parse("http://instagram.com/" + mTwitterHandle);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + mTwitterHandle))); }
    }

    private void twitterPage(String mTwitterHandle) {
        try{
//            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Best App for learning Android: " + "https://play.google.com/store/apps/details?id=arjuntoshniwal.androidtutorials.advanced&hl=en");
//            sharingIntent.setPackage("advanced.twitter.android");
//            startActivity(sharingIntent);

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + mTwitterHandle)));
        }catch (ActivityNotFoundException e){ startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + mTwitterHandle))); }
    }

    private void facebookPage(String pageId) {
        try {  startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("fb://page/" + pageId))); }
        catch (ActivityNotFoundException e){ startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/"+ pageId))); }
    }

//TODO:Points xml


}