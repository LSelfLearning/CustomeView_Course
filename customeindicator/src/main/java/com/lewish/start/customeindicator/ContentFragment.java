package com.lewish.start.customeindicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/21 16:50.
 */

public class ContentFragment extends Fragment {
    public static final String BUNDLE_TITLE = "title";
    private String mStrContent;
    public static ContentFragment newInstance(String title){
        ContentFragment instance = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE,title);
        instance.setArguments(bundle);
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mStrContent = arguments.getString(BUNDLE_TITLE);
        TextView textView = new TextView(getActivity());
        textView.setText(mStrContent);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
