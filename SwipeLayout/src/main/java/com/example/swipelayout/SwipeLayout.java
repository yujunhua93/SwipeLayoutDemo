package com.example.swipelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

public class SwipeLayout extends LinearLayout {
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;
    private Context mContext;

    float downX = 0;
    float downY = 0;

    private VelocityTracker mVelocityTracker;
    private View mContentView;
    private float mMenuViewWidth;
    private Scroller mScroller;

    int meunWidth = 0; // 菜单的宽度

    private int[] location = new int[2] ;
    private int locationX = location[0];

    private float percent = 0.4f; //阈值,控制是否回弹还是展开

    public SwipeLayout(Context context) {
        this(context,null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        setClickable(true);
        setOrientation(HORIZONTAL);
//        getLocationOnScreen(location);
    }

    private void init() {
        mScroller = new Scroller(mContext);
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        meunWidth = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            int childWidth ;
            childWidth = view.getMeasuredWidth();
            int childheight = view.getMeasuredHeight();

            if (i > 0){
                meunWidth = meunWidth + view.getMeasuredWidth();
            }else {

                mContentView = view;
            }
            width = width + childWidth;
            Log.e("onMeasure_width",":"+width);
        }
        setMeasuredDimension(width,heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int firstwidth = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (i == 0){
                int childWidth =  view.getMeasuredWidth();
                int childheight = view.getMeasuredHeight();
                firstwidth = childWidth;
                view.layout(0,0,childWidth,childheight);
            }else {
                int childWidth = view.getMeasuredWidth();
                int childheight = view.getMeasuredHeight();
                view.layout(firstwidth + childWidth*(i-1),0,firstwidth + childWidth*i ,childheight);
                Log.e("childWidth",":"+childWidth);
                Log.e("width",":"+firstwidth);
            }
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                return true;
////                Log.e("ACTION_DOWN","ACTION_DOWN");
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()){
             case MotionEvent.ACTION_DOWN:
                 downX =  event.getRawX();
                 downY =  event.getRawY();
                 Log.e("ACTION_DOWN:","x:"+downX+"y:"+downY);
                 break;
             case MotionEvent.ACTION_MOVE:
                 float moveX =  event.getRawX();
                 float moveY =  event.getRawY();

                 if (canScroll(moveX,downX)){
                     float moveX1 =  event.getRawX();
//                     if (Math.abs(moveX1 - downX) > 10){
                         scrollTo((int)-(moveX1 - downX),0);
                         mContentView.getLocationOnScreen(location);
                         locationX = location[0];
//                         Log.e("leftpostion", locationX+"");
//                     }else {
//
//                     }
                 }else {
                     break;
                 }

                 break;
             case MotionEvent.ACTION_UP:
                 float upX = event.getRawX();
                    if (shouldOpen()){
                        mScroller.startScroll(getScrollX(), 0, Math.abs(meunWidth -  (int)Math.abs(upX - downX)), 0,1000);
                    }else {
                        mScroller.startScroll(getScrollX(), 0, -(int)Math.abs(upX - downX), 0,1000);
                    }
//                 if (Math.abs(upX - downX) > 100 ){
//                     Toast.makeText(mContext, "大于100了执行动画", Toast.LENGTH_SHORT).show();
//                     mScroller.startScroll(0, 0, -(int)Math.abs(upX - downX), 0,1000);
                     invalidate();
////                     scrollBy(-(int) Math.abs(upX - downX),0);
//                 }
//                 int upX = (int) event.getRawX();
//                 int upY = (int) event.getRawY();
//                 scrollBy( upX - downX ,0);
             case MotionEvent.ACTION_CANCEL:
                 break;
         }

        return super.onTouchEvent(event);
    }



    public void closeMenu(){
        mScroller.startScroll(getScrollX(), 0, -meunWidth, 0,1000);
    }

    private void isExpand(){

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }


    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }

    /**
     * 判断是否超过了menu的最大宽度不在滑动
     * @param moveX
     * @param downX
     */
    private boolean canScroll(float moveX, float downX){
        if (moveX < downX){ //表示向左滑动
            return Math.abs(moveX - downX) <= (getMeasuredWidth() - mContentView.getMeasuredWidth());
        }else {
            if (locationX > 0){ //如果右滑动超过左起点
                return false;
            }
        }
        return false;
//        getScrollX();
//        if (moveX < downX){ //表示向左滑动
//            return Math.abs(moveX - downX) <= (getMeasuredWidth() - mContentView.getMeasuredWidth());
//        }else {
//            return false;
//        }
//        return true;
    }

    /**
     * 判断滑动边（我以menu菜单的宽度的 percent 比例）
     */
    private boolean shouldOpen(){
        if (getScrollX() < 0)
        Log.e("currentXX:",Math.abs(getScrollX()) +" & " + meunWidth * percent );
        return ( Math.abs(getScrollX()) > meunWidth * percent);
    }

}
