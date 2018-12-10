package com.atguigu.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

/**
 * 作者：杨光福 on 2016/5/16 10:33
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：仿ViewPager
 */
public class MyViewPager extends ViewGroup {

    /**
     * 手势识别器
     * 1.定义出来
     * 2.实例化-把想要的方法给重新
     * 3.在onTouchEvent()把事件传递给手势识别器
     *
     *
     */

    private GestureDetector detector;
    /**
     * 当前页面的下标位置
     */
    private int currentIndex;

    private Scroller scroller;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(final Context context) {
        scroller = new Scroller(context);
        //2.实例化手势识别器
        detector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(context,"长按",Toast.LENGTH_SHORT).show();
            }

            /**
             *
             * @param e1
             * @param e2
             * @param distanceX 在X轴滑动了的距离
             * @param distanceY 在Y轴滑动了的距离
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                /**
                 *x:要在X轴移动的距离
                 *y:要在Y轴移动的距离
                 */
                scrollBy((int)distanceX,0);

                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(context,"双击",Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历孩子，给每个孩子指定在屏幕的坐标位置
        for(int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);

            childView.layout(i*getWidth(),0,(i+1)*getWidth(),getHeight());
        }

    }
    private float startX;

    private float downX;
    private float downY;

    /**
     * 如果当前方法，返回ture,拦截事件，将会触发当前控件的onTouchEvent()方法
     * 如果当前方法,返回false,不拦截事件，事件继续传递给孩子
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        boolean result = false;//默认传递给孩子

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("onInterceptTouchEvent==ACTION_DOWN");
                //1.记录坐标
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("onInterceptTouchEvent==ACTION_MOVE");
                //2.记录结束值
                float endX = ev.getX();
                float endY = ev.getY();

                //3.计算绝对值
                float distanceX = Math.abs(endX - downX);
                float distanceY = Math.abs(endY - downY);

                if(distanceX > distanceY&&  distanceX >5){
                    result = true;
                }else{
                    scrollToPager(currentIndex);
                }

                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onInterceptTouchEvent==ACTION_UP");

                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //3.把事件传递给手势识别器
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.记录坐标
                startX = event.getX();
                System.out.println("onTouchEvent==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("onTouchEvent==ACTION_MOVE");

                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onTouchEvent==ACTION_UP");

                //2.来到新的坐标
                float endX = event.getX();

                //下标位置
                int tempIndex = currentIndex;

                if((startX-endX)>getWidth()/2){

                    //显示下一个页面
                    tempIndex ++;

                }else if((endX-startX)>getWidth()/2){

                    //显示上一个页面
                    tempIndex --;
                }

                //根据下标位置移动到指定的页面
                scrollToPager(tempIndex);

                break;
        }
        return true;
    }

    /**
     * 屏蔽非法值，根据位置移动到指定页面
     * @param tempIndex
     */
    public void scrollToPager(int tempIndex) {

        if(tempIndex < 0){
            tempIndex = 0;
        }

        if(tempIndex > getChildCount()-1){
            tempIndex = getChildCount()-1;
        }
        //当前页面的下标位置
        currentIndex = tempIndex;

        if(mOnPagerChangListenter != null){
//            mOnPagerChangListenter ==MyOnPagerChangListenter;
            //MyOnPagerChangListenter 里面有一个方法，onScrollToPager(int)
            Log.e("yangguangfu", "MyViewPager--scrollToPager____mOnPagerChangListenter=="+mOnPagerChangListenter);
            mOnPagerChangListenter.onScrollToPager(currentIndex);
        }

        //总距离计算出来
        int distanceX = currentIndex*getWidth() - getScrollX();
        // int distanceX = 目标 - getScrollX();
        //移动到指定的位置
//        scrollTo(currentIndex*getWidth(),getScrollY());
//        scroller.startScroll(getScrollX(),getScrollY(),distanceX,0);

        scroller.startScroll(getScrollX(),getScrollY(),distanceX,0,Math.abs(distanceX));

        //
        invalidate();//强制绘制;//onDraw();computeScroll();
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
       if( scroller.computeScrollOffset()){

           //得到移动这个一小段对应的坐标
           float currX = scroller.getCurrX();

           scrollTo((int) currX,0);

           invalidate();
       };
    }



    /**
     * 监听页面的改变
     * @author 阿福
     */
    public interface OnPagerChangListenter {

        /**
         * 当页面改变的时候回调这个方法
         * @author 阿福
         * @param position 当前页面的下标
         */
        void onScrollToPager(int position);
    }

    private OnPagerChangListenter mOnPagerChangListenter;

    /**
     * 设置页面改变的监听
     * @param l
     */
    public void setOnPagerChangListenter( OnPagerChangListenter l) {
        Log.e("yangguangfu", "setOnPagerChangListenter=="+l);
        //当前的 mOnPagerChangListenter==MyOnPagerChangListenter；
        mOnPagerChangListenter = l;
    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * 1.测量的时候测量多次
     * 2.widthMeasureSpec父层视图给当前视图的宽和模式
     * 系统的onMesaure中所干的事：
     1、根据 widthMeasureSpec 求得宽度width，和父view给的模式
     2、根据自身的宽度width 和自身的padding 值，相减，求得子view可以拥有的宽度newWidth
     3、根据 newWidth 和模式求得一个新的MeasureSpec值:
      MeasureSpec.makeMeasureSpec(newSize, newmode);
     4、用新的MeasureSpec来计算子view

     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
//        MeasureSpec.AT_MOST;
//        MeasureSpec.UNSPECIFIED;
//        MeasureSpec.EXACTLY;
        int newsize = MeasureSpec.makeMeasureSpec(size,mode);
        System.out.println("widthMeasureSpec=="+widthMeasureSpec+"size=="+size+",mode=="+mode);
        System.out.println("widthMeasureSpec=="+widthMeasureSpec+"sizeHeight=="+sizeHeight+",modeHeight=="+modeHeight);

        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

}
