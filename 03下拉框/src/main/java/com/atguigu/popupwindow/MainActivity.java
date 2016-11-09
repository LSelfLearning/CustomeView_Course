package com.atguigu.popupwindow;

import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText et_input;
    private ImageView iv_down_arrow;

    /**
     *
     */
    private PopupWindow popupWindow;
    private ListView listview;
    private MyAdapter myAdapter;

    private ArrayList<String> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_down_arrow = (ImageView) findViewById(R.id.iv_down_arrow);
        et_input = (EditText) findViewById(R.id.et_input);

        et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( popupWindow == null){
                    popupWindow = new PopupWindow(MainActivity.this);
                    popupWindow.setWidth(et_input.getWidth());
                    int height = DensityUtil.dip2px(MainActivity.this,200);//dp->px
                    Toast.makeText(MainActivity.this,"height=="+height,Toast.LENGTH_SHORT).show();
                    popupWindow.setHeight(height);//px

                    popupWindow.setContentView(listview);
                    popupWindow.setFocusable(true);//设置焦点
                }

                popupWindow.showAsDropDown(et_input,0,0);
            }
        });

        listview = new ListView(this);
        listview.setBackgroundResource(R.drawable.listview_background);
        //准备数据
        msgs = new ArrayList<>();
        for(int i= 0;i <100;i++){
            msgs.add(i+"--aaaaaaaaaaaaaa--"+i);
        }
        myAdapter= new MyAdapter();
        listview.setAdapter(myAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.得到数据
                String msg = msgs.get(position);
                //2.设置输入框
                et_input.setText(msg);

                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView  == null){
                convertView = View.inflate(MainActivity.this,R.layout.item_main,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到数据
            final String msg = msgs.get(position);
            viewHolder.tv_msg.setText(msg);

            //设置删除
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.从集合中删除
                    msgs.remove(msg);
                    //2.刷新ui-适配器刷新
                    myAdapter.notifyDataSetChanged();//getCount()-->getView();

                }
            });
            return convertView;
        }
    }

    static class ViewHolder{
        TextView tv_msg;
        ImageView iv_delete;
    }
}
