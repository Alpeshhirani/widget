package com.hirani.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hirani.R;
import com.hirani.utils.FontUtils;


/**
 * Created by root on 24/4/17.
 */

public class DRadioButton extends AppCompatRadioButton {


    public DRadioButton(Context context) {
        super(context);

//        TextViewHelper.setTypeface(context, this);
    }

    public DRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TextViewHelper.setTypeface(context, this, attrs);
    }

    public static class TextViewHelper {

        private static Typeface typeface = null;
        private static int type;

        public static void setTypeface(Context context, TextView textView, AttributeSet attrs) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DRadioButton);
            try {
                type = ta.getInt(R.styleable.DRadioButton_radioFontFace, 1);
                typeface = FontUtils.fontName(context, type);



            } finally {
                ta.recycle();
            }


            textView.setTypeface(typeface);
        }

        public static void setTypeface(Context context, TextView textView) {

            if (typeface == null) {

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            }

            textView.setTypeface(typeface);
        }

    }
}
