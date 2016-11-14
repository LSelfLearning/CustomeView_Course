package com.lewish.start.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/11/11 16:50.
 */

public class MyListView extends ListView {
    private static final String TAG = "MyListView";
    private float mDownX;
    private float mDownY;
    public MyListView(Context context) {
        this(context,null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = super.onInterceptTouchEvent(ev);
        float x = ev.getRawX();
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                mDownX = x;
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE :
                float deltaX = x - mDownX;
                float deltaY = y - mDownY;

                if(Math.abs(deltaY)>Math.abs(deltaX)&&Math.abs(deltaY)> ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    isIntercept =  true;
                }
                break;
            case MotionEvent.ACTION_UP :
                break;
        }
        return isIntercept;
    }
}
