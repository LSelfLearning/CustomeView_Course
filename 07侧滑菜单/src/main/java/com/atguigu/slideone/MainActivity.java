package com.atguigu.slideone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MySlideView msv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		msv = (MySlideView) findViewById(R.id.msv);
		
		
		findViewById(R.id.main_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				msv.changeState();
				
			}
		});
	}
	
	public void onClick(View v){
		
		TextView tv = (TextView) v;
		Toast.makeText(this,tv.getText(), 0).show();
	}


}
