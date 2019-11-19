package com.arjinmc.selectabletextviewdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import com.arjinmc.selectabletextview.SelectablePopupMenu;
import com.arjinmc.selectabletextview.SelectableTextView;

public class MainActivity extends AppCompatActivity {

    private SelectablePopupMenu selectablePopupMenu;
    private SelectableTextView selectableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectablePopupMenu = new SelectablePopupMenu(this);

        selectableTextView = findViewById(R.id.tv_content);
        selectablePopupMenu.setSelectableTextView(selectableTextView);
        selectableTextView.setOnSelectedListener(new SelectableTextView.OnSelectedListener() {
            @Override
            public void onSelectedChange(String selectedText, int startIndex, int endInex) {
                Log.e("onSelectedChange", "onSelectedChange");
                selectablePopupMenu.showAtLocation(selectableTextView, Gravity.NO_GRAVITY, 0, 0);
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
