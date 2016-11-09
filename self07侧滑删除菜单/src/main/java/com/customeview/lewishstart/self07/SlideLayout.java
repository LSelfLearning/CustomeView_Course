package com.customeview.lewishstart.self07;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/5/19.
 */
public class SlideLayout extends LinearLayout {
    private int menuWidth;
    private int contentWidth;
    private int itemHeight;
    private int itemWidth;

    private View contentView;
    private View menuView;

    private Scroller scroller;
    private Context context;

    private float lastX;
    private float lastY;
    private float downX;
    private float downY;

    private SildeStateChangeListener sildeStateChangeListener;
    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    private void initView() {
        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemHeight = getMeasuredHeight();
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        contentView.layout(0, 0, contentWidth, itemHeight);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, itemHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = downX = eventX;
                lastY = downY = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = (int) (eventX - lastX);
                int distanceY = (int) (eventY - lastY);
                lastX = eventX;
                lastY = eventY;
                int toScrollX = getScrollX() - distanceX;
                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());
                //在X,Y轴滑动的距离
                float dX = Math.abs(eventX-downX);
                float dY = Math.abs(eventY -downY);
                if(dX>dY&&dX>10) {//反拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
               if(getScrollX()<menuWidth/2) {
                   closeMenu();
                   if(sildeStateChangeListener!=null) {
                       sildeStateChangeListener.onCloseMenu(this);
                   }
               }else {
                   openMenu();
                   if(sildeStateChangeListener!=null) {
                       sildeStateChangeListener.onOpenMenu(this);
                   }
               }
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        float eventX = ev.getX();
        float eventY = ev.getY();
        switch (ev.getAction()) {
            case  MotionEvent.ACTION_DOWN:
                lastX = downX = eventX;
                lastY = downY = eventY;
                if(sildeStateChangeListener!=null) {
                    sildeStateChangeListener.onClick(this);
                }
                //Log.i("TAG", "SlideLayout onInterceptTouchEvent() MotionEvent.ACTION_DOWN");
                break;
            case  MotionEvent.ACTION_MOVE:
                //Log.i("TAG", "SlideLayout onInterceptTouchEvent() MotionEvent.ACTION_MOVE");
                if(Math.abs(eventX-lastX)>10) {
                    isIntercept = true;
                    //Log.i("TAG", "拦截");
                }
                break;
            case  MotionEvent.ACTION_UP:

                break;
        }
        return isIntercept;
    }

    public void openMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth-getScrollX(), getScrollY());
        invalidate();
    }

    public void closeMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), getScrollY());
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), getScrollY());
            invalidate();
        }
    }
    public interface SildeStateChangeListener{
        public void onOpenMenu(SlideLayout slideLayout);
        public void onCloseMenu(SlideLayout slideLayout);
        public void onClick(SlideLayout slideLayout);
    }
    public void setOnSildeStateChangeListener(SildeStateChangeListener sildeStateChangeListener){
        this.sildeStateChangeListener = sildeStateChangeListener;
    }
}
