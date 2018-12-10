package com.example.cusviewpager;

import android.R.integer;
import android.os.SystemClock;

/*
 * project:CusViewPager
 * package:com.example.cusviewpager
 * author:曹智民
 * data:2015年6月6日 上午11:36:45
 */
public class MyScroll {
	//开始的xy坐标
	private float startx;
	private float starty;
	//在xy轴要移动距离
	private float distanceX;
	private float distanceY;
	private long starttime;
	private boolean isFinish;
	//总共花费最大时间
	private long totalTime=1000;
	private float currX=startx;
	public void startScroll(float startx,float starty,float distanceX,float distancey){
		this.startx=startx;
		this.starty=starty;
		//需要移动的总距离
		this.distanceX=distanceX;
		this.distanceY=distancey;
		this.starttime=SystemClock.uptimeMillis();
		this.isFinish=false;
	}
	//计算这段花费时间 距离 平均速度,返回true代表还在计算false代表结束
	public boolean computScrollOffset(){
		if(isFinish){
			return false;
		}
		long endTime=SystemClock.uptimeMillis();
		long passTime=endTime-starttime;
		if(passTime<totalTime){
			//正在移动
			//1.计算平均速度必须是float类型
			float vx=distanceX/totalTime;
			//2.计算这一小段的距离
			float distanceSmallX=vx*passTime;
			//3.计算当前坐标
			currX = startx+distanceSmallX;
		}else {
			currX=startx+distanceX;
			isFinish=true;
			return false;
		}
		return true;
	}
	public float getCurrX() {
		return currX;
	}
	public void setCurrX(float currX) {
		this.currX = currX;
	}
}
