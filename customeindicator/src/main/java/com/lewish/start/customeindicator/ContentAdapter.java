package com.lewish.start.customeindicator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/21 16:58.
 */

public class ContentAdapter extends FragmentPagerAdapter {
    private ArrayList<ContentFragment> mContentFragments;
    private Context mContext;

    public ContentAdapter(Context mContext,FragmentManager fm,  ArrayList<ContentFragment> mContentFragments) {
        super(fm);
        this.mContext = mContext;
        this.mContentFragments = mContentFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mContentFragments.get(position);
    }

    @Override
    public int getCount() {
        return mContentFragments.size();
    }
}
