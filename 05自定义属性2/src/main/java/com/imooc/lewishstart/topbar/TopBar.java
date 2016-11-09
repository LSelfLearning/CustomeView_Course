package com.imooc.lewishstart.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/12.
 */
public class TopBar extends RelativeLayout {
    //title的属性
    private String titleContent;
    private float titleTextSize;
    private int titleTextcolor;

    //左Button属性
    private String leftText;
    private int leftTextColor;
    private Drawable leftBackground;

    //右Button属性
    private String rightText;
    private int rightTextColor;
    private Drawable rightBackground;

    //定义控件
    private Button leftButton,rightButton;
    private TextView tvTitle;

    //定义监听器
    private TopBarClickListener listener;
    public interface TopBarClickListener{
        public void leftButtonClick();
        public void rightButtonClick();
    }
    public void setOnTopBarClickListener(TopBarClickListener listener){
        this.listener=listener;
    }
    //控件的布局参数
    LayoutParams leftLayoutParams,rightLayoutParams,titleLayoutParams;
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //一定别忘初始化控件！！！
        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);
        //从xml文件中取出参数
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TopBar);
        titleContent = typedArray.getString(R.styleable.TopBar_titleContent);
        titleTextSize = typedArray.getDimension(R.styleable.TopBar_titleTextSize,0);
        titleTextcolor = typedArray.getColor(R.styleable.TopBar_titleTextcolor,0);

        leftText = typedArray.getString(R.styleable.TopBar_leftText);
        leftTextColor = typedArray.getColor(R.styleable.TopBar_leftTextColor,0);
        leftBackground = typedArray.getDrawable(R.styleable.TopBar_leftBackground);

        rightText = typedArray.getString(R.styleable.TopBar_rightText);
        rightTextColor = typedArray.getColor(R.styleable.TopBar_rightTextColor,0);
        rightBackground = typedArray.getDrawable(R.styleable.TopBar_rightBackground);

        typedArray.recycle();
        //给左Button控件设置属性
        leftButton.setText(leftText);
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);

        //给右Button控件设置属性
        rightButton.setText(rightText);
        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        //给TextView控件设置属性
        tvTitle.setText(titleContent);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setTextColor(titleTextcolor);
        tvTitle.setGravity(Gravity.CENTER);
        //将左Button添加到布局中
        leftLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton,leftLayoutParams);//设置布局参数
        //将右Button添加到布局中
        rightLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButton,rightLayoutParams);
        //将TextView添加到布局中
        titleLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(tvTitle,titleLayoutParams);
        //设置背景色
        setBackgroundColor(Color.GREEN);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftButtonClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightButtonClick();
            }
        });
    }
}
