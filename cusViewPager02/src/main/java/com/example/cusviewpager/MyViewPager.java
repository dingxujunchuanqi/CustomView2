package com.example.cusviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Scroller;

/*
 * project:CusViewPager
 * package:com.example.cusviewpager
 * data:2015年6月6日 上午10:22:35
 */
public class MyViewPager extends ViewGroup {
	private int height;
	private int widht;
	private int currentIndex = 0;
	private Scroller myScroll;
	/**
	 * 计算down和move的时候移动的距离
	 */
	float startx1;
	/**
	 * 计算down和up的时候移动的距离
	 */
	float startx2;
	float starty;
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		myScroll = new Scroller(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		height = getMeasuredHeight();
		widht = getMeasuredWidth();
		//使子View显示
		for(int i=0;i<getChildCount();i++){
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			this.getChildAt(i).layout(i * widht, 0, i * widht + widht, height);
		}
	}
	public void moveTo(int temp) {
		if (temp < 0) {
			temp = 0;
		} else if (temp > getChildCount() - 1) {
			temp = getChildCount() - 1;
		}
		currentIndex = temp;
		if(onPageChangeListener!=null){
			onPageChangeListener.onPageSelect(currentIndex);
		}
		//缓慢移动
		int distanceX = currentIndex * getWidth()- getScrollX();
		//给MyScroll 计算的类赋初始值
		myScroll.startScroll(getScrollX(), 0, distanceX, 0,Math.abs(distanceX));
		invalidate();
	}
	@Override
	public void computeScroll() {
		//如果为true说明移动还没结束
		if (myScroll.computeScrollOffset()) {
			//得到计算的位置，然后移动
			float currX = myScroll.getCurrX();
			scrollTo((int) currX, 0);
			invalidate();
		}
	}
	private onPageChangeListener onPageChangeListener;
	public void setOnPageChangeListener(onPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
	}
	public interface onPageChangeListener{
		public void onPageSelect(int position);
	}
	/*
	 * 如果子View没有冲突，执行intercept的down然后Touch的down move up
	 * 如果子View有冲突，执行intercept的down move如果父View处理就ToucheEvent的move和up
	 * 综上：在down放在intercept中判断冲突的move放在intercept中其它的move和up放在TouchEvent中，这样dector的计算距离就会有错误所以手动计算移动距离
	 */
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean result=false;//这个一定要放在里面，不然move的时候result为tre下次再down返回true ListView就没有机会执行了
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("onInterceptTouchEvent ACTION_DOWN result="+result);
			startx2 = x;
			startx1 = x;
			starty=y;
			break;
		case MotionEvent.ACTION_MOVE://不用startx1，为了防止污染onTouchEvent的startx1
			float disx=x-startx2;
			float disy=y-starty;
			result=Math.abs(disx)>Math.abs(disy);
			startx2=x;
			starty=y;
			System.out.println("onInterceptTouchEvent ACTION_MOVE result="+result);
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("onInterceptTouchEvent ACTION_UP result="+result);
			break;
		default:
			break;
		}
		return result;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("onTouchEvent ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			System.out.println("onTouchEvent ACTION_MOVE");
			float disx = x - startx1;
			// scrollBy总是和移动的相反
			MyViewPager.this.scrollBy((int) -disx, 0);
			startx1 = x;
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("onTouchEvent ACTION_UP");
			if (x - startx2 > getWidth() / 2) {// 移动到上一个
				moveTo(currentIndex - 1);
			} else if (x - startx2 < -getWidth() / 2) {// 移动到下一个
				moveTo(currentIndex + 1);
			} else {// 移动到当前页面
				moveTo(currentIndex);
			}
			break;
		default:
			break;
		}
		return true;
	}
}
