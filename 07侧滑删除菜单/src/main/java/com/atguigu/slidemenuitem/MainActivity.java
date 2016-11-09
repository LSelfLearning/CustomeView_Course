package com.atguigu.slidemenuitem;

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
        for(int i=0 ; i < 100 ;i++){
            myBeans.add(new MyBean("content"+i));
        }

        //设置适配器
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter{

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
                convertView = View.inflate(MainActivity.this,R.layout.item_main,null);
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
            slideLayout.setStateChangeListener(new MyOnStateChangeListener());
            return convertView;
        }
    }


    static class ViewHolder{
        TextView item_content;
        TextView item_menu;
    }

    private SlideLayout openedLayout;

    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {

        @Override
        public void onOpen(SlideLayout layout) {
            openedLayout = layout;//记录，变化在下个item 被Down的时候关闭打开的
        }

        @Override
        public void onClose(SlideLayout layout) {
            if(openedLayout == layout){
                openedLayout = null;//释放
            }

        }

        @Override
        public void onDown(SlideLayout layout) {

            if(openedLayout != null && openedLayout != layout){
                openedLayout.closeMenu();
            }

        }
    }
}
