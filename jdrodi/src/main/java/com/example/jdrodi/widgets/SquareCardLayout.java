package com.example.jdrodi.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.cardview.widget.CardView;


public class SquareCardLayout extends CardView {

    public SquareCardLayout(Context context) {
        super(context);
    }

    public SquareCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}