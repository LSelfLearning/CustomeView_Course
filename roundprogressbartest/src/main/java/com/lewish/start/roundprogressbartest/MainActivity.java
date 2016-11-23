package com.lewish.start.roundprogressbartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private RoundProgressBar roundProgressBar;
    private MyClockView my_clockview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roundProgressBar = (RoundProgressBar)findViewById(R.id.round_progress_bar);
        my_clockview = (MyClockView)findViewById(R.id.my_clockview);
        roundProgressBar.dynamicDraw(100);
        my_clockview.dynamicDraw(50,3000);
        my_clockview.updateTime(28806000);
    }
}
