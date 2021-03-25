package com.example.swipelayoutdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.swipelayout.SwipeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SwipeLayout swipeLayout = findViewById(R.id.swipe);
//        swipeLayout.setTranslationX(-500);
    }
}