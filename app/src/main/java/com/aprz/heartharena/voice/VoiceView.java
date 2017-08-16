package com.aprz.heartharena.voice;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.aprz.heartharena.R;
import com.aprz.heartharena.utils.ScreenUtil;
import com.aprz.heartharena.voice.listener.SimpleOnGestureListener;

/**
 * Created by aprz on 17-8-13.
 * email: lyldalek@gmail.com
 * desc:
 */

public class VoiceView extends View {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_RECORDING = 2;

    private Bitmap mNormalBitmap;
    private Bitmap mRecordingBitmap;
    private Paint mPaint;
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    private int mState = STATE_NORMAL;
    private float mMinRadius;
    private float mMaxRadius;
    private float mCurrentRadius;
    private GestureDetector mGestureDetector;

    private OnVoiceViewClickListener mListener;

    public VoiceView(Context context) {
        super(context);
        init();
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mNormalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_voice_off);
        mRecordingBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_voice_on);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ActivityCompat.getColor(getContext(), R.color.voice_edge));

        mMinRadius = ScreenUtil.dp2px(getContext(), 100) / 2;
        mCurrentRadius = mMinRadius;

        setEnabled(true);
        setClickable(true);
        setLongClickable(true);
        mGestureDetector = new GestureDetector(getContext(), new OnGestureListenerImpl());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxRadius = Math.min(w, h) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mRecordingBitmap.getWidth() * 2, + mRecordingBitmap.getHeight() * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if(mCurrentRadius > mMinRadius){
            canvas.drawCircle(width / 2, height / 2, mCurrentRadius, mPaint);
        }

        switch (mState){
            case STATE_NORMAL:
                canvas.drawBitmap(mNormalBitmap, width / 2 - mMinRadius,  height / 2 - mMinRadius, mPaint);
                break;
            case STATE_RECORDING:
                canvas.drawBitmap(mRecordingBitmap, width / 2 - mMinRadius,  height / 2 - mMinRadius, mPaint);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void animateRadius(float radius){
        // --------------------
        radius += mMinRadius;
        Log.e("RecognizerDialogUtil", radius + "--- " + mCurrentRadius);
        // --------------------

        if (mState != STATE_RECORDING) {
            return;
        }
        if(radius <= mCurrentRadius){
            return;
        }
        if(radius > mMaxRadius){
            radius = mMaxRadius;
        }else if(radius < mMinRadius){
            radius = mMinRadius;
        }
        if(radius == mCurrentRadius){
            return;
        }
        if(mAnimatorSet.isRunning()){
            mAnimatorSet.cancel();
        }
        mAnimatorSet.playSequentially(
                ObjectAnimator.ofFloat(this, "CurrentRadius", getCurrentRadius(), radius).setDuration(50),
                ObjectAnimator.ofFloat(this, "CurrentRadius", radius, mMinRadius).setDuration(200)
        );
        mAnimatorSet.start();
    }

    public float getCurrentRadius() {
        return mCurrentRadius;
    }

    public void setCurrentRadius(float currentRadius) {
        mCurrentRadius = currentRadius;
        invalidate();
    }

    public void startRecording () {
        mState = STATE_RECORDING;
        if (mListener != null) {
            mListener.start();
        }
    }

    public void stopRecording() {
        mState = STATE_NORMAL;
        if (mListener != null) {
            mListener.stop();
        }
    }

    public void setStateNormal() {
        mState = STATE_NORMAL;
//        if (mAnimatorSet.isRunning()) {
            postInvalidate();
//        }
    }

    public void setStateRecording() {
        mState = STATE_RECORDING;
//        if (mAnimatorSet.isRunning()) {
            postInvalidate();
//        }
    }

//    public void forceInvokeStartCallback () {
//
//    }


    public void setOnVoiceViewClickListener(OnVoiceViewClickListener listener) {
        mListener = listener;
    }

    private class OnGestureListenerImpl extends SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            float x = e.getX();
            float y = e.getY();

            if (Math.abs(x - getWidth()/ 2) > getWidth() / 4) {
                return super.onSingleTapUp(e);
            }

            if (Math.abs(y - getHeight() / 2) > getHeight() / 4) {
                return super.onSingleTapUp(e);
            }

            if (mState == STATE_NORMAL) {
                startRecording();
            } else {
                stopRecording();
            }
            invalidate();

            return true;
        }
    }

    public interface OnVoiceViewClickListener {

        void start();

        void stop();

    }

}
