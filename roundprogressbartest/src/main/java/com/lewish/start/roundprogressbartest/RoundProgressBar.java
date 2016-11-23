package com.lewish.start.roundprogressbartest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


public class RoundProgressBar extends View {
    private Paint paint;
    // 圆环的颜色
    private int roundColor;
    // 圆环进度的颜色   
    private int roundProgressColor;
    // 中间进度百分比的字符串的颜色   
    private int textColor;
    // 旋转小圆的颜色
    private int rotateRoundColor;
    // 中间进度百分比的字符串的字体大小
    private float textSize;
    // 圆环画笔的宽度
    private float roundPaintWidth;
    // 最大进度
    private int max;
    // 当前进度
    private float progress;
    // 状态
    private int status;
    // 类型 债权， 银多利
    private int type;
    // 是否显示中间的进度
    private boolean textIsDisplayable;
    // 进度的风格，实心或者空心
    private int style;
    private static final float RADIO_CIRCLE_WIDTH = 1/4f;//转动的小圆环半径与外圆半径之比

    public static final int STROKE = 0;
    public static final int FILL = 1;
    private ValueAnimator valueAnimator;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        initFromXml(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initFromXml(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.RED);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
        rotateRoundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_rotateRoundColor, Color.RED);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundPaintWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundPaintWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int padding = getPaddingRight();
        int width = getWidth() - padding * 2;
        int height = getHeight() - padding * 2;
        /**
         * 画最外层的大圆环
         */
        int centre = width / 2 + padding; //获取圆心的x坐标
        int radius = (int) (Math.min(width, height) / 2 - roundPaintWidth / 2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(0); //设置圆环线的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环
        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
//        float percent = (((float) progress / (float) max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float percent = progress;
        String content = "";
        if (!TextUtils.isEmpty(content) && percent <= 0) {
            progress = max;
        }
        if (TextUtils.isEmpty(content)) {
            content = Utils.formatDouble(percent) + "%";
        }
        float textWidth = paint.measureText(content); //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        canvas.drawText(content, centre - textWidth / 2, centre + textSize / 2, paint); //画出进度百分比
        /**
         * 画圆弧 ，画圆环的进度
         */
        paint.setStrokeWidth(roundPaintWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
        //设置进度是实心还是空心
        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }
        //画小圆
        double degress = 2*Math.PI * progress / max;
        float x = (float) ((radius)* (1 + Math.sin(degress))+padding);
        float y = (float) ((radius) * (1 - Math.cos(degress))+padding);
        paint.setColor(rotateRoundColor); //设置圆环的颜色
        paint.setAlpha(98);
        paint.setStyle(Paint.Style.FILL); //设置实心
        paint.setStrokeWidth(0); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(x, y, radius*RADIO_CIRCLE_WIDTH, paint); //画出圆环
    }

    public void dynamicDraw(float progress){
        valueAnimator = ValueAnimator.ofFloat(0,progress);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                setProgress(animatedValue);
            }
        });
        valueAnimator.start();
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized float getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = Float.valueOf(Utils.formatDouble((double) progress));
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if(valueAnimator!=null) {
            valueAnimator.cancel();
            valueAnimator.end();
            valueAnimator=null;
        }
        super.onDetachedFromWindow();
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundPaintWidth() {
        return roundPaintWidth;
    }

    public void setRoundPaintWidth(float roundPaintWidth) {
        this.roundPaintWidth = roundPaintWidth;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    /**
     * dp 2 px
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }
}
