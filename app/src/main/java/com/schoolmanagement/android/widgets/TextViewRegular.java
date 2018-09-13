package com.schoolmanagement.android.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 *
 */
public class TextViewRegular extends AppCompatTextView {

    public TextViewRegular(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRegular(Context context) {
        super(context);
        init();
    }

    public void init() {

        if (!isInEditMode()) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
            setTypeface(typeface, Typeface.NORMAL);
        }

    }

}
