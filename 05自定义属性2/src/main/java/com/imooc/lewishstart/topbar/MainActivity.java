package com.imooc.lewishstart.topbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TopBar topbar_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topbar_main = (TopBar)findViewById(R.id.topbar_main);

        topbar_main.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void leftButtonClick() {
                Toast.makeText(MainActivity.this, "上一个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightButtonClick() {
                Toast.makeText(MainActivity.this, "下一个", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
