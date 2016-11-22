package com.lewish.start.roundprogressbartest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private RoundProgressBar roundProgressBar;
    private ClockVIew clock_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roundProgressBar = (RoundProgressBar)findViewById(R.id.round_progress_bar);
        clock_view = (ClockVIew)findViewById(R.id.clock_view);
        roundProgressBar.dynamicDraw(100);
        clock_view.dynamicDraw(15);
    }
}
