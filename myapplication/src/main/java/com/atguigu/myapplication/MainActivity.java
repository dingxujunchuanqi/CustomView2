package com.atguigu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_laucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_laucher = (ImageView) findViewById(R.id.iv_laucher);
        iv_laucher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        System.out.println("ACTION_MOVE");
                        break;

                    case MotionEvent.ACTION_UP:
                        System.out.println("ACTION_UP");
                        break;

                }


                return true;
            }
        });
    }
}
