package com.ats.mongi_production.activity;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.ats.mongi_production.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ImageZoomActivity extends AppCompatActivity {

    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    private ZoomageView zoomageView;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        zoomageView= findViewById(R.id.myZoomageView);

        try {

            image = getIntent().getExtras().getString("image");
            Log.e("IMAGE_ZOOM_ACT","   ------------- "+image);

            Picasso.get().load(image)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(zoomageView);
        } catch (Exception e) {
        }


    }

}
