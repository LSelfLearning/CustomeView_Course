package com.qqyumidi.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TouchEventFather extends LinearLayout {

	public TouchEventFather(Context context) {
		super(context);
	}

	public TouchEventFather(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.e("eventTest", "Father | dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
//		return true;
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean result = false;
		Log.i("eventTest", "Father | onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		switch (ev.getAction()) {
		    case  MotionEvent.ACTION_DOWN:

		        break;
		    case  MotionEvent.ACTION_MOVE:
				result = true;
		        break;
		    case  MotionEvent.ACTION_UP:

		        break;
		}
		return result;
		//return false;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		boolean result = false;
		Log.d("eventTest", "Father | onTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		switch (ev.getAction()) {
		    case  MotionEvent.ACTION_DOWN:

				break;
		    case  MotionEvent.ACTION_MOVE:
				result = true;
		        break;
		    case  MotionEvent.ACTION_UP:

		        break;
		}
		return result;
//		return true;
	}

}
