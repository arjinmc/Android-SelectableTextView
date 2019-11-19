package com.arjinmc.selectabletextview;

import android.content.Context;
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

    private final int DEFAULT_MARGIN_TOP = 60;
    private int mTopOffset = DEFAULT_MARGIN_TOP;

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
        int locationX = 0, locationY = 0;
        int[] screenLocation = new int[2];
        selectableTextView.getLocationOnScreen(screenLocation);

        //calculate the locationX
        //check if right edge
        if (screenLocation[0] + selectableTextView.getMeasuredWidth() - touchX < mWidth / 2) {
            locationX = screenLocation[0] + selectableTextView.getMeasuredWidth() - mWidth;

            //check if left edge
        } else if (touchX < mWidth / 2) {
            locationX = screenLocation[0];

            //others
        } else {
            //not very good need optimize
            locationX = screenLocation[0] + touchX;

        }

        //calculate the locationY
        //check if need show at down
        if (screenLocation[1] + touchY < mTopOffset + mHeight) {
            locationY = screenLocation[1] + mTopOffset + mHeight + selectableTextView.getPaddingTop();
            //others show at top
        } else {
            locationY = screenLocation[1] + touchY - mTopOffset - mHeight;
        }
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
