package com.schoolmanagement.android.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 *
 */
public class EditTextMedium extends AppCompatEditText {

    public EditTextMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextMedium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf");
        setTypeface(typeface, Typeface.NORMAL);
    }

}
