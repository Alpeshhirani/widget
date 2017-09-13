package com.hirani.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hirani.R;
import com.hirani.utils.FontUtils;


/**
 * Created by root on 19/6/17.
 */

public class DButton extends AppCompatButton {

    public DButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextViewHelper.setTypeface(context, this, attrs);
    }

    public static class TextViewHelper {

        private static Typeface typeface = null;
        private static int type;

        public static void setTypeface(Context context, TextView textView, AttributeSet attrs) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DButton);
            try {
                type = ta.getInt(R.styleable.DButton_buttonFontFace, 1);

                typeface = FontUtils.fontName(context, type);


            } finally {
                ta.recycle();
            }


            textView.setTypeface(typeface);
        }

    }
}
