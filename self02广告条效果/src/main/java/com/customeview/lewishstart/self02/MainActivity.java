package com.customeview.lewishstart.self02;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final int NEXT_AD = 0;
    /**
     * 图片的ID资源
     */
    private int[] ids = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};

    /**
     *  图片标题集合
     */
    private final String[] imageDescriptions = {
            "尚硅谷波河争霸赛！",
            "凝聚你我，放飞梦想！",
            "抱歉没座位了！",
            "7月就业名单全部曝光！",
            "平均起薪11345元"
    };

    private ViewPager vp_main_ad;
    private TextView tv_main_ad;
    private LinearLayout ll_main_ad;
    private List<ImageView> views;
    private MyPagerAdapter myPagerAdapter;
    //上一次高亮显示的位置
    private int prePosition=0;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case  NEXT_AD:
                    vp_main_ad.setCurrentItem(vp_main_ad.getCurrentItem()+1);
                    sendEmptyMessageDelayed(NEXT_AD,4000);
                    break;
            }
        }
    };
    private boolean isDragging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPagerAdapter = new MyPagerAdapter();
        views = new ArrayList<ImageView>();
        //初始化视图控件
        vp_main_ad = (ViewPager)findViewById(R.id.vp_main_ad);
        tv_main_ad = (TextView)findViewById(R.id.tv_main_ad);
        ll_main_ad = (LinearLayout)findViewById(R.id.ll_main_ad);
        //准备数据
        initData();
        vp_main_ad.setAdapter(myPagerAdapter);
        //保证从第0张开始
        int startItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % views.size();
        vp_main_ad.setCurrentItem(startItem);
        tv_main_ad.setText(imageDescriptions[0]);
        vp_main_ad.addOnPageChangeListener(new MyOnPageChangeListener());
        handler.sendEmptyMessageDelayed(NEXT_AD,3000);
    }

    private void initData() {
        for (int i=0;i<ids.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            views.add(imageView);

            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10),DensityUtil.dip2px(this, 10));
            if(i==0) {
                point.setEnabled(true);
            }else{
                point.setEnabled(false);
                layoutParams.leftMargin=DensityUtil.dip2px(this, 10);
            }
            ll_main_ad.addView(point,layoutParams);
        }
    }

    class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = views.get(position%views.size());
            container.removeView(imageView);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = views.get(position%views.size());
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case  MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case  MotionEvent.ACTION_MOVE:

                            break;
                        case  MotionEvent.ACTION_CANCEL:

                            break;
                        case  MotionEvent.ACTION_UP:
                            handler.sendEmptyMessageDelayed(NEXT_AD,3000);
                            break;
                    }
                    return false;
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,imageDescriptions[position%views.size()], Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(imageView);
            return imageView;
        }
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        /**
         * 当页面滚动了的时候回调这个方法
         * @param position 当前页面的位置
         * @param positionOffset 滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            
        }

        /**
         * 当某个页面被选中时回调
         * @param position 被选中的页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            tv_main_ad.setText(imageDescriptions[position%views.size()]);
            int currentPosition = position % views.size();

            ll_main_ad.getChildAt(prePosition).setEnabled(false);
            ll_main_ad.getChildAt(currentPosition).setEnabled(true);
            prePosition = currentPosition;
        }
        /**
         当页面滚动状态变化的时候回调这个方法
         静止->滑动
         滑动-->静止
         静止-->拖拽
         SCROLL_STATE_DRAGGING 页面被拖动
         SCROLL_STATE_SETTLING 页面滑动
         SCROLL_STATE_IDLE 页面空闲（普通状态）
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                handler.removeCallbacksAndMessages(null);
            }else if(state == ViewPager.SCROLL_STATE_SETTLING) {//滑动状态
                
            }else if(state == ViewPager.SCROLL_STATE_IDLE&&isDragging) {
                isDragging = false;
                handler.sendEmptyMessageDelayed(NEXT_AD,3000);
            }
        }
    }
}
