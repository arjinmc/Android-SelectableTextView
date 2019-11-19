package com.arjinmc.selectabletextview;

import android.widget.PopupWindow;

/**
 * Abstract SelectablePopupMenu
 * Created by Eminem Lo on 2019-11-19.
 * email: arjinmc@hotmail.com
 */
public abstract class AbstractSelectablePopupMenu extends PopupWindow {

    public void show(int touchX, int touchY) {
        try {
            throw new Exception("You should override this method");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
