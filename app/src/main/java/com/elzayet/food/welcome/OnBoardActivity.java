package com.elzayet.food.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.elzayet.food.MainActivity;
import com.elzayet.food.R;


public class OnBoardActivity extends AppCompatActivity {
    private ViewPager a_o_b_slider;
    private LinearLayout a_o_b_dots;

    private OnBoardAdapter onBoardAdapter;
    private TextView[]dots;

    private Button a_o_b_getStarted ,a_o_b_next;
    private int currentSlide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        a_o_b_slider     = findViewById(R.id.a_o_b_slider);
        a_o_b_dots       = findViewById(R.id.a_o_b_dots);
        a_o_b_getStarted = findViewById(R.id.a_o_b_getStarted);
        a_o_b_next       = findViewById(R.id.a_o_b_next);

        onBoardAdapter = new OnBoardAdapter(this);
        a_o_b_slider.setAdapter(onBoardAdapter);
        addDots(0);
        a_o_b_slider.addOnPageChangeListener(changeListener);

        a_o_b_getStarted.setOnClickListener(v -> {
            startActivity(new Intent(OnBoardActivity.this , MainActivity.class));
            finish();
        });

        a_o_b_next.setOnClickListener(v -> { a_o_b_slider.setCurrentItem(currentSlide + 1); });
    }

    private void addDots(int position){
        dots = new TextView[4];
        currentSlide = position ;
        a_o_b_dots.removeAllViews();
        for (int i = 0 ; i < dots.length ; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            a_o_b_dots.addView(dots[i]);
        }

        if(dots.length > 0){ dots[position].setTextColor(getResources().getColor(R.color.purple_700)); }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
        @Override
        public void onPageSelected(int position) {
            addDots(position);
            if (position == 3) {
                a_o_b_getStarted.setVisibility(View.VISIBLE);
                a_o_b_next.setVisibility(View.INVISIBLE);
            } else {
                a_o_b_getStarted.setVisibility(View.INVISIBLE);
                a_o_b_next.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) { }
    };



    private class OnBoardAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater ;

        private int[] sliderImages = {R.drawable.ic_favorites_white,R.drawable.ic_favorites_white,R.drawable.ic_favorites_white,R.drawable.ic_favorites_white};
        private int[] sliderTitles = {R.string.first_slide_title,R.string.second_slide_title,R.string.fourth_slide_title,R.string.fourth_slide_title};
        private int[] sliderDescription = {R.string.first_slide_descrition,R.string.second_slide_descrition,R.string.fourth_slide_descrition,R.string.fourth_slide_descrition};

        public OnBoardAdapter(Context context){ this.context = context ; }

        @Override
        public int getCount() { return sliderImages.length; }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) { return view == (ConstraintLayout) object; }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.card_on_bording_item,container,false);

            ImageView c_s_m_sliderImage      = view.findViewById(R.id.c_s_m_sliderImage);
            TextView c_s_m_sliderTitle       = view.findViewById(R.id.c_s_m_sliderTitle);
            TextView c_s_m_sliderDescription = view.findViewById(R.id.c_s_m_sliderDescription);

            c_s_m_sliderImage.setImageResource(sliderImages[position]);
            c_s_m_sliderTitle.setText(sliderTitles[position]);
            c_s_m_sliderDescription.setText(sliderDescription[position]);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { container.removeView((ConstraintLayout) object); }
    }

    //////////////////////////////
    ////////hide statebar/////////
    //////////////////////////////
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { hideSystemUI(); }
        else { showSystemUI(); }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}