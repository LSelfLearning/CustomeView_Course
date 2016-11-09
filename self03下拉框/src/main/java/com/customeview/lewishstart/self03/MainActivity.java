package com.customeview.lewishstart.self03;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private EditText et_input;
    private ImageView down_arrow;
    private PopupWindow popupWindow;
    private ListView listView;

    private List<String> msgs = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图控件
        et_input = (EditText)findViewById(R.id.et_input);
        down_arrow = (ImageView)findViewById(R.id.down_arrow);
        //初始化数据
        for(int i = 0; i < 100; i++) {
            msgs.add("---第"+i+"个账号---");
        }

        listView = new ListView(this);
        listView.setBackgroundResource(R.drawable.listview_background);
        listView.setAdapter(new MyAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_input.setText(msgs.get(position));
                if(popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow==null) {
                    popupWindow = new PopupWindow(MainActivity.this);
                    popupWindow.setWidth(et_input.getWidth());
                    popupWindow.setHeight(DensityUtil.dip2px(MainActivity.this,200));
                    popupWindow.setContentView(listView);
                    popupWindow.setFocusable(true);
                }
                popupWindow.showAsDropDown(et_input,0,0);
            }
        });
    }

    /**
     * 活动退出时，popupWindow赋值null
     */
    @Override
    protected void onDestroy() {
        popupWindow=null;
        super.onDestroy();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return msgs.size();
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
            ViewHolder viewHolder;
            if(convertView==null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(MainActivity.this,R.layout.list_item,null);
                viewHolder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_user_name.setText(msgs.get(position));
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgs.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
        class ViewHolder{
            TextView tv_user_name;
            ImageView iv_delete;
        }
    }
}
