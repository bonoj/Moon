package com.hk47.boofplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import boofcv.struct.image.GrayU8;

public class MainActivity extends AppCompatActivity {

    TextView mBoofTextView;
    ImageView mBoofImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoofTextView = (TextView) findViewById(R.id.boof_text_view);
        mBoofImageView = (ImageView) findViewById(R.id.boof_image_view);


        GrayU8 image = new GrayU8(100, 150);

        int unchangedPixel = image.get(5,23);

        String pixelTest = "unchangedPixel: " + unchangedPixel;

        changeSinglePixel(image);

        int changedPixel = image.get(5,23);

        pixelTest = pixelTest + "\nchangedPixel: " + changedPixel;

        mBoofTextView.setText(pixelTest);


        // TODO experiment with loading an image from resources
//        List<BufferedImage> images = UtilImageIO.loadImages("path/to/images","jpg");
//        GrayU8 gray = new GrayU8(1,1);
//        for( BufferedImage image : images ) {
//            ConvertBufferedImage.convertFrom(image,gray,true);
//        }
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
}
