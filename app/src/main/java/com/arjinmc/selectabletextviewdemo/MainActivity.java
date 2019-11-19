package com.arjinmc.selectabletextviewdemo;

import android.os.Bundle;
import android.util.Log;

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

        //You should define your own SelectablePopupMenu extends AbstractSelectablePopupMenu class
        //Then call SelectableTextView.setSelectablePopupMenu to bind the menu.
        selectablePopupMenu = new SelectablePopupMenu(this);
        selectableTextView = findViewById(R.id.tv_content);
        selectableTextView.setSelectablePopupMenu(selectablePopupMenu);
        selectableTextView.setOnSelectedListener(new SelectableTextView.OnSelectedListener() {
            @Override
            public void onSelectedChange(String selectedText, int startIndex, int endIndex) {
                Log.i("onSelectedChange", "selectedText:" + selectedText
                        + "\tstartIndex:" + startIndex + "\tendIndex:" + endIndex);
            }
        });

        //You should define your own SelectablePopupMenu extends AbstractSelectablePopupMenu class
        //Also the menu items
        selectablePopupMenu.setOnMenuOptionClickListener(new SelectablePopupMenu.OnMenuOptionClickListener() {
            @Override
            public void onCopy() {
                //copy your text here
                Log.i("OnMenuOptionClick","onCopy");
            }

            @Override
            public void onSelectAll() {
                //I made it select All
                Log.i("OnMenuOptionClick","onSelectAll");
            }

            @Override
            public void onCancel() {
                Log.i("OnMenuOptionClick","onCancel");
            }
        });
    }
}
