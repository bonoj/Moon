package com.hk47.boofplay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hk47.boofplay.ui.CameraDisplayView;

import boofcv.struct.image.GrayU8;

public class MainActivity extends AppCompatActivity {

    public static final int RC_CAMERA_PERMISSION = 200;

    private FrameLayout mContainerView;
    private CameraDisplayView mCameraDisplayView;
    private TextView mBoofTextView;
    private ImageView mBoofImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoofTextView = (TextView) findViewById(R.id.boof_text_view);
        mBoofImageView = (ImageView) findViewById(R.id.boof_image_view);

        mContainerView = (FrameLayout) findViewById(R.id.boof_container_view);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
        }



        mCameraDisplayView = new CameraDisplayView(this, this);
        mContainerView.addView(mCameraDisplayView);

        GrayU8 image = new GrayU8(100, 150);

        int unchangedPixel = image.get(5,23);

        String pixelTest = "unchangedPixel: " + unchangedPixel;

        changeSinglePixel(image);

        int changedPixel = image.get(5,23);

        pixelTest = pixelTest + "\nchangedPixel: " + changedPixel;

        mBoofTextView.setText(pixelTest);

    }




    // Gets and sets pixel at (5,23) to 50 (some grayscale value)
    public static void changeSinglePixel(GrayU8 image) {
        int pixel = image.get(5, 23);
        image.set(5, 23, 50);
        image.set(6, 23, 50);
        image.set(7, 23, 50);
        image.set(8, 23, 50);
        image.set(5, 24, 50);
        image.set(6, 24, 50);
        image.set(7, 24, 50);
        image.set(8, 24, 50);
        image.set(5, 25, 50);
        image.set(6, 25, 50);
        image.set(7, 25, 50);
        image.set(8, 25, 50);
        image.set(5, 26, 50);
        image.set(6, 26, 50);
        image.set(7, 26, 50);
        image.set(8, 26, 50);
    }

    public void getBitmap(View view) {
        mBoofImageView.setImageBitmap(mCameraDisplayView.mBitmap);
    }
}
