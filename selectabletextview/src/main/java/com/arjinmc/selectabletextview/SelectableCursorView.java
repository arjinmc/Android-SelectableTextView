package com.arjinmc.selectabletextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

/**
 * SelectableCursorView
 * Created by Eminem Lo on 2019-11-18.
 * email: arjinmc@hotmail.com
 */
class SelectableCursorView extends View {

    private Drawable mDrawable;
    private int mWidth, mHeight;

    public SelectableCursorView(Context context) {
        super(context);
        init();
    }

    public SelectableCursorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectableCursorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectableCursorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mDrawable = ContextCompat.getDrawable(getContext(), R.drawable.selectabletextview_ic_cursor);
        measureSize();
    }

    public void setDrawableId(@DrawableRes int drawableRes) {
        setDrawable(ContextCompat.getDrawable(getContext(), drawableRes));
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
        measureSize();
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(mWidth, mHeight);
    }

    private void measureSize() {
        if (mDrawable == null) {
            return;
        }
        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.setBounds(0, 0, mWidth, mHeight);
        mDrawable.draw(canvas);
    }
}
