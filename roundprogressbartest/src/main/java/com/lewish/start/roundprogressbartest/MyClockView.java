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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/11/23 11:26.
 */

public class MyClockView extends RelativeLayout {
    private TextView mTvTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
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
    private int mAnimTime;
    private ValueAnimator valueAnimator;

    public MyClockView(Context context) {
        this(context,null);
    }

    public MyClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromXml(context, attrs);
        initPaint();
        initView(context);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
    }

    private void initFromXml(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ClockView);
        mReachColor = typedArray.getColor(R.styleable.MyClockView_progressReachColor, Color.parseColor("#d20000"));
        mUnReachColor = typedArray.getColor(R.styleable.MyClockView_progressUnReachColor, Color.parseColor("#b9babc"));
        mStrokeWidth = (int) typedArray.getDimension(R.styleable.MyClockView_strokeWidth, dp2px(4));
        mStrokeLength = (int) typedArray.getDimension(R.styleable.MyClockView_strokeLength,dp2px(8));
        mTotalDotNum = typedArray.getInteger(R.styleable.MyClockView_totalDotNum,36);
        mProgress = typedArray.getInteger(R.styleable.MyClockView_mprogress,24);
        mAnimTime=typedArray.getInteger(R.styleable.MyClockView_animTime,3000);
        typedArray.recycle();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawCircleDot(canvas);
        super.dispatchDraw(canvas);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mDiameter = Math.min(mWidth - getPaddingLeft() - getPaddingRight(), mHeight - getPaddingTop() - getPaddingBottom());
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public void setProgress(int progress) {
        mProgress = (int) ((progress/100.0)* mTotalDotNum);
        valueAnimator = ValueAnimator.ofInt(0,mProgress);
        valueAnimator.setDuration(mAnimTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
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

    private void initView(Context context) {
        View.inflate(context, R.layout.myclockview,this);
        mTvTime = (TextView) findViewById(R.id.tv_time);
    }

    public void updateTime(long millisUntilFinished){
        mTvTime.setText(sdf.format(millisUntilFinished-28800000));//中国在东8区，减掉8小时
    }
}
