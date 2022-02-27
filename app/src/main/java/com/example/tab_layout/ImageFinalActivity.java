package com.example.tab_layout;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageFinalActivity extends AppCompatActivity {

    private ScaleGestureDetector scaleGestureDetector;
    String ImgPath;
    private ImageView imageView;

    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_final);

        // on below line getting data which we have passed from our adapter class.
        ImgPath = getIntent().getStringExtra("imgpath");

        // initializing our image view.
        imageView = findViewById(R.id.idFImage);

        // on below line we are initializing our scale gesture detector for zoom in and out for our image.
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // on below line we are getting our image file from its path.
        File imgFile = new File(ImgPath);

        if (imgFile.exists()) {
            Uri imageUri = Uri.fromFile(imgFile);
            Glide.with(this).load(imageUri).into(imageView);
            //Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(imageView);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // inside on touch event method we are calling on
        // touch event method and pasing our motion event to it.
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        // on below line we are creating a class for our scale
        // listener and extending it with gesture listener.
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

            // inside on scale method we are setting scale
            // for our image in our image view.
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            // on below line we are setting
            // scale x and scale y to our image view.
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
