package com.arjinmc.selectabletextview;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Default selectable menu
 * You should define your own selectable menu
 * Created by Eminem Lo on 2019-11-18.
 * email: arjinmc@hotmail.com
 */
public class SelectablePopupMenu extends AbstractSelectablePopupMenu implements View.OnClickListener {

    private final int DEFAULT_MARGIN_TOP = 20;

    private TextView mTvCopy, mTvAll;
    private OnMenuOptionClickListener mOnMenuOptionClickListener;


    private Context mContext;
    private int mWidth, mHeight;
    private boolean isCanceled = true;

    public SelectablePopupMenu(Context context) {
        init(context);

    }

    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.selectabletextview_selectable_popup_menu, null);
        mTvCopy = view.findViewById(R.id.selectabletextview_tv_copy);
        mTvAll = view.findViewById(R.id.selectabletextview_tv_all);
        mTvCopy.setOnClickListener(this);
        mTvAll.setOnClickListener(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(view);
        view.measure(0, 0);
        setOutsideTouchable(true);
        mWidth = view.getMeasuredWidth();
        mHeight = view.getMeasuredHeight();
        setWidth(mWidth);
        setHeight(mHeight);
    }

    @Override
    public void onClick(View v) {

        if (mOnMenuOptionClickListener == null) {
            dismiss();
            return;
        }

        if (v.getId() == R.id.selectabletextview_tv_copy) {
            isCanceled = false;
            mOnMenuOptionClickListener.onCopy();
        } else if (v.getId() == R.id.selectabletextview_tv_all) {
            isCanceled = false;
            mOnMenuOptionClickListener.onSelectAll();
        }
        dismiss();
    }

    @Override
    public void show(SelectableTextView selectableTextView, int touchX, int touchY) {
        if (selectableTextView == null) {
            return;
        }
        Log.e("touch", touchX + "," + touchY);
        Log.e("self", mWidth + "," + mHeight);
        int locationX = 0, locationY = 0;

        //calculate the locationX
        if (selectableTextView.getMeasuredWidth() - touchX < mWidth / 2) {
            locationX = selectableTextView.getMeasuredWidth() - mWidth;
        } else if (touchX < mWidth / 2) {
            locationX = 0;
        } else {
            //not very good need optimize
            locationX = touchX;
        }

        //calculate the locationY
        int[] parentScreenLocation = new int[2];
        selectableTextView.getLocationOnScreen(parentScreenLocation);

        Log.e("show", locationX + "," + locationY);

        showAtLocation(selectableTextView, Gravity.NO_GRAVITY, locationX, locationY);
    }

    public void setOnMenuOptionClickListener(OnMenuOptionClickListener onMenuOptionClickListener) {
        mOnMenuOptionClickListener = onMenuOptionClickListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (isCanceled && mOnMenuOptionClickListener != null) {
            mOnMenuOptionClickListener.onCancel();
        }
        isCanceled = true;
    }

    public interface OnMenuOptionClickListener {
        void onCopy();

        void onSelectAll();

        void onCancel();
    }
}
