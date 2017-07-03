package com.hk47.boofplay;

import android.graphics.Bitmap;

import boofcv.abst.filter.derivative.ImageGradient;
import boofcv.android.VisualizeImageData;
import boofcv.android.gui.VideoImageProcessing;
import boofcv.factory.filter.derivative.FactoryDerivative;
import boofcv.struct.image.GrayS16;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageType;

public class ShowGradient extends VideoImageProcessing<GrayU8>
{
    // Storage for the gradient
    private GrayS16 derivX = new GrayS16(1,1);
    private GrayS16 derivY = new GrayS16(1,1);

    // computes the image gradient
    private ImageGradient<GrayU8,GrayS16> gradient = FactoryDerivative.three(GrayU8.class, GrayS16.class);

    protected ShowGradient()
    {
        super(ImageType.single(GrayU8.class));
    }

    @Override
    protected void declareImages( int width , int height ) {
        // You must call the super or else it will crash horribly
        super.declareImages(width,height);

        derivX.reshape(width,height);
        derivY.reshape(width,height);
    }

    @Override
    protected void process(GrayU8 gray, Bitmap output, byte[] storage)
    {
        gradient.process(gray,derivX,derivY);
        VisualizeImageData.colorizeGradient(derivX, derivY, -1, output, storage);
    }
}