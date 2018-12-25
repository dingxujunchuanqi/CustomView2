package com.atguigu.pinterestlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {


	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * onInterceptTouchEvent();拦截触摸事件
	 1.如果返回的是true,将会触发当前View的onTouchEvent();
	 2.如果返回的是false,事件将会传给孩子
	 3.如果你重写dispatchTouchEvent 你返回true 或者fals 事件都不会响应，
	 必须返回 return super.dispatchTouchEvent(ev);
	 4.  onTouchEvent 判断里面的boolen值必须返回true，你返回false 或者super.onTouchEvent(event)
	 都不会执行

     *
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//屏幕的三分之一
		int width=getWidth()/getChildCount();
		int height = getHeight();
		int count=getChildCount();

		float eventX = event.getX();

		if (eventX<width){	// 滑动左边的 listView
			getChildAt(0).dispatchTouchEvent(event);
			return true;

		} else if (eventX > width && eventX < 2 * width) { //滑动中间的 listView  
			float eventY = event.getY();
			if (eventY < height / 2) {
				for (int i = 0; i < count; i++) {
					View child = getChildAt(i);
					try {
						child.dispatchTouchEvent(event);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				return true;
			} else if (eventY > height / 2) {
//				event.setLocation(width / 2, event.getY());
				getChildAt(1).dispatchTouchEvent(event);
				return true;
			}
		}else if (eventX>2*width){
			getChildAt(2).dispatchTouchEvent(event);
			return true;
		}

		return true;
	}

}
