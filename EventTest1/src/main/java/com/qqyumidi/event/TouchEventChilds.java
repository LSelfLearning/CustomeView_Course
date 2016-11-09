package com.qqyumidi.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class TouchEventChilds extends LinearLayout {
	private  View childView;
	private Scroller scroller;

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

//		childView = getChildAt(0);
	}

	public TouchEventChilds(Context context) {
		super(context);
	}

	public TouchEventChilds(Context context, AttributeSet attrs) {
		super(context, attrs);
//		scroller = new Scroller(context) ;
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.e("eventTest", "Childs | dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
//		return true;
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.i("eventTest", "Childs | onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.onInterceptTouchEvent(ev);
//		return true;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		boolean result = false;
		android.util.Log.d("eventTest", "Childs | onTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		super.onTouchEvent(ev);
		switch (ev.getAction()) {
		    case  MotionEvent.ACTION_DOWN:
				result = true;
		        break;
		    case  MotionEvent.ACTION_MOVE:

		        break;
		    case  MotionEvent.ACTION_UP:

		        break;
		}
		return result;
//		return true;
	}

//	private float startX;
//	private float startY;
//
//	public boolean onTouchEvent(MotionEvent ev) {
//		super.onTouchEvent(ev);
//		Log.d("eventTest", "Childs | onTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
//		switch (ev.getAction()){
//			case MotionEvent.ACTION_DOWN:
//				startX = ev.getX();
//				startY = ev.getY();
//				Log.d("yangguangfu", "========ACTION_DOWN=========" + startX+","+startY);
//				break;
//			case MotionEvent.ACTION_MOVE:
//				float endX = ev.getX();
//				float endY = ev.getY();
//
//
//				float distanceX = endX - startX;
//				float distanceY = endY - startY;
//
////				Log.d("yangguangfu", "distanceX ======================" + distanceX);
////				Log.d("yangguangfu", " childView.getScrollX ===================" + childView.getScrollX());
////
////				Log.d("yangguangfu", " getScrollX ===================" + getScrollX());
//
//				float DX = getScrollX() - distanceX;
//
////				Log.d("yangguangfu", "( " + startX+",,,"+startY+")"+"--("+endX+",,,"+endY+")--("+distanceX+",,,"+distanceY+")"+",DX==="+DX);
////				Log.d("yangguangfu", "getScrollX ======================" + getScrollX());
//
//
//
////				childView.scrollTo((int) DX, (int) 0);//移动的是相当坐标
//				childView.scrollBy((int) DX, (int) 0);//移动的是一下断的距离
//
//				startX = ev.getX();
//				startY = ev.getY();
//
//
//				break;
//			case MotionEvent.ACTION_UP:
//				int totalScrollX = childView.getScrollX();
//
//				Log.d("yangguangfu", "关闭 " + childView.getScrollX());
//				//公式
//				//X
//				//int dx =  目标- getScrollX()
//
////				int X = -childView.getMeasuredWidth() - childView.getScrollX();
//				int X = 0 - childView.getScrollX();
//				Log.d("yangguangfu", "( " + childView.getScrollX()+",,,"+childView.getScrollY()+")"+"--("+X+",,,"+0+")"+",childViewWidth="+childView.getMeasuredWidth());
//				scroller.startScroll(childView.getScrollX(), childView.getScrollY(), X, 0);
//				invalidate();
//
//
//				break;
//		}
////		return super.onTouchEvent(ev);
//		return true;
//	}
//
//	@Override
//	public void computeScroll() {
//		super.computeScroll();
//		if(scroller .computeScrollOffset()){
//
//			childView.scrollTo(scroller.getCurrX(),scroller.getCurrY());
//			invalidate();
//		}
//	}
}
