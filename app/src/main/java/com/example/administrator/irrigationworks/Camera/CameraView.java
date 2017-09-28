package com.example.administrator.irrigationworks.Camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Context context;
    private Camera camera;
    private FaceView faceView;

    private OnFaceDetectedListener onFaceDetectedListener;

    /**
     * 摄像头最大的预览尺寸
     */
    private Camera.Size maxPreviewSize;

    /**
     * 预览时Frame的计数器
     */
    private int frameCount;

    /**
     * 是否正在检测人脸
     */
    private boolean isDetectingFace = false;

    /**
     * 是否已检测到人脸
     */
    private boolean detectedFace = false;

    public CameraView(Context context) {
        super(context);
        init(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        getHolder().addCallback(this);
    }

    public void setFaceView(FaceView faceView) {
        if (faceView != null) {
            this.faceView = faceView;
        }
    }

    /** 摄像头重新开始预览 */
    public void reset() {
        if(faceView != null) {
            faceView.setVisibility(View.GONE);
        }
        if(camera != null) {
            camera.setPreviewCallback(this);
            camera.startPreview();
        }
        frameCount = 0;
        detectedFace = false;
        isDetectingFace = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            if(camera != null) {
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(holder);
                camera.setPreviewCallback(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            maxPreviewSize = getMaxPreviewSize(camera);
            if (maxPreviewSize != null) {
                ViewGroup.LayoutParams params = getLayoutParams();
                Point point = getScreenSize();
                params.width = point.x;
                params.height = maxPreviewSize.width * point.x / maxPreviewSize.height;
                setLayoutParams(params);
                if(faceView != null) {
                    faceView.setLayoutParams(params);
                }
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(maxPreviewSize.width, maxPreviewSize.height);
                camera.setParameters(parameters);
            }
            camera.startPreview();
            frameCount = 0;
            detectedFace = false;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            try {
                camera.stopPreview();
                camera.setPreviewDisplay(null);
                camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取手机屏幕的尺寸
     *
     * @return
     */
    private Point getScreenSize() {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return new Point(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    /**
     * 获取摄像头最大的预览尺寸
     *
     * @param camera
     * @return
     */
    private Camera.Size getMaxPreviewSize(Camera camera) {
        List<Camera.Size> list = camera.getParameters().getSupportedPreviewSizes();
        if (list != null) {
            int max = 0;
            Camera.Size maxSize = null;
            for (Camera.Size size : list) {
                int n = size.width * size.height;
                if (n > max) {
                    max = n;
                    maxSize = size;
                }
            }
            return maxSize;
        }
        return null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.e("yubo", "onPreviewFrame...");
        frameCount++;
        //前面15帧丢弃
        if (frameCount > 15 && !isDetectingFace && !detectedFace) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            final byte[] byteArray = ImageUtils.yuv2Jpeg(data, size.width, size.height);
            isDetectingFace = true;
            new Thread() {
                @Override
                public void run() {
                    detectFaces(byteArray);
                }
            }.start();
        }
    }

    /**
     * 检测data数据中是否有人脸，这里需要先旋转一下图片，该方法执行在子线程中
     *
     * @param data
     */
    private void detectFaces(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        bitmap = ImageUtils.rotateBitmap(bitmap, -90);
        bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        FaceDetectorUtils.detectFace(bitmap, new FaceDetectorUtils.Callback() {
            @Override
            public void onFaceDetected(final FaceDetector.Face[] faces, final Bitmap bm) {
                isDetectingFace = false;
                Log.e("yubo", "face detected...");
                if (!detectedFace) {
                    detectedFace = true;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (camera != null) {
                                camera.stopPreview();
                            }
                            if (faceView != null) {
                                float scaleRate = bm.getWidth() * 1.0f / getScreenSize().x;
                                faceView.setScaleRate(scaleRate);
                                faceView.setFaces(faces, bm);
                                faceView.setVisibility(View.VISIBLE);
                            }
                            if (onFaceDetectedListener != null) {
                                onFaceDetectedListener.onFaceDetected(bm);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFaceNotDetected(Bitmap bm) {
                bm.recycle();
                if (faceView != null) {
                    faceView.clear();
                }
                isDetectingFace = false;
            }
        });
    }

    /**
     * 检测到人脸的监听器
     */
    public interface OnFaceDetectedListener {
        void onFaceDetected(Bitmap bm);
    }

    /**
     * 设置监听器，监听检测到人脸的动作
     */
    public void setOnFaceDetectedListener(OnFaceDetectedListener listener) {
        if (listener != null) {
            this.onFaceDetectedListener = listener;
        }
    }

}
