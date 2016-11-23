package com.lewish.start.customeindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sundong on 2016/11/21 16:45.
 */

public class PagerIndicator extends LinearLayout {
    private static final int COUNT_DEFAULT_TAB = 2;
    private static final String REACH_COLOR = "#d20000";
    private static final String UNREACH_COLOR = "#c0c2c6";
    private Paint mPaint;
    private Path mPath;
    private int mTabVisibleCount;
    private int tabWidth;
    private int tabHeight;

    private int mTriangleWidth;
    private int mTriangleHeight;
    private int mTrangleInitTranslationX;
    private int mTrangleTranslationX;
    private static final float RADIO_TRIANGLE_WIDTH = 1/3f;
    private int mReachColor;
    private int mUnReachColor;


    private int mLineWidth;
    private int mLineHeight;
    private int mLineTranslationX;
    private int childCount;

    private ViewPager mViewPager;
    private OnPageChangeListener mOnPageChangeListener;
    private List<String> mTabTitles;


    public PagerIndicator(Context context) {
        this(context, null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.PagerIndicator);
        mReachColor = typedArray.getColor(R.styleable.PagerIndicator_reachColor,Color.parseColor(REACH_COLOR));
        mUnReachColor = typedArray.getColor(R.styleable.PagerIndicator_unReachColor,Color.parseColor(UNREACH_COLOR));
        mTabVisibleCount = typedArray.getInteger(R.styleable.PagerIndicator_visibleCount,COUNT_DEFAULT_TAB);
        tabWidth = getScreenWidth()/mTabVisibleCount;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    /**
     * 倒三角
     */
    private void initTriangle() {
        mTriangleHeight = mTriangleWidth / 2;//默认三角形高度是宽度的1/2
        mLineHeight = mTriangleWidth / 6;//默认线的高度是三角形宽度的1/6
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, mTriangleHeight);
        mPath.close();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childCount = getChildCount();
        if(childCount ==0) {
            return;
        }
        for (int i = 0; i<childCount; i++){
            View childView = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) childView.getLayoutParams();
            lp.weight = 0;
            lp.width = tabWidth;
            childView.setLayoutParams(lp);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setStrokeWidth(mLineHeight);
        //画灰线
        mPaint.setColor(mUnReachColor);
        canvas.drawLine(0,tabHeight-mTriangleHeight,tabWidth*childCount,tabHeight-mTriangleHeight,mPaint);
        //画红线
        mPaint.setColor(mReachColor);
        canvas.drawLine(mLineTranslationX,tabHeight-mTriangleHeight,mLineTranslationX+tabWidth,tabHeight-mTriangleHeight,mPaint);
        //画三角
        canvas.save();
        canvas.translate(mTrangleInitTranslationX + mTrangleTranslationX, tabHeight-mTriangleHeight);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (h * RADIO_TRIANGLE_WIDTH);
        mTrangleInitTranslationX = w / mTabVisibleCount / 2 - mTriangleWidth / 2;//默认小三角形的位置
        tabHeight = h;
        initTriangle();
    }

    /**
     * 滑动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        //三角形要移动的距离
        mTrangleTranslationX = (int) (tabWidth * (offset + position));
        //红线要移动的距离
        mLineTranslationX = (int) (tabWidth * (offset + position));
        // 容器滚动，当移动到倒数最后一个的时候，开始滚动
        if ((position<(childCount-2))&& position >= (mTabVisibleCount - 2) &&offset>0&& getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * offset), 0);
            } else {// 为count为1时 的特殊处理
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();
    }

    /**
     * 设置Tab数据集合
     * @param datas
     */
    public void setTabItemTitles(List<String> datas) {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (datas != null && datas.size() > 0) {
            this.removeAllViews();
            this.mTabTitles = datas;
            childCount = datas.size();
            tabWidth = getScreenWidth()/mTabVisibleCount;
            for (String title : mTabTitles) {
                // 添加view
                addView(generateTextView(title));
            }
        }
        // 设置item的click事件
        setItemClickEvent();
    }
    private TextView generateTextView(String text) {
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mTabVisibleCount;
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(mUnReachColor);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 高亮文本
     * @param position
     */
    protected void highLightTextView(int position) {
        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(mReachColor);
        }
    }
    /**
     * 重置文本颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(mUnReachColor);
            }
        }
    }
    protected void shadowTextColor(int position, float positionOffset){
        View preView = getChildAt(position);
        View nextView = null;
        if(position!=childCount-1) {
            nextView = getChildAt(position + 1);
        }
        if (preView instanceof TextView) {
            ((TextView) preView).setAlpha(1-positionOffset);
        }
        if(nextView!=null) {
            if (nextView instanceof TextView) {
                ((TextView) nextView).setAlpha(positionOffset);
            }
        }
    }
    /**
     * 设置点击事件
     */
    public void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
    public int getTabVisibleCount() {
        return mTabVisibleCount;
    }

    public void setTabVisibleCount(int mTabVisibleCount) {
        this.mTabVisibleCount = mTabVisibleCount;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setmOnPageChangeListener(OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    public interface OnPageChangeListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    /**
     * 关联ViewPager
     * @param viewPager
     * @param defaultPos
     */
    public void setUpWithViewPager(ViewPager viewPager, final int defaultPos) {
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position,positionOffset);
//                shadowTextColor(position,positionOffset);
                if(mOnPageChangeListener!=null) {
                    mOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                // 设置字体颜色高亮
                resetTextViewColor();
                highLightTextView(position);
                if(mOnPageChangeListener!=null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(mOnPageChangeListener!=null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(defaultPos);
    }

    /**
     * 得到屏幕宽度
     * @return
     */
    public int getScreenWidth()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }
}
