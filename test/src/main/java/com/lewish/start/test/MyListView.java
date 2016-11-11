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
    private float mLastX;
    private float mLastY;
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
        boolean isIntercept = false;
        float x = ev.getRawX();
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE :
                float deltaX = x - mLastX;
                float deltaY = y - mLastY;

                if(Math.abs(deltaY)>Math.abs(deltaX)&&Math.abs(deltaY)> ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    isIntercept = true;
                }else {
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP :
                isIntercept = false;
                break;
        }
        mLastX = x;
        mLastY = y;
        return isIntercept;
    }
}
