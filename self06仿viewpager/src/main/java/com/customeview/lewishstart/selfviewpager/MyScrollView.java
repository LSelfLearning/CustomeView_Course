package com.customeview.lewishstart.selfviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyScrollView extends ViewGroup {
    private Scroller scroller;
    private float startX;
    private float startY;
    private float endX;
    private int curIndex;
    private int tempIndex;
    private GestureDetector gestureDetector;
    private PageChangeListener pageChangeListener;

    public interface PageChangeListener{
        public void moveTo(int tempIndex);
    }
    public void setOnPageChangeListener(PageChangeListener pageChangeListener){
        this.pageChangeListener = pageChangeListener;
    }
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        scroller = new Scroller(context);
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            //SimpleOnGestureListener获得GestureDetector转发的MotionEvent对象，并作出相应反馈
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scrollBy((int) distanceX,getScrollY());
                return true;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout(i*getWidth(),0,(i+1)*getWidth(),getHeight());
        }
    }

    public void moveTo(int tempIndex) {
        //对tempIndex做一些限制，防止非法值的出现
        if(tempIndex<0) {
            tempIndex=0;
        }
        if(tempIndex>=getChildCount()-1) {
            tempIndex = getChildCount()-1;
        }
        curIndex = tempIndex;
        //计算要移动的距离
        int distance = curIndex*getWidth()-getScrollX();
        if(pageChangeListener!=null) {
            pageChangeListener.moveTo(tempIndex);
        }
        scroller.startScroll(getScrollX(),getScrollY(),distance,0,800);
        invalidate();//会导致onDraw和computeScroll（）方法执行
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(),getScrollY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        for(int i = 0; i < getChildCount(); i++) {
          getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        gestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case  MotionEvent.ACTION_DOWN:
                Log.i("TAG", "MyScrollView onInterceptTouchEvent() ACTION_DOWN");
                    startX = ev.getX();
                    startY = ev.getY();
                break;
            case  MotionEvent.ACTION_MOVE:
                Log.i("TAG", "MyScrollView onInterceptTouchEvent() ACTION_MOVE");
                float endX = ev.getX();
                float endY = ev.getY();
                float distanceX = Math.abs(endX-startX);
                float distanceY = Math.abs(endY-startY);
                if(distanceX>distanceY&&distanceX>10) {
                    result = true;//消费掉该事件，中断事件的传递
                }
                break;
            case  MotionEvent.ACTION_UP:
                Log.i("TAG", "MyScrollView onInterceptTouchEvent() ACTION_UP");
                break;
        }
        return result;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //GestureDetector转发MotionEvent对象至SimpleOnGestureListener
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case  MotionEvent.ACTION_DOWN:
                Log.e("TAG", "MyScrollView onTouchEvent() ACTION_DOWN");
                startX = event.getX();
                break;
            case  MotionEvent.ACTION_MOVE:
                Log.e("TAG", "MyScrollView onTouchEvent() ACTION_MOVE");
                break;
            case  MotionEvent.ACTION_UP:
                Log.e("TAG", "MyScrollView onTouchEvent() ACTION_UP");
                endX = event.getX();
                tempIndex = curIndex;
                if(endX-startX>getWidth()/2) {
                    tempIndex--;
                }
                if(startX-endX>getWidth()/2) {
                    tempIndex++;
                }
                moveTo(tempIndex);
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
