package com.lewish.start.roundprogressbartest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Lewish on 2016/11/22.
 */

public class ClockView extends View {
    private Paint mPaint;
    private int mReachColor;
    private int mUnReachColor;
    private int mStrokeWidth;
    private int mStrokeLength;
    private int mWidth;
    private int mHeight;
    private int mDiameter;
    private int mTotalDotNum;
    private int mProgress;
    private ValueAnimator valueAnimator;

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ClockView);
        mReachColor = typedArray.getColor(R.styleable.ClockView_cprogressReachColor, Color.parseColor("#d20000"));
        mUnReachColor = typedArray.getColor(R.styleable.ClockView_cprogressUnReachColor, Color.parseColor("#b9babc"));
        mStrokeWidth = (int) typedArray.getDimension(R.styleable.ClockView_cstrokeWidth, dp2px(4));
        mStrokeLength = (int) typedArray.getDimension(R.styleable.ClockView_cstrokeLength,dp2px(8));
        mTotalDotNum = typedArray.getInteger(R.styleable.ClockView_ctotalDotNum,36);
        mProgress = typedArray.getInteger(R.styleable.ClockView_cprogress,24);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircleDot(canvas);
    }

    private void drawCircleDot(Canvas canvas) {
        for(int i = 0; i< mTotalDotNum; i++){
            if(i<= mProgress) {
                mPaint.setColor(mReachColor);
            }else {
                mPaint.setColor(mUnReachColor);
            }
            canvas.drawLine(mWidth/2, mHeight/2 - mDiameter/2, mWidth/2, mHeight/2 - mDiameter/2 + mStrokeLength, mPaint);
            canvas.rotate(10,mWidth/2, mHeight/2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        mDiameter = Math.min(mWidth - getPaddingLeft() - getPaddingRight(), mHeight - getPaddingTop() - getPaddingBottom());
    }

    public void dynamicDraw(int progress,int animTime){
        valueAnimator = ValueAnimator.ofInt(0,progress);
        valueAnimator.setDuration(animTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                setProgress(animatedValue);
            }
        });
        valueAnimator.start();
    }
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public void setProgress(int progress) {
        mProgress = (int) ((progress/100.0)* mTotalDotNum);
        postInvalidate();
    }
    @Override
    protected void onDetachedFromWindow() {
        if(valueAnimator!=null) {
            valueAnimator.cancel();
            valueAnimator.end();
            valueAnimator=null;
        }
        super.onDetachedFromWindow();
    }
}
