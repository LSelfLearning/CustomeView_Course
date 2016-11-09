package com.atguigu.quichindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：杨光福 on 2016/4/12 17:59
 * 微信：yangguangfu520
 * QQ号：541433511
 * <p>
 * 3.)在按下和移动时显示更新提示字母
 * 1.自定义接口 OnIndexChangeListener
 * 2.定义成员变量和相关set方法
 * 3.调用接口对应的方法
 * 4.使用回调接口
 * 5.实现相应的效果
 */
public class IndexView extends View {
    /**
     * 每个item的宽和高
     */
    private float itemWidth;
    private float itemHeight;
    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private Paint paint;

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        //设置粗体字
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到item的宽和高
        itemWidth  = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            //设置当前下标对应的字母为灰色，其他为白色
            if (i == touchIndex) {
                paint.setColor(Color.GRAY);
            } else {
                paint.setColor(Color.WHITE);
            }

            String word = words[i];

            Rect bounds = new Rect();
            paint.getTextBounds(word, 0, 1, bounds);

            //计算每个文字的宽和高
            int wordWidth = bounds.width();
            int wordHeight = bounds.height();

            float wordX = itemWidth / 2 - wordWidth / 2;
            float wordY = itemHeight / 2 + wordHeight / 2 + i * itemHeight;
            canvas.drawText(word, wordX, wordY, paint);
        }
    }


    /**
     * 字母的下标位置
     */
    private int touchIndex = -1;


    /**
     * 1).在按下和移动时候，使操作的字母变色
     * a.重写onTouchEvent(),返回true
     * b.在down/move时，计算出操作的下标，并且在onDraw(),设置不同颜色画笔，强制绘制
     * c.在up时，重新操作下标，强制重绘制
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float Y = event.getY();//只能用这个

                int index = (int) (Y / itemHeight);
                if (index != touchIndex) {//表示不同的字母位置
                    //当前字母的下标位置
                    touchIndex = index;
                    //强制绘制
                    invalidate();//会导致onDraw(),需要设置不同颜色的画笔

                    //调用接口的方法
                    if (onIndexChangeListener != null && touchIndex < words.length) {
                        onIndexChangeListener.onIndexChange(words[touchIndex]);
                    }
                }

                break;
            case MotionEvent.ACTION_UP://离开
                touchIndex = -1;//重置下标位置
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 监听字母下标的变化
     */
    public interface OnIndexChangeListener {
        /**
         * 当字母下标位置变化的时候，回调该方法
         *
         * @param word 字母
         */
        public void onIndexChange(String word);
    }

    private OnIndexChangeListener onIndexChangeListener;

    /**
     * 设置监听下标位置变化
     *
     * @param onIndexChangeListener
     */
    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }
}