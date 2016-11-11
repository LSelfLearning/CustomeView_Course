package com.atguigu.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 自定义类继承ViewGroup
 *
 * @author 杨光福 一个View(可能是View或者是ViewGroup)显示到屏幕主要经历的方法
 *         1.测量高和宽-measure()--->onMeasure(int,int);
 *         2.指定控件的位置-layout()--->onLayout(boolean,int,int,int,int);
 *         普通的View不需要实现这个方法，但是ViewGroup一定要实现该方法 指定位置过程中，孩子有建议权，父类（ViewGroup）有决定权。
 *         3.根据测量的结果绘制到屏幕上draw()--->onDraw(canvas);
 */
public class MyViewPagerView extends ViewGroup {
    /**
     * 手势识别器 1.定义 2.实例化 3.接收触摸事件
     */
    // 1.定义手势识别器;
    private GestureDetector detector;
    private Scroller scroller;
    /**
     *1.先定义接口
     *2.当事件改变的时候要调用接口对应的方法
     * 添加支持用户实现该接口的方法
     * 调用接口
     *3.用户要使用接口
     */

    /**
     * 该接口用于监听页面的变化
     */
    public interface PageChangeListener {
        /**
         * 当页面改变的时候，回调这个方法，并且传入改变页面对应的下标位置
         *
         * @param currentIndex
         */
        public void moveTo(int currentIndex);
    }

    private PageChangeListener changeListener;


    /**
     * 设置监听页面改变
     * 当页面改变的时候，会回调PageChangeListener的moveTo(int currentIndex)方法
     *
     * @param changeListener
     */
    public void setChangeListener(PageChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * 在布局文件中使用将会被调用
     *
     * @param context
     * @param attrs
     */
    public MyViewPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        scroller = new Scroller(context);
        // 2.实例化手势识别器
        detector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    /**
                     * e1:按下开始要移动的信息 e2:按下移动结束移动的信息 distanceX：在屏幕上X轴移动的距离
                     * distanceY：在屏幕上Y轴移动的距离
                     */
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                            float distanceX, float distanceY) {
                        /**
                         * 移动距离 x：要在屏幕上X轴移动的距离 y：要在屏幕上Y轴移动的距离
                         */
                        scrollBy((int) distanceX, 0);
//						System.out.println("distanceX==" + distanceX);
                        return true;
                    }
                });

    }

    /**
     * 测量view的高和宽
     * widthMeasureSpec:包含父类给当前孩子宽和模式
     * 三种模式：未指定，至多，未知
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("widthMeasureSpec==" + widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);//得到宽
        int mode = MeasureSpec.getMode(widthMeasureSpec);////得到模式
        System.out.println("widthMeasureSpec==" + widthMeasureSpec + ",width==" + width + ",mode==" + mode);
//		MeasureSpec.makeMeasureSpec(size, mode);//根据新的宽和新的模式，生成新的宽和高

        //把所有的孩子遍历出来测量；这7个孩子是MyViewPagerView的一级View
        //只要把这7个孩子都测量了，就会把MyViewPagerView对应一级View的孩子的View测量
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 指定控件的位置：包含孩子，有多个，遍历
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 遍历得到每一个孩子，给每一个孩子指定位置
        for (int i = 0; i < getChildCount(); i++) {
            View children = getChildAt(i);
            // 只能画最后一个页面，其他的都被覆盖了
            // children.layout(0, 0, getWidth(), getHeight());
            children.layout(i * getWidth(), 0, (i + 1) * getWidth(),
                    getHeight());
        }
    }

    private float startX;
    /**
     * 各个页面的下标位置
     */
    private int currentIndex;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        System.out.println("dispatchTouchEvent---------");
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 第一次按下的X轴坐标
     */
    private float downX;
    /**
     * 第一次按下的Y轴坐标
     */
    private float downY;

    /**
     * 是否中断事件，如果返回false,事件继续传递给孩子，
     * 如果返回true,事件中断，将会调用当前View的onTouchEvent()方法
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        boolean isIntercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("onInterceptTouchEvent--------ACTION_DOWN-");
                //1.记录坐标
                downX = ev.getRawX();
                downY = ev.getRawY();
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("onInterceptTouchEvent--------ACTION_MOVE-");
                //2.来到新的坐标
                float newX = ev.getRawX();
                float newY = ev.getRawY();
                //3.计算移动的距离
                float distanceX = Math.abs(newX - downX);
                float distanceY = Math.abs(newY - downY);
                //4.比较大小（谁大就代表在他的方向上滑动）此处根据业务逻辑判断
                if (distanceX > distanceY) {
                    //返回true:
                    //如果返回true,事件中断，将会调用当前View的onTouchEvent()方法
                    isIntercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onInterceptTouchEvent--------ACTION_UP-");
                break;
            default:
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEvent(event);// 还会继续传递事件
        detector.onTouchEvent(event);
//		System.out.println("event.getX()=="+event.getX()+",,,,,,,,,event.getRawX()=="+event.getRawX());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下屏幕的时候
                System.out.println("onTouchEvent--------ACTION_DOWN-");
                //1.记录坐标
                startX = event.getX();//只能用getX();
                break;
            case MotionEvent.ACTION_MOVE://手指在屏幕上移动的时候
                System.out.println("onTouchEvent--------ACTION_MOVE-");
                break;
            case MotionEvent.ACTION_UP://手指离开屏幕的时候
                System.out.println("onTouchEvent--------ACTION_UP-");
                //2.来到新的坐标
                float newX = event.getX();
                //3.计算偏移量
                float distanceX = newX - startX;
                //要移动到对应的页面
                int tempIndex = currentIndex;
                if (distanceX > getWidth() / 2) {
                    //上一个页面
                    tempIndex--;
                } else if ((startX - newX) > getWidth() / 2) {
                    //显示下一个页面
                    tempIndex++;
                }
                //根据下标位置移动到对应的页面
                moveTo(tempIndex);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 根据下标位置移动到对应的页面
     *
     * @param tempIndex
     */
    public void moveTo(int tempIndex) {
        // TODO Auto-generated method stub
        //屏幕非法值
        if (tempIndex < 0) {
            tempIndex = 0;
        }
        if (tempIndex > getChildCount() - 1) {
            tempIndex = getChildCount() - 1;
        }
        //在这里是修正过的
        currentIndex = tempIndex;
        //判断为空
        if (changeListener != null) {
            changeListener.moveTo(currentIndex);
        }
        //用于坐标来移动
        //瞬间移动
//		scrollTo(currentIndex*getWidth(), 0);
        //要移动的总距离
        int distanceX = currentIndex * getWidth() - getScrollX();

        //起始的时刻-这一小段的
//		scroller.startScroll(getScrollX(), 0, distanceX, 0);
//		System.out.println(" Math.abs(distanceX)==="+ Math.abs(distanceX));
        scroller.startScroll(getScrollX(), 0, distanceX, 0, Math.abs(distanceX));
        //刷新页面
        invalidate();//会导致onDraw方法执行;computeScroll()
    }

    @Override
    public void computeScroll() {//这是View里的方法
        //结束的时刻-这一小段
        if (scroller.computeScrollOffset()) {
            //移动这一小段后对应的坐标
            scrollTo((int) scroller.getCurrX(), 0); //根据具体业务修改
            //刷新页面
            postInvalidate();//会导致onDraw方法执行;computeScroll()
        }
    }
}
