package com.arjinmc.selectabletextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

/**
 * Selectable TextView
 * Created by Eminem Lo on 2019-11-18.
 * email: arjinmc@hotmail.com
 */
public class SelectableTextView extends TextView implements View.OnLongClickListener {

    /**
     * default selected length
     */
    private final static int DEFAULT_SELECTION_LEN = 5;

    /**
     * the index for selected text start and end
     */
    private int mSelectedStartIndex, mSelectedEndIndex;
    /**
     * touch coordinate
     */
    private int mTouchX, mTouchY;

    /**
     * selected text background color
     */
    private int mSelectedBackgroundColor = Color.RED;
    private SelectableCursorView mCursorView;
    /**
     * popup menu
     */
    private AbstractSelectablePopupMenu mSelectablePopupMenu;
    private PopupWindow.OnDismissListener mSelectablePopupMenuOnDismissListener;

    private OnSelectedListener mOnSelectedListener;

    public SelectableTextView(Context context) {
        super(context);
        init();
    }

    public SelectableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        setOnLongClickListener(this);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTouchX = (int) event.getX() - getPaddingLeft() - getPaddingRight();
                mTouchY = (int) event.getY() - getPaddingTop();
                return false;
            }
        });
    }

    public void setSelectablePopupMenu(SelectablePopupMenu selectablePopupMenu) {
        mSelectablePopupMenu = selectablePopupMenu;
        initSelectablePopupMenuListener();
    }

    public void initSelectablePopupMenuListener() {
        if (mSelectablePopupMenu == null) {
            return;
        }
        if (mSelectablePopupMenuOnDismissListener == null) {
            mSelectablePopupMenuOnDismissListener = new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    cancelSelected();
                }
            };
        }
        mSelectablePopupMenu.setOnDismissListener(mSelectablePopupMenuOnDismissListener);
    }

    public void setSelectedBackgroundColor(@ColorRes int colorRes) {
        mSelectedBackgroundColor = ContextCompat.getColor(getContext(), colorRes);
    }

    /**
     * set selected text range
     *
     * @param startIndex
     * @param endIndex
     */
    public void setSelectedRange(int startIndex, int endIndex) {

        if (isEmpty()) {
            return;
        }
        if (startIndex == endIndex) {
            return;
        }
        if (startIndex > endIndex) {
            mSelectedStartIndex = endIndex;
            mSelectedEndIndex = startIndex;
        } else {
            mSelectedStartIndex = startIndex;
            mSelectedEndIndex = endIndex;
        }

    }

    /**
     * set selected text background color shown
     *
     * @param shown
     */
    private void setSelectedBackgroundShown(boolean shown) {

        if (isEmpty()) {
            return;
        }

        if (shown) {
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(mSelectedBackgroundColor);
            String content = getText().toString();
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(backgroundColorSpan, mSelectedStartIndex, mSelectedEndIndex
                    , SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
            setText(spannableString);
        } else {
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.TRANSPARENT);
            String content = getText().toString();
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(backgroundColorSpan, mSelectedStartIndex, mSelectedEndIndex
                    , SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
            setText(spannableString);
        }
    }

    /**
     * check if is empty content
     *
     * @return
     */
    public boolean isEmpty() {
        String content = getText().toString();
        return TextUtils.isEmpty(content);
    }

    /**
     * cancel selected state
     */
    public void cancelSelected() {
        mSelectedStartIndex = 0;
        mSelectedEndIndex = 0;
        if (isEmpty()) {
            return;
        }
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.TRANSPARENT);
        String content = getText().toString();
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(backgroundColorSpan, 0, content.length()
                , SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        setText(spannableString);
    }

    @Override
    public boolean onLongClick(View v) {

        calculateIndex(mTouchX, mTouchY);
        setSelectedBackgroundShown(true);
        dispatchSelected();
        return false;
    }

    /**
     * dispatch selected event
     */
    public void dispatchSelected() {
        if (mOnSelectedListener != null) {
            mOnSelectedListener.onSelectedChange(getSelectedText(), mSelectedStartIndex, mSelectedEndIndex);
        }
        if (mSelectablePopupMenu != null) {
            mSelectablePopupMenu.show(SelectableTextView.this, mTouchX, mTouchY);
        }
    }

    /**
     * Gets the character offset of (x, y). If (x, y) lies on the right half of the character, it
     * returns the offset of the next character. If (x, y) lies on the left half of the character, it
     * returns the offset of this character.
     *
     * @param x x coordinate relative to this TextView
     * @param y y coordinate relative to this TextView
     * @return the offset at (x,y), -1 if error occurs
     */
    public int getOffset(int x, int y) {
        Layout layout = getLayout();
        int offset = -1;

        if (layout != null) {
            int topVisibleLine = layout.getLineForVertical(y);
            offset = layout.getOffsetForHorizontal(topVisibleLine, x);
        }

        return offset;
    }

    /**
     * Gets the character offset where (x, y) is pointing to.
     *
     * @param x x coordinate relative to this TextView
     * @param y y coordinate relative to this TextView
     * @return the offset at (x, y), -1 if error occurs
     * @see {@link #getOffset(int, int)}
     */
    public int getPreciseOffset(int x, int y) {
        Layout layout = getLayout();

        if (layout != null) {
            int topVisibleLine = layout.getLineForVertical(y);
            int offset = layout.getOffsetForHorizontal(topVisibleLine, x);

            int offset_x = (int) layout.getPrimaryHorizontal(offset);
            if (offset_x > x) {
                return layout.getOffsetToLeftOf(offset);
            }
        }
        return getOffset(x, y);
    }

    /**
     * calculate selected index of text string
     *
     * @param x
     * @param y
     */
    private void calculateIndex(int x, int y) {

        int start = getPreciseOffset(x, y);

        if (start > -1) {
            int end = start + DEFAULT_SELECTION_LEN;
            if (end >= getText().length()) {
                end = getText().length() - 1;
            }
            mSelectedStartIndex = start;
            mSelectedEndIndex = end;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * get selected text
     *
     * @return
     */
    public String getSelectedText() {
        String text = getText().toString();

        if (TextUtils.isEmpty(text)) {
            return null;
        }
        if (mSelectedStartIndex == mSelectedEndIndex) {
            return null;
        }
        if (mSelectedStartIndex > mSelectedEndIndex) {
            int temp = mSelectedStartIndex;
            mSelectedStartIndex = mSelectedEndIndex;
            mSelectedEndIndex = temp;
        }
        if (mSelectedStartIndex < 0) {
            mSelectedStartIndex = 0;
        }
        int textLength = text.length();
        if (mSelectedEndIndex > textLength) {
            mSelectedEndIndex = textLength;
        }
        return text.substring(mSelectedStartIndex, mSelectedEndIndex);

    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        mOnSelectedListener = onSelectedListener;
    }

    public interface OnSelectedListener {
        void onSelectedChange(String selectedText, int startIndex, int endIndex);
    }

}
