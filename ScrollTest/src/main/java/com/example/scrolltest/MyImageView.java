package com.example.scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Scroller;

public class MyImageView extends ImageView {

	private Scroller scroller;

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		scroller = new Scroller(context);
	}
	
	public void reset() {
		scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY(), 1000);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		if(scroller.computeScrollOffset()) {//滑动还没有完成
			Log.e("TAG", "CurrX="+scroller.getCurrX());
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			invalidate();
		}
	}
}
