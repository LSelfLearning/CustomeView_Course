package com.lewish.start.customeindicator;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PagerIndicator mPagerIndicator;
    private ViewPager mViewPager;
    private List<String> mTitles =  Arrays.asList("银多利","月盈利");
    private ArrayList<ContentFragment> mContentFragments = new ArrayList<>();
    private ContentAdapter mContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerIndicator = (PagerIndicator) findViewById(R.id.pager_indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        for (String mTitle : mTitles) {
            ContentFragment contentFragment = ContentFragment.newInstance(mTitle);
            mContentFragments.add(contentFragment);
        }
        mContentAdapter = new ContentAdapter(this,getSupportFragmentManager(),mContentFragments);
        mViewPager.setAdapter(mContentAdapter);

        mPagerIndicator.setTabVisibleCount(2);
        mPagerIndicator.setTabItemTitles(mTitles);
        mPagerIndicator.setUpWithViewPager(mViewPager,1);
    }
}
