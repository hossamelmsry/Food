package com.elzayet.food.bottombar.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elzayet.food.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    // creating a variable for
    // context and array list.
    private Context context;
    private List<SliderModel> mSliderItems = new ArrayList<>();

    // constructor for our adapter class.
    public SliderAdapter(Context context, List<SliderModel> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        // inside the on Create view holder method we are
        // inflating our layout file which we have created.
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        // inside on bind view holder method we are
        // getting url of image from our modal class
        // and setting that url for image inside our
        // image view using Picasso.
        final SliderModel sliderItem = mSliderItems.get(position);
        Picasso.get().load(sliderItem.getImgUrl()).fit().centerCrop().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {  return mSliderItems.size();  }

    // view holder class for initializing our view holder.
    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        // variables for our view and image view.
        View itemView;
        ImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            // initializing our views.
            imageView     = itemView.findViewById(R.id.idIVimage);
            this.itemView = itemView;
        }
    }
}
