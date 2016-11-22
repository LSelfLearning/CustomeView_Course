package com.lewish.start.customeindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/11/21 16:45.
 */

public class PagerIndicator extends LinearLayout {
    private Paint mPaint;
    private Path mPath;
    private int tabWidth;
    private int tabHeight;

    private int mTriangleWidth;
    private int mTriangleHeight;
    private int mTrangleInitTranslationX;
    private int mTrangleTranslationX;
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 8f;
    private static final String COLOR_LINE_REACH = "#d20000";
    private static final String COLOR_LINE_UNREACH = "#44000000";

    private int mLineWidth;
    private int mLineHeight;
    private int mLineTranslationX;


    public PagerIndicator(Context context) {
        this(context, null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(COLOR_LINE_REACH));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    /**
     * 倒三角
     */
    private void initTriangle() {
        mTriangleHeight = mTriangleWidth / 2;
        mLineHeight = mTriangleWidth / 6;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, mTriangleHeight);
        mPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setStrokeWidth(mLineHeight);
        //画灰线
        mPaint.setColor(Color.parseColor(COLOR_LINE_UNREACH));
        canvas.drawLine(0,tabHeight-mTriangleHeight,tabWidth*2,tabHeight-mTriangleHeight,mPaint);
        //画红线
        mPaint.setColor(Color.parseColor(COLOR_LINE_REACH));
        canvas.drawLine(mLineTranslationX,tabHeight-mTriangleHeight,mLineTranslationX+tabWidth,tabHeight-mTriangleHeight,mPaint);
        //画三角
        canvas.save();
        canvas.translate(mTrangleInitTranslationX + mTrangleTranslationX, tabHeight-mTriangleHeight);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / 2 * RADIO_TRIANGLE_WIDTH);
        mTrangleInitTranslationX = w / 2 / 2 - mTriangleWidth / 2;
        tabHeight = h;
        initTriangle();
    }

    public void scroll(int position, float offset) {
        tabWidth = getWidth() / 2;
        mTrangleTranslationX = (int) (tabWidth * (offset + position));
        mLineTranslationX = (int) (tabWidth * (offset + position));
        invalidate();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }
}
