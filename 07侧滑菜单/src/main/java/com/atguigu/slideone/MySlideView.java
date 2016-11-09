package com.atguigu.slideone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MySlideView extends ViewGroup{

	private View content;
	
	private View menu;
	
	private boolean isMenuShow = false;
	
	public MySlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		scroller = new Scroller(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		menu = getChildAt(0);
		content = getChildAt(1);
		
		menu.measure(MeasureSpec.makeMeasureSpec(menu.getLayoutParams().width, MeasureSpec.EXACTLY)
				, heightMeasureSpec);
		
		content.measure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		System.out.println("layout:::"+getChildCount());
		
		menu.layout(0-menu.getMeasuredWidth(), 0, 0, b);
		
		content.layout(0, 0, r, b);
	}
	
	private int firstX;

	private int lastX;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			firstX = lastX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			
			int disX = (int) (lastX - event.getX());
			lastX = (int) event.getX();
			
			int nextScrollX = getScrollX()+disX; // 可能的，下一个 mScrollX 的值
			
			if( nextScrollX >= -menu.getWidth() && nextScrollX <=0){
				scrollBy(disX, 0);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			int curScrollX = getScrollX();
			
			if(curScrollX > -menu.getWidth()/2){
				isMenuShow = false;
			}else{
				isMenuShow = true;
			}
			flushState();
			
			break;
		}
		return true; 
	}

	private void flushState() {
		
		int distance = 0;
		
		if(!isMenuShow){
//			scrollTo(0,0);
			distance = 0-getScrollX();
		}else{
//			scrollTo(-menu.getWidth(),0);
			distance = -menu.getWidth()-getScrollX();
		}
		
		scroller.startScroll(getScrollX(), 0, distance, 0);
		invalidate();
		
	}
	
	@Override
	public void computeScroll() {
		if(scroller.computeScrollOffset()){
			scrollTo(scroller.getCurrX(),0);
			invalidate();
		}
	}
	private Scroller scroller ;

	public void changeState() {
		isMenuShow = !isMenuShow;
		flushState();
	}

}
