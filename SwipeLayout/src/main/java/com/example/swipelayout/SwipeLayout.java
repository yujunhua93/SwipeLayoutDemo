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
    private Scroller mScroller;

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

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            int childWidth ;
            childWidth = view.getMeasuredWidth();
            int childheight = view.getMeasuredHeight();

            if (i > 0){

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

                 if (Math.abs(moveX - downX) >= (getMeasuredWidth() - mContentView.getMeasuredWidth())) {
                     Toast.makeText(mContext, "超过了最大宽度", Toast.LENGTH_SHORT).show();
                     break;
                 }//超过了控件最大宽度
                 Log.e("ACTION_MOVE:","x:"+moveX+"y:"+moveY);
                 float moveX1 =  event.getRawX();
                 float moveY1 =  event.getRawY();
                 if (Math.abs(moveX1 - downX) > 10){
                     scrollTo((int) Math.abs(moveX1 - downX),0);
                 }else {

                 }
                 break;
             case MotionEvent.ACTION_UP:
                 float upX = event.getRawX();
                 if (Math.abs(upX - downX) > 100 ){
                     Toast.makeText(mContext, "大于100了执行动画", Toast.LENGTH_SHORT).show();
                     mScroller.startScroll(0, 0, -(int)Math.abs(upX - downX), 0,1000);
                     invalidate();
//                     scrollBy(-(int) Math.abs(upX - downX),0);
                 }
//                 int upX = (int) event.getRawX();
//                 int upY = (int) event.getRawY();
//                 scrollBy( upX - downX ,0);
             case MotionEvent.ACTION_CANCEL:
                 break;
         }

        return super.onTouchEvent(event);
    }



    private void closeMenu(){

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
}
