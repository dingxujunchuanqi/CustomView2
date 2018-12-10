package com.atguigu.myviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    private MyViewPager myviewpager;
    private RadioGroup rg_main;
    private int[] ids = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myviewpager = (MyViewPager) findViewById(R.id.myviewpager);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        //添加6页面
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            //添加到MyViewPager这个View中
            myviewpager.addView(imageView);
        }

        //添加测试页面
        View testview = View.inflate(this,R.layout.test,null);
        myviewpager.addView(testview,2);


        for(int i=0;i<myviewpager.getChildCount();i++){
            RadioButton button = new RadioButton(this);
            button.setId(i);//0~5的id

            if(i==0){
                button.setChecked(true);
            }

            //添加到RadioGroup
            rg_main.addView(button);
        }




        //设置RadioGroup选中状态的变化
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /**
             *
             * @param group
             * @param checkedId : 0~5之间
             */
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                myviewpager.scrollToPager(checkedId);//根据下标位置定位到具体的某个页面
            }
        });


        //设置监听页面的改变
        MyOnPagerChangListenter myOnPagerChangListenter = new MyOnPagerChangListenter();
        myviewpager.setOnPagerChangListenter(myOnPagerChangListenter);

    }

    class MyOnPagerChangListenter implements MyViewPager.OnPagerChangListenter {

        @Override
        public void onScrollToPager(int position) {
            rg_main.check(position);
        }
    }


}
