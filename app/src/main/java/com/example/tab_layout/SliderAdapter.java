package com.example.tab_layout;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.Slider;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private List<String> sliderItems;
    private ViewPager2 viewPager2;
    private Context mContext;

    public SliderAdapter(Context context, List<String> sliderItems, ViewPager2 viewPager2) {
        mContext = context;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slider_item_container,
                        parent,
                        false
                )

        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.bind(sliderItems.get(position));
        if(position == sliderItems.size() - 2){
            viewPager2.post(runnable);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // inside on click listener we are creating a new intent
                Intent i = new Intent(mContext, ImageFinalActivity.class);
                String ImgPath = sliderItems.get(position);
                // on below line we are passing the image path to our new activity.
                i.putExtra("imgpath", ImgPath);

                // at last we are starting our activity.
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.idIVImage);
        }

        void bind(String sliderItem) {
                Glide.with(mContext).load(sliderItem).into(imageView);
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run(){
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

}
