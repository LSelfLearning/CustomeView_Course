package com.customeview.lewishstart.self04;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/5/15.
 */
public class MyToggleButton extends View implements View.OnClickListener{
    private static final String TAG = "MyToggleButton";
    private boolean isOpen = false;
    private boolean isEnableClick = true;
    private int slideLeft;
    private int slideLeftMax;
    private int lastX;
    private int startX;
    private int endX;
    private Paint paint;

    private Bitmap backgroundBitmap;
    private Bitmap slidingBitmap;
    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.switch_background);
        slidingBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.slide_button);
        slideLeftMax=backgroundBitmap.getWidth()-slidingBitmap.getWidth();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap,0,0,paint);
        canvas.drawBitmap(slidingBitmap,slideLeft,0,paint);
    }
    private void flushView(){
        if(isOpen) {
            slideLeft = slideLeftMax;
        }else {
            slideLeft = 0;
        }
        invalidate();
    }
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");
        if(isEnableClick) {
            isOpen=!isOpen;
            flushView();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case  MotionEvent.ACTION_DOWN:
                Log.i(TAG, "MotionEvent.ACTION_DOWN");
                startX = (int) event.getX();
                lastX = startX;
                isEnableClick=true;
                break;
            case  MotionEvent.ACTION_MOVE:
                Log.i(TAG, "MotionEvent.ACTION_MOVE");
                endX = (int) event.getX();
                if(Math.abs(endX-lastX)<5) {
                    isEnableClick=true;
                }else {
                    isEnableClick=false;
                }
                slideLeft += (endX-startX);
                if(slideLeft<0) {
                    slideLeft=0;
                }
                if(slideLeft>slideLeftMax) {
                    slideLeft = slideLeftMax;
                }
                invalidate();
                startX = (int) event.getX();
                break;
            case  MotionEvent.ACTION_UP:
                if(!isEnableClick) {
                    if(slideLeft>slideLeftMax/2) {
                        isOpen=true;
                    }else {
                        isOpen=false;
                    }
                    flushView();
                }
                break;
        }
        return true;
    }
}
