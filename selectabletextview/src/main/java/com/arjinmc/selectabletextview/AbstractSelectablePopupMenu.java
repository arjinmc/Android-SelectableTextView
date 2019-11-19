package com.arjinmc.selectabletextview;

import android.widget.PopupWindow;

/**
 * Abstract SelectablePopupMenu
 * Created by Eminem Lo on 2019-11-19.
 * email: arjinmc@hotmail.com
 */
public abstract class AbstractSelectablePopupMenu extends PopupWindow {

    /**
     * show at the right position for popup menu
     *
     * @param selectableTextView
     * @param touchX
     * @param touchY
     */
    abstract void show(SelectableTextView selectableTextView, int touchX, int touchY);
}
