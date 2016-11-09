package com.customeview.lewishstart.self09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/5/18.
 */
public class IndexView extends View {
    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private Context context;
    private int itemWidth;
    private int itemHeight;
    private int viewHeight;
    private int viewWidth;
    private int letterX;
    private int letterY;
    private int touchIndex = -1;

    private Paint paint;
    private LetterChangeListener letterChangeListener;
    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setTextSize(40);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        itemWidth = viewWidth;
        itemHeight = viewHeight/words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0; i < words.length; i++) {
            String letter = words[i];
            if(touchIndex==i) {
                paint.setColor(Color.BLACK);
            }else {
                paint.setColor(Color.WHITE);
            }
            Rect bounds = new Rect();
            paint.getTextBounds(letter,0,1,bounds);

            int letterWidth = bounds.width();
            int letterHeight = bounds.height();

            letterX = itemWidth/2-letterWidth/2;
            letterY = itemHeight/2+letterWidth/2+i*itemHeight;

            canvas.drawText(letter,letterX,letterY,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case  MotionEvent.ACTION_DOWN:
            case  MotionEvent.ACTION_MOVE:
                    int index = (int) (event.getY()/itemHeight);
                    if(touchIndex!=index) {
                        touchIndex=index;
                        if(letterChangeListener!=null&&touchIndex<26) {
                            letterChangeListener.changeLetter(words[touchIndex]);
                        }
                        invalidate();
                    }
                break;
            case  MotionEvent.ACTION_UP:
                    touchIndex = -1;
                    invalidate();
                break;
        }
        return true;
    }
    public interface LetterChangeListener{
        public void changeLetter(String letter);
    }
    public void setOnLetterChangeListener(LetterChangeListener letterChangeListener){
        this.letterChangeListener = letterChangeListener;
    }
}
