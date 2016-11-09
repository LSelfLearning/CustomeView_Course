package com.atguigu.myviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.atguigu.myviewpager.MyViewPagerView.PageChangeListener;

public class MainActivity extends Activity {
	
	/**
	 * 图片id集合
	 */
	private int[] ids = {R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6};
	
	
	private MyViewPagerView my_viewpager;
	private RadioGroup radiogroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		my_viewpager = (MyViewPagerView) findViewById(R.id.my_viewpager);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		
		//添加各个页面
		for(int i =0; i < ids.length; i++){
			
			//实例化了一张图
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(ids[i]);//一定要设置背景
			
			//添加到自定义的类中-ViewGroup
			my_viewpager.addView(imageView);
			
		}
		
		//添加测试页面-孩子
		//View.inflate把一个布局文件转换成View
		View test = View.inflate(this, R.layout.test, null);
		my_viewpager.addView(test, 2);
		
		
		
		
		//得到孩子的个数，设置有多少个RadioButton
		for(int i=0;i<my_viewpager.getChildCount();i++){
			
			RadioButton button = new RadioButton(this);
			button.setId(i);//设置id不能少
			if(i==0){
				button.setChecked(true);
			}
			
			//添加到RadioGroup里面去
			radiogroup.addView(button);
			
			
		}
		
		
		//3.用户要使用接口
		my_viewpager.setChangeListener(new MyPageChangeListener());
		
		//设置RadioGroup的选中监听
		radiogroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
	}
	class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		/**
		 * checkedId:被选中的id：0~5
		 * 页面的下标：0~5
		 */
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			//根据下标跳转到对应的页面，但是这个方法目前是私有
			//MyViewPagerView .moveTo(int);
			my_viewpager.moveTo(checkedId);
			
		}
		
	}
	
	class MyPageChangeListener implements PageChangeListener{

		/**
		 * currentIndex:0~5;
		 * RadioButton的id：0~5
		 * currentIndex下标位置
		 */
		@Override
		public void moveTo(int currentIndex) {
			// TODO Auto-generated method stub
			//上面用的是下标
//			RadioButton button = (RadioButton) radiogroup.getChildAt(currentIndex);
//			button.setChecked(true);
			
			radiogroup.check(currentIndex);//用他的id
			
			
			
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// 释放资源
		super.onDestroy();
	}

}
