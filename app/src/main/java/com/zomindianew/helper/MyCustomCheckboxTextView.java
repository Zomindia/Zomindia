package com.zomindianew.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckedTextView;


public class MyCustomCheckboxTextView extends CheckedTextView {
    public MyCustomCheckboxTextView(Context context) {
        super(context);
        // init(null);
    }

    public MyCustomCheckboxTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //// init(attrs);
    }

    public MyCustomCheckboxTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyCustomCheckboxTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //init(attrs);
    }


}
