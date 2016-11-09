package com.qqyumidi.event;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class TouchEventActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.w("eventTest", "Activity | dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
//		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.w("eventTest", "Activity | onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
		return super.onTouchEvent(event);
	}

}