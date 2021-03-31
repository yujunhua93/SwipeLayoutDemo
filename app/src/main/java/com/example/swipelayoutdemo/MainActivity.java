package com.example.swipelayoutdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.swipelayout.SwipeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SwipeLayout swipeLayout = findViewById(R.id.swipe);
        final TextView textView = findViewById(R.id.delete_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                swipeLayout.closeMenu();
            }
        });
//        ValueAnimator valueAnimator = ValueAnimator.ofInt(500);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
////                swipeLayout.setTranslationX(valueAnimator.getAnimatedValue());
//                swipeLayout.scrollTo((Integer)valueAnimator.getAnimatedValue(),0);
//
//            }
//        });
//        valueAnimator.setDuration(2000);
//        valueAnimator.start();
//
    }
}