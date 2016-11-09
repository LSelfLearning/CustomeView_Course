package com.atguigu.myviewpager;

import android.os.SystemClock;

/**
 * 工具类，用于计算移动一小段对应的相关参数
 * x2 - x1 = 这段距离
要移动的总距离已经知道的
总时间：500毫秒
平均速度 = 总距离 / 总时间
求：
这一小段对应的时间
这一小段的距离
这一小段移动后对于的坐标=== 起始坐标 + 这一小段的距离 
 *
 */
public class MyScroller {
	
	/**
	 * 移动者一大段总的时间
	 */
	private long totalTime = 500;
	
	/**
	 * 开始滑动的起始X坐标
	 */
	private float startX;
	/**
	 * 开始滑动的起始Y坐标
	 */
	private float startY;
	/**
	 * 要在X轴上移动的距离
	 */
	private int distanceX;
	/**
	 * 要在Y轴上移动的距离
	 */
	private int distanceY;
	/**
	 * 刚开始滑动的起始时刻
	 */
	private long startTime;
	/**
	 * 是否移动结束
	 * true:移动结束了
	 * false:还在移动
	 */
	private boolean isFinish;

	private float currX;

	/**
	 * 得到移动这一小段后对于的X坐标
	 * @return
	 */
	public float getCurrX() {
		return currX;
	}
	public void startScroll(float startX,float startY,int distanceX,int distanceY){
		this.startX = startX;
		this.startY = startY;
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		this.startTime = SystemClock.uptimeMillis();
		this.isFinish = false;
		
	}
	/**
	 * 求：
		这一小段对应的时间
		这一小段的距离
		这一小段移动后对于的坐标=== 起始坐标 + 这一小段的距离 
	 * @return
	 * 如果返回true:代表正在移动
	 * 如果返回false:移动完成
	 */
	public boolean computeScrollOffset(){
		if(isFinish){
			return false;
		}
		
		long endTime = SystemClock.uptimeMillis();
		
		//这一小段对应的时间
		long passTime  = endTime - startTime;
		if(passTime < totalTime){
			//还没有移动完成，正在移动
			
			//平均速度 = 总距离 / 总时间
//			float veloCityX = distanceX/totalTime;
			
			//这一小段的距离 = 时间 * 速度 
			float distanceSmallX =  passTime * distanceX/totalTime;
			//  distanceX/totalTime*passTime==0
			System.out.println(" passTime * distanceX/totalTime=="+ passTime * distanceX/totalTime+"------------distanceX/totalTime*passTime=="+distanceX/totalTime*passTime);
			
			currX = startX + distanceSmallX;
			
			
		}else{
			currX = startX + distanceX;
			//移动结束
			isFinish = true;
		}
		
		
		
		return true;
		
	}

}
