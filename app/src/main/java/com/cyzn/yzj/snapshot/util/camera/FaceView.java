package com.cyzn.yzj.snapshot.util.camera;

import android.content.Context;
import android.graphics.*;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.*;

/**
 * @author YZJ
 * @description 人脸检测框
 * @date 2018/11/12 0012
 */
public class FaceView extends AppCompatImageView {
    private Context mContext;
    private Camera.Face[] mFaces;
    private Matrix mMatrix = new Matrix();
    private boolean mirror;
    private Paint mLinePaint;


    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        this.mContext = context;
    }

    public void setFaces(Camera.Face[] faces) {
        this.mFaces = faces;
        prepareMatrix();
        invalidate();
    }

    public void clearFaces() {
        mFaces = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFaces == null || mFaces.length < 1) {
            return;
        }
        if (mFaces != null && mFaces.length >= 1) {
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.rotate(-0);
            mirror = true;
            Log.e("mFaces", "mFaces" + mFaces.length);
            for (int i = 0; i < mFaces.length; i++) {
                Camera.Face face = mFaces[i];
                int width = face.rect.right - face.rect.left;
                int needWidth = getWidth() * width / 1700; //1700：此值可改变人脸框范围大小，数值越小范围越大
                if (mirror) {
                    int cx = -face.rect.centerY(); //因为之前对camera做了旋转，所以这里需要转换一下   //后置摄像头
                    int cy = face.rect.centerX();
                    RectF rectF = new RectF(getWidth() * cx / 2000f - needWidth / 2, getHeight() * cy / 2000f - needWidth / 2, getWidth() * cx / 2000f + needWidth / 2, getHeight() * cy / 2000f + needWidth / 2);
                    canvas.drawRect(rectF, mLinePaint);
                } else {
                    int cx = -face.rect.centerY(); //因为之前对camera做了旋转，所以这里需要转换一下   //前置摄像头
                    int cy = -face.rect.centerX();
                    RectF rectF = new RectF(getWidth() * cx / 2000f - needWidth / 2, getHeight() * cy / 2000f - needWidth / 2, getWidth() * cx / 2000f + needWidth / 2, getHeight() * cy / 2000f + needWidth / 2);
                    canvas.drawRect(rectF, mLinePaint);
                }
            }
        }
        super.onDraw(canvas);
    }


    /**
     * <p>Here is the matrix to convert driver coordinates to View coordinates
     * in pixels.</p>
     * <pre>
     * Matrix matrix = new Matrix();
     * CameraInfo info = CameraHolder.instance().getCameraInfo()[cameraId];
     * // Need mirror for front camera.
     * boolean mirror = (info.facing == CameraInfo.CAMERA_FACING_FRONT);
     * matrix.setScale(mirror ? -1 : 1, 1);
     * // This is the value for android.hardware.Camera.setDisplayOrientation.
     * matrix.postRotate(displayOrientation);
     * // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
     * // UI coordinates range from (0, 0) to (width, height).
     * matrix.postScale(view.getWidth() / 2000f, view.getHeight() / 2000f);
     * matrix.postTranslate(view.getWidth() / 2f, view.getHeight() / 2f);
     * </pre>
     */
    private void prepareMatrix() {
        mMatrix.setScale(mirror ? -1 : 1, 1);
        mMatrix.postRotate(9);
        mMatrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
        mMatrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//      int color = Color.rgb(0, 150, 255);
        int color = Color.rgb(98, 212, 68);
//      mLinePaint.setColor(Color.RED);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(180);
    }
}
