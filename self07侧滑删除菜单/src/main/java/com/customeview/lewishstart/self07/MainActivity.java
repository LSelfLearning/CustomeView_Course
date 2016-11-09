package com.customeview.lewishstart.self07;

import android.app.Activity;
import android.content.Context;
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
    private ListView lv_main;
    private MyAdapter myAdapter;
    private List<MyBean> dataList;
    private SlideLayout openedSlideLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_main = (ListView)findViewById(R.id.lv_main);
        //数据初始化
        dataList = new ArrayList<MyBean>();
        for(int i = 0; i < 30; i++) {
          dataList.add(new MyBean("Content"+i));
        }
        myAdapter = new MyAdapter(this);
        lv_main.setAdapter(myAdapter);
    }
    class MyAdapter extends BaseAdapter{
        private Context context;
        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return dataList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHoler;
            if(convertView==null) {
                viewHoler = new ViewHolder();
                convertView = View.inflate(context,R.layout.item_main,null);
                viewHoler.item_content = (TextView) convertView.findViewById(R.id.item_content);
                viewHoler.item_menu = (TextView) convertView.findViewById(R.id.item_menu);
                convertView.setTag(viewHoler);
            }else {
                viewHoler = (ViewHolder) convertView.getTag();
            }
            final SlideLayout slideLayout = (SlideLayout) convertView;
            MyBean myBean = dataList.get(position);

            viewHoler.item_content.setText(myBean.getName());
            viewHoler.item_content.setTag(position);
            /**
             * 给TextView设置点击事件
             */
            viewHoler.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Toast.makeText(MainActivity.this, dataList.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            /**
             * 给菜单添加点击事件
             */
            viewHoler.item_menu.setTag(position);
            viewHoler.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    slideLayout.closeMenu();
                    dataList.remove(position);
                    notifyDataSetChanged();
                }
            });
            /**
             * 给SlideLayout设置状态变化监听
             * （滑动时关闭窗口）
             */
            slideLayout.setOnSildeStateChangeListener(new SlideLayout.SildeStateChangeListener() {
                @Override
                public void onOpenMenu(SlideLayout slideLayout) {
                    openedSlideLayout = slideLayout;
                }

                @Override
                public void onCloseMenu(SlideLayout slideLayout) {
                    if(openedSlideLayout==slideLayout) {
                        openedSlideLayout=null;
                    }
                }

                @Override
                public void onClick(SlideLayout slideLayout) {
                    if(openedSlideLayout!=null&&openedSlideLayout!=slideLayout) {
                        openedSlideLayout.closeMenu();
                    }
                }
            });
            return convertView;
        }
    }
    static class ViewHolder{
        TextView item_content;
        TextView item_menu;
    }
}
