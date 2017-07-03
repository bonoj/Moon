package com.hk47.boofplay;

import android.content.DialogInterface;
import android.hardware.Camera;
import android.support.v7.app.AlertDialog;

import java.util.List;

import boofcv.android.gui.VideoDisplayActivity;

public class VideoActivity extends VideoDisplayActivity
{
    @Override
    protected void onResume() {
        super.onResume();
        setProcessing( new ShowGradient());

        // for fun you can display the FPS by uncommenting the line below.
        // The FPS will vary depending on processing time and shutter speed,
        // which is dependent on lighting conditions
//		setShowFPS(true);
    }

    @Override
    protected Camera openConfigureCamera( Camera.CameraInfo cameraInfo )
    {
        Camera mCamera = selectAndOpenCamera(cameraInfo);
        Camera.Parameters param = mCamera.getParameters();

        // Select the preview size closest to 320x240
        // Smaller images are recommended because some computer vision operations are very expensive
        List<Camera.Size> sizes = param.getSupportedPreviewSizes();
        Camera.Size s = sizes.get(closest(sizes,320,240));
        param.setPreviewSize(s.width,s.height);
        mCamera.setParameters(param);

        return mCamera;
    }

    /**
     * Step through the camera list and select a camera.  It is also possible that there is no camera.
     * The camera hardware requirement in AndroidManifest.xml was turned off so that devices with just
     * a front facing camera can be found.  Newer SDK's handle this in a more sane way, but with older devices
     * you need this work around.
     */
    private Camera selectAndOpenCamera(Camera.CameraInfo info) {
        int numberOfCameras = Camera.getNumberOfCameras();

        int selected = -1;

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, info);

            if( info.facing == Camera.CameraInfo.CAMERA_FACING_BACK ) {
                selected = i;
                break;
            } else {
                // default to a front facing camera if a back facing one can't be found
                selected = i;
            }
        }

        if( selected == -1 ) {
            dialogNoCamera();
            return null; // won't ever be called
        } else {
            return Camera.open(selected);
        }
    }

    /**
     * Gracefully handle the situation where a camera could not be found
     */
    private void dialogNoCamera() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your device has no cameras!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Goes through the size list and selects the one which is the closest specified size
     */
    public static int closest(List<Camera.Size> sizes , int width , int height ) {
        int best = -1;
        int bestScore = Integer.MAX_VALUE;

        for( int i = 0; i < sizes.size(); i++ ) {
            Camera.Size s = sizes.get(i);

            int dx = s.width-width;
            int dy = s.height-height;

            int score = dx*dx + dy*dy;
            if( score < bestScore ) {
                best = i;
                bestScore = score;
            }
        }

        return best;
    }
}