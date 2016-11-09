package com.customeview.lewishstart.selfviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
    private MyScrollView msv;
    private RadioGroup rg;
    private int[] ids = { R.drawable.a1, R.drawable.a2, R.drawable.a3,
            R.drawable.a4, R.drawable.a5, R.drawable.a6 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图控件
        msv = (MyScrollView)findViewById(R.id.msv);
        rg = (RadioGroup)findViewById(R.id.rg);

        View view = View.inflate(this, R.layout.test, null);
        /**
         * 向ScrollView中添加视图
         */
        for(int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(ids[i]);
            msv.addView(imageView);
        }
        /**
         * 向ScrollView中添加测试页面
         */
        msv.addView(view,2);
        /**
         * 动态向RadioGroup中添加RadioButton
         */
        for(int i = 0; i < msv.getChildCount(); i++) {
            //向RadionGroup中添加RadioButton。
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            if(i==0) {
                radioButton.setChecked(true);
            }else {
                radioButton.setChecked(false);
            }
            rg.addView(radioButton);
        }
        msv.setOnPageChangeListener(new MyScrollView.PageChangeListener() {
            @Override
            public void moveTo(int tempIndex) {
                rg.check(tempIndex);
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                msv.moveTo(checkedId);
            }
        });
    }
}
