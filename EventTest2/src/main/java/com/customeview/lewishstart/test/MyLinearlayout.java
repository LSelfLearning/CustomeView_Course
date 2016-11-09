package com.customeview.lewishstart.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyLinearlayout extends LinearLayout {
    public MyLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        View seekBar1 = getChildAt(1);
        View seekBar2 = getChildAt(2);
        if(event.getY()<getHeight()/3) {
            seekBar1.dispatchTouchEvent(event);
            seekBar2.dispatchTouchEvent(event);
        }
        return true;
    }
}
