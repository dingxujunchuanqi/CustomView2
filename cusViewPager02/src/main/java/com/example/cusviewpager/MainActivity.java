package com.example.cusviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.cusviewpager.MyViewPager.onPageChangeListener;

public class MainActivity extends Activity {
	private MyViewPager vp_viewpager;
	private int ids[] = { R.drawable.a1, R.drawable.a2, R.drawable.a3,
			R.drawable.a4, R.drawable.a5, R.drawable.a6 };
	private RadioGroup rg_group;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		vp_viewpager=(MyViewPager) findViewById(R.id.vp_viewpager);
		rg_group=(RadioGroup) findViewById(R.id.rg_group);
		for (int i = 0; i < ids.length; i++) {
			ImageView iv=new ImageView(this);
			iv.setBackgroundResource(ids[i]);
			vp_viewpager.addView(iv);
		}
		//添加测试页面
				View testView = View.inflate(this, R.layout.test, null);
				vp_viewpager.addView(testView,2);
		//添加单选按钮
		for (int i = 0; i <vp_viewpager.getChildCount(); i++) {
			RadioButton rb=new RadioButton(this);
			rb.setId(i);
			rg_group.addView(rb);
			if(i==0){
				rb.setChecked(true);
			}
		}
		//点击radioButton跳到对应的页面
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				vp_viewpager.moveTo(checkedId);
			}
		});
		//滑动页面RadioButton也跟着移动
		vp_viewpager.setOnPageChangeListener(new onPageChangeListener() {
			@Override
			public void onPageSelect(int position) {
				rg_group.check(position);
			}
		});
	}
}
