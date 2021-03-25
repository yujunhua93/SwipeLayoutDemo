package com.example.swipelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SwipeLayout extends LinearLayout {
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;

    private float percent = 0.4f; //阈值,控制是否回弹还是展开

    public SwipeLayout(Context context) {
        this(context,null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);
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

            int childWidth = view.getMeasuredWidth();
            int childheight = view.getMeasuredHeight();


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
                int childWidth = view.getMeasuredWidth();
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


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mDownX = mLastX = (int)ev.getX();
                mDownY = (int)ev.getY();
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                int disX = (int)(ev.getX() - mDownX);
                int disY = (int)(ev.getY() - mDownY);
                return false;
            }
            case MotionEvent.ACTION_UP: {
                    return false;
            }
            case MotionEvent.ACTION_CANCEL: {
                return false;
            }
        }
        return isIntercepted;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int dx;
        int dy;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = (int)event.getX();
                mLastY = (int)event.getY();
                Log.e("ACTION_DOWN","ACTION_DOWN");
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int disX = (int)(mLastX - event.getX());
                int disY = (int)(mLastY - event.getY());
                scrollBy(disX, 0);
                Log.e("move","move");
                break;
            }
            case MotionEvent.ACTION_UP: {
                dx = (int)(mDownX - event.getX());
                dy = (int)(mDownY - event.getY());
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
