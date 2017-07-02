package com.hk47.boofplay.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraDisplayView extends SurfaceView implements SurfaceHolder.Callback,
        Camera.PreviewCallback {

    public static final String TAG = CameraDisplayView.class.getSimpleName();

    float mVerticalFOV;
    float mHorizontalFOV;

    SurfaceHolder mHolder;
    Activity mActivity;
    Camera mCamera;

    public Bitmap mBitmap;

    public CameraDisplayView(Context context) {
        super(context);
    }

    public CameraDisplayView(Context context, Activity activity) {
        super(context);

        mActivity = activity;
        mHolder = getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = Camera.open();

        mCamera.setPreviewCallback(this);

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        mCamera.setDisplayOrientation((info.orientation - degrees + 360) % 360);

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            Log.e(TAG, "surfaceCreated exception: ", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();

        // Corrects the preview aspect ratio to reflect the picture that will be taken.
        // Shouldn't be necessary for the augmented reality overlay.
//        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
//        Collections.reverse(previewSizes);
//        for (Camera.Size size : previewSizes) {
//            if ((size.height >= height) && (size.width >= width))
//            {
//                params.setPreviewSize(size.width, size.height);
//            }
//        }

        if (params.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        // Get the camera's field of vision
        mVerticalFOV = params.getVerticalViewAngle();
        mHorizontalFOV = params.getHorizontalViewAngle();

        mCamera.setParameters(params);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public float getVerticalFOV() {
        return mVerticalFOV;
    }

    public float getHorizontalFOV() {
        return mHorizontalFOV;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
        Allocation bmData = renderScriptNV21ToRGBA888(
                mActivity,
                800,
                800,
                data);
        bmData.copyTo(mBitmap);
    }

    public Allocation renderScriptNV21ToRGBA888(Context context, int width, int height, byte[] nv21) {
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));

        Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
        Allocation in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

        Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
        Allocation out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);

        in.copyFrom(nv21);

        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        return out;
    }
}
