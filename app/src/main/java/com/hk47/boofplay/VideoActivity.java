package com.hk47.boofplay;

import android.hardware.Camera;

import boofcv.android.gui.VideoDisplayActivity;

public class VideoActivity extends VideoDisplayActivity {

    @Override
    protected void onResume() {
        super.onResume();
        //setProcessing( new ShowGradient());
    }

    @Override
    protected Camera openConfigureCamera(Camera.CameraInfo cameraInfo) {
        return null;
    }
}
