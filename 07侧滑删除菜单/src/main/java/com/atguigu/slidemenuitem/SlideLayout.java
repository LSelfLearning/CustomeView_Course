package com.atguigu.slidemenuitem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
/**
 * 作者：杨光福 on 2016/4/9 07:43
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：侧滑菜单的item
 * 1.正常显示item代码实现
 * 1.1).得到子View对象（ContentView,MenuView）-->onFinishInflate()
 * 1.2).得到子View的宽和高-->onMeasure()
 * 1.3).对子View进行重新布局-->onLayout
 * 2.通过手势拖动把menu打开或者关闭
 * 2.1).响应用户操作-重写onToucheEvent() return true;
 * 2.2).在move事件记录移动，对视图进行滚动，scrollTo()
 * 2.3).当up的时候，计算总的偏移量，判断是平滑的关闭或者打开
 * 2.4).平滑的关闭或者打开
 */
public class SlideLayout extends FrameLayout {

    private View contentView, menuView;

    private int contentWidth, menuWidth, ViewHeight;

    private Scroller scroller;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    /**
     * 当布局文件加载完成后回调这个方法
     * 1.1).得到子View对象（ContentView,MenuView）-->onFinishInflate()
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }


    /**
     * 1.2).得到子View的宽和高-->onMeasure()
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        contentWidth = contentView.getMeasuredWidth();
//        contentWidth = getMeasuredWidth();//可以
        menuWidth = menuView.getMeasuredWidth();
//        menuWidth = getMeasuredWidth();//不可以

        ViewHeight = getMeasuredHeight();

    }

    /**
     * * 1.3).对子View进行重新布局-->onLayout
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, ViewHeight);//
    }

    /**
     * 第一次按下的值
     */
    private int lastX;
    private int downX;
    private int lastY;
    private int downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录起始坐标
                downX = lastX = eventX;
                downY = lastY = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                //2.计算偏移量

                int distenceX = eventX - lastX;
                int distenceY = eventY - lastY;
                int toScrollX = getScrollX() - distenceX;
                System.out.println(toScrollX);
                //屏蔽非法值
                if (toScrollX < 0) {
                    toScrollX = 0;
                }else if(toScrollX > menuWidth){
                    toScrollX = menuWidth;
                }

//                scrollTo(toScrollX, 0);//也可以
                scrollTo(toScrollX, getScrollY());

                //重新赋值
                lastX = eventX;

                int dX = Math.abs(eventX - downX);
                int dY = Math.abs(eventY - downY);

                if(dX > dY&& dX >8){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }


                break;
            case MotionEvent.ACTION_UP:
               // 2.3).当up的时候，计算总的偏移量，判断是平滑的关闭或者打开
                int totallScrollX = getScrollX();
                if(totallScrollX < menuWidth/2){
                    System.out.println("totallScrollX < menuWidth/2");
                    //关闭菜单
                    closeMenu();
                }else{
                    System.out.println("totallScrollX >= menuWidth/2");
                    //打开菜单
                    openMenu();
                }
                break;
        }

//        return super.onTouchEvent(event);
        return true;
    }

    public void openMenu() {//--->menuWidth
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), getScrollY());
        invalidate();//会导致 执行computeScroll
        if(onStateChangeListener != null){
            onStateChangeListener.onOpen(this);
        }
    }

    public void closeMenu() {//--->0
        scroller.startScroll(getScrollX(),getScrollY(),0-getScrollX(),getScrollY());
        invalidate();//会导致 执行computeScroll
        if(onStateChangeListener != null){
            onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();//强制重绘制
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录起始坐标
                downX = lastX = eventX;
                downY = lastY = eventX;
                if(onStateChangeListener != null){
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //2.计算偏移量
                int dX = Math.abs(eventX - downX);
                if( dX >8){
                    intercept = true;
                }

                break;
        }
        return intercept;
    }


    public interface OnStateChangeListener{
        /**
         * 当item被打开的时候回调
         * @param layout
         */
        public void onOpen(SlideLayout layout);

        /**
         * 当item关闭的时候回调
         * @param layout
         */
        public void onClose(SlideLayout layout);

        /**
         * 当item按下的时候被回调
         * @param layout
         */
        public void onDown(SlideLayout layout);
    }


    private OnStateChangeListener onStateChangeListener;


    public void setStateChangeListener(OnStateChangeListener stateChangeListener) {
        this.onStateChangeListener = stateChangeListener;
    }
}
