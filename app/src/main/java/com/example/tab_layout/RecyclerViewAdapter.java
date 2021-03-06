package com.example.tab_layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    // creating a variable for our context and array list.
    private final Context context;
    private final ArrayList<String> imagePathArrayList;


    // on below line we have created a constructor.
    public RecyclerViewAdapter(Context context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }

    interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    private OnItemLongClickListener mListenerL = null;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mListenerL = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout in this method which we have created.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // on below line we are getting th file from the
        // path which we have stored in our list.
        File imgFile = new File(imagePathArrayList.get(position));
        // on below line we are checking if tje file exists or not.
        if (imgFile.exists()) {
            Uri imageUri = Uri.fromFile(imgFile);
            Glide.with(context).load(imageUri).into(holder.imageIV);
            // if the file exists then we are displaying that file in our image view using picasso library.
            //Picasso.get().load("file://" +imgFile).fit()
            //        .centerInside().placeholder(R.drawable.ic_launcher_background).into(holder.imageIV);

            // on below line we are adding click listener to our item of recycler view.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // inside on click listener we are creating a new intent
                    Intent i = new Intent(context, ImageDetailActivity.class);

                    // on below line we are passing the image path to our new activity.
                    i.putExtra("position", position);
                    i.putExtra("PathArrayList",imagePathArrayList);

                    // at last we are starting our activity.
                    context.startActivity(i);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(position!=RecyclerView.NO_POSITION){
                        if (mListenerL!=null){
                            mListenerL.onItemLongClick(view,position);
                        }
                    }
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        // this method returns
        // the size of recyclerview
        return imagePathArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private final ImageView imageIV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            imageIV = itemView.findViewById(R.id.idIVImage);
        }
    }
}