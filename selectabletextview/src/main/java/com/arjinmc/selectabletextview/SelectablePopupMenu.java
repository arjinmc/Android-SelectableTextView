package com.arjinmc.selectabletextview;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
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
    private SelectableTextView mSelectableTextView;

    private Context mContext;
    private int mWidth, mHeight;

    public SelectablePopupMenu(Context context) {
        init(context);

    }

    public SelectablePopupMenu(Context context, SelectableTextView selectableTextView) {
        init(context);
        mSelectableTextView = selectableTextView;
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

    public void setSelectableTextView(SelectableTextView selectableTextView) {
        mSelectableTextView = selectableTextView;
    }

    @Override
    public void onClick(View v) {

        if (mOnMenuOptionClickListener == null) {
            return;
        }

        if (v.getId() == R.id.selectabletextview_tv_copy) {
            mOnMenuOptionClickListener.onCopy();
        } else if (v.getId() == R.id.selectabletextview_tv_all) {
            mOnMenuOptionClickListener.onAll();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mSelectableTextView != null) {
            mSelectableTextView.cancelSelected();
        }
    }

    @Override
    public void show(int touchX, int touchY) {
        if (mSelectableTextView == null) {
            return;
        }
        int locationX = 0, locationY = 0;

        //calculate the locationX
        if (mSelectableTextView.getMeasuredWidth() - touchX < mWidth / 2) {
            locationX = mSelectableTextView.getMeasuredWidth() - mWidth;
        } else if (locationX < mWidth / 2) {
            locationX = 0;
        } else {
            locationX = touchX - mWidth / 2;
        }

        //calculate the locationY
        int[] parentScreenLocation = new int[2];
        mSelectableTextView.getLocationOnScreen(parentScreenLocation);

        Log.e("show",locationX+","+locationY);

        showAtLocation(mSelectableTextView, Gravity.NO_GRAVITY, locationX, locationY);
    }

    public void setOnMenuOptionClickListener(OnMenuOptionClickListener onMenuOptionClickListener) {
        mOnMenuOptionClickListener = onMenuOptionClickListener;
    }

    public interface OnMenuOptionClickListener {
        void onCopy();

        void onAll();
    }
}
