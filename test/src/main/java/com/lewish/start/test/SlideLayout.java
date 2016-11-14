package com.lewish.start.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by sundong on 2016/11/11 14:27.
 */

public class SlideLayout extends FrameLayout{
    private static final String TAG = "SlideLayout";
    private View mContentView;
    private View mMenuView;
    private Scroller mScroller;

    private int mContentWidth;
    private int mMenuWidth;
    private int mHeight;

    private float mLastX;
    private float mLastY;
    private float mDownX;
    private float mDownY;

    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        mContentView = getChildAt(0);
        mMenuView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mContentWidth = mContentView.getMeasuredWidth();
        mHeight = mContentView.getMeasuredHeight();

        mMenuWidth = mMenuView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mContentView.layout(0, 0, mContentWidth, mHeight);
        mMenuView.layout(mContentWidth, 0, mContentWidth + mMenuWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mLastX;
                float deltaY = y - mLastY;
                float dX = Math.abs(x - mDownX);
                float dY = Math.abs(y - mDownY);

                float toScrollX = getScrollX() - deltaX;
                float toScrollY = getScrollY() - deltaY;
                //限位
                if (toScrollX > mMenuWidth) {
                    toScrollX = mMenuWidth;
                }
                if (toScrollX < 0) {
                    toScrollX = 0;
                }
                scrollTo((int) toScrollX, 0);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //回弹
                int wTh = mMenuWidth / 2;
                if (getScrollX() > wTh) {
                    openMenu();
                } else {
                    closeMenu();
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mLastX;
                float deltaY = y - mLastY;

                if (Math.abs(deltaY) > Math.abs(deltaX)&&Math.abs(deltaY)>ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = super.onInterceptTouchEvent(event);
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录起始坐标
                mDownX = eventX;
                mDownY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                //2.计算偏移量
                int dX = (int) Math.abs(eventX - mDownX);
                if (dX > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    intercept = true;
                }
                break;
        }
        return intercept;
    }

    private void xSmoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        mScroller.startScroll(scrollX, 0, deltaX, 0);
        invalidate();
    }

    private void ySmoothScrollTo(int destX, int destY) {
        int scrollY = getScrollX();
        int deltaY = destY - scrollY;
        mScroller.startScroll(0, scrollY, 0, deltaY);
        invalidate();
    }

    private void xySmothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        int scrollY = getScrollX();
        int deltaY = destY - scrollY;
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void openMenu() {
        if(onSlideLayoutStateChangedListener!=null) {
            onSlideLayoutStateChangedListener.onMenuOpen(this);
        }
        xSmoothScrollTo(mMenuWidth, 0);
    }

    public void closeMenu() {
        if(onSlideLayoutStateChangedListener!=null) {
            onSlideLayoutStateChangedListener.onMenuClose(this);
        }
        xSmoothScrollTo(0, 0);
    }

    public interface onSlideLayoutStateChangedListener{
        public void onMenuOpen(SlideLayout slideLayout);
        public void onMenuClose(SlideLayout slideLayout);
    }
    private onSlideLayoutStateChangedListener onSlideLayoutStateChangedListener;

    public void setOnSlideLayoutStateChangedListener(SlideLayout.onSlideLayoutStateChangedListener onSlideLayoutStateChangedListener) {
        this.onSlideLayoutStateChangedListener = onSlideLayoutStateChangedListener;
    }
}
