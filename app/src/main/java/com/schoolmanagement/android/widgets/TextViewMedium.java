package com.schoolmanagement.android.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 *
 */

public class TextViewMedium extends AppCompatTextView {

    public TextViewMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewMedium(Context context) {
        super(context);
        init();
    }

    public void init() {

        if (!isInEditMode()) {
            Typeface typeface = TextViewMedium.getCustomTypeface(getContext());
            setTypeface(typeface, Typeface.NORMAL);
        }

    }

    public static Typeface getCustomTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
    }

}
