package com.lewish.start.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView listview;

    private List<MyBean> myBeans;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        myBeans = new ArrayList<>();
        for(int i=0 ; i < 50 ;i++){
            myBeans.add(new MyBean("content"+i));
        }

        //设置适配器
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(MainActivity.this,R.layout.item_slidelayout,null);
                viewHolder = new ViewHolder();
                viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
                viewHolder.item_menu = (TextView) convertView.findViewById(R.id.item_menu);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            MyBean myBean = myBeans.get(position);
            viewHolder.item_content.setText(myBean.getName());
            viewHolder.item_content.setTag(position);
            viewHolder.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    SlideLayout slideLayout = (SlideLayout) v.getParent();
                    slideLayout.closeMenu();
                    Toast.makeText(MainActivity.this, "position=" + myBeans.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.item_menu.setTag(position);
            viewHolder.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SlideLayout slideLayout = (SlideLayout) v.getParent();
                    slideLayout.closeMenu();
                    int position = (int) v.getTag();
                    myBeans.remove(position);
                    myAdapter.notifyDataSetChanged();
                }
            });

            //设置这一整条的监听
            SlideLayout slideLayout = (SlideLayout) convertView;
            slideLayout.setOnSlideLayoutStateChangedListener(onSlideLayoutStateChangedListener);
            return convertView;
        }
    }
    static class ViewHolder{
        TextView item_content;
        TextView item_menu;
    }

    private SlideLayout.onSlideLayoutStateChangedListener onSlideLayoutStateChangedListener = new SlideLayout.onSlideLayoutStateChangedListener() {
        @Override
        public void onMenuOpen(SlideLayout slideLayout) {

        }

        @Override
        public void onMenuClose(SlideLayout slideLayout) {

        }
    };
}
