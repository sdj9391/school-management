package com.schoolmanagement.android.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 *
 */

public class EditTextRegular extends AppCompatEditText {

    public EditTextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
        setTypeface(typeface, Typeface.NORMAL);
    }

}
