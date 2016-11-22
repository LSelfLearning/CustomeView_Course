package com.lewish.start.roundprogressbartest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Lewish on 2016/11/22.
 */

public class ClockVIew extends View {
    public static final int STROKE_WIDTH = 1;
    public static final String REACH_COLOR = "";
    public static final String UNREACH_COLOR = "";
    private int mWidth;
    private int mHeight;
    private int mDiameter;
    private int progress = 13;
    private ValueAnimator valueAnimator;

    public ClockVIew(Context context) {
        this(context,null);
    }

    public ClockVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setAntiAlias(true);
        for(int i=0; i<36; i++){
            if(i<=progress) {
                paint.setColor(Color.RED);
            }else {
                paint.setColor(Color.GRAY);
            }
            paint.setStrokeWidth(dp2px(4));
            canvas.drawLine(mWidth/2, mHeight/2 - mDiameter/2, mWidth/2, mHeight/2 - mDiameter/2 + dp2px(8), paint);
            // 旋转画布，每次旋转10度
            canvas.rotate(10,mWidth/2, mHeight/2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        // 钟表的外圆直径（除去 padding ）
        mDiameter = Math.min(mWidth - getPaddingLeft() - getPaddingRight(), mHeight - getPaddingTop() - getPaddingBottom());
    }

    public void dynamicDraw(int progress){
        valueAnimator = ValueAnimator.ofInt(0,progress);
        valueAnimator.setDuration(3000);
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
        this.progress = progress;
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
