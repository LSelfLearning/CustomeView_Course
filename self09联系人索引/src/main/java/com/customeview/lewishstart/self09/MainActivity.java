package com.customeview.lewishstart.self09;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv_contacts;
    private TextView tv_letter;
    private IndexView iv_words;
    private List<Person> persons = new ArrayList<Person>();
    private MyAdapter myAdapter;

     private Handler handler = new Handler(){
        public void handleMessage(Message msg){
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_contacts = (ListView)findViewById(R.id.lv_contacts);
        tv_letter = (TextView)findViewById(R.id.tv_letter);
        iv_words = (IndexView)findViewById(R.id.iv_words);

        initData();

        myAdapter = new MyAdapter(this);
        lv_contacts.setAdapter(myAdapter);
        iv_words.setOnLetterChangeListener(new IndexView.LetterChangeListener() {
            @Override
            public void changeLetter(String letter) {
                updateLetter(letter);
                updateContacts(letter);
            }
        });
    }

    private void updateContacts(String letter) {
        for(int i = 0; i < persons.size(); i++) {
          if(letter.equals(persons.get(i).getPinyin().substring(0,1))) {
              lv_contacts.setSelection(i);
              return;
          }
        }
    }

    private void updateLetter(String letter) {
        tv_letter.setVisibility(View.VISIBLE);
        handler.removeCallbacksAndMessages(null);
        tv_letter.setText(letter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_letter.setVisibility(View.GONE);
            }
        },2000);
    }

    private void initData() {
        persons.add(new Person("张晓飞"));
        persons.add(new Person("杨光福"));
        persons.add(new Person("胡继群"));
        persons.add(new Person("刘畅"));

        persons.add(new Person("钟泽兴"));
        persons.add(new Person("尹革新"));
        persons.add(new Person("安传鑫"));
        persons.add(new Person("张骞壬"));

        persons.add(new Person("温松"));
        persons.add(new Person("李凤秋"));
        persons.add(new Person("刘甫"));
        persons.add(new Person("娄全超"));
        persons.add(new Person("张猛"));

        persons.add(new Person("王英杰"));
        persons.add(new Person("李振南"));
        persons.add(new Person("孙仁政"));
        persons.add(new Person("唐春雷"));
        persons.add(new Person("牛鹏伟"));
        persons.add(new Person("姜宇航"));

        persons.add(new Person("刘挺"));
        persons.add(new Person("张洪瑞"));
        persons.add(new Person("张建忠"));
        persons.add(new Person("侯亚帅"));
        persons.add(new Person("刘帅"));

        persons.add(new Person("乔竞飞"));
        persons.add(new Person("徐雨健"));
        persons.add(new Person("吴亮"));
        persons.add(new Person("王兆霖"));

        persons.add(new Person("阿三"));

        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });
    }
    class MyAdapter extends BaseAdapter{
        private Context context;
        public MyAdapter(Context context) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return persons.size();
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
            if(convertView==null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context,R.layout.item_layout,null);
                viewHolder.tv_letter = (TextView) convertView.findViewById(R.id.tv_letter);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Person person = persons.get(position);
            final String name = person.getName();
            String curLetter=person.getPinyin().substring(0,1);

            viewHolder.tv_name.setText(name);
            viewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.tv_letter.setText(curLetter);
            if(position==0) {
                viewHolder.tv_letter.setVisibility(View.VISIBLE);
            }else {
                String preLetter = persons.get(position-1).getPinyin().substring(0,1);
                if(preLetter.equals(curLetter)) {
                    viewHolder.tv_letter.setVisibility(View.GONE);
                }else {
                    viewHolder.tv_letter.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }
    }
    static class ViewHolder{
        TextView tv_letter;
        TextView tv_name;
    }
}
