package com.hirani.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hirani.R;
import com.hirani.utils.FontUtils;


/**
 * Created by divyeshshani on 12/09/16.
 */
public class DCheckBox extends AppCompatCheckBox {

    public DCheckBox(Context context) {
        super(context);

//        TextViewHelper.setTypeface(context, this);
    }

    public DCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        TextViewHelper.setTypeface(context, this, attrs);
    }

    public static class TextViewHelper {

        private static Typeface typeface = null;
        private static int type;

        public static void setTypeface(Context context, TextView textView, AttributeSet attrs) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DCheckBox);
            try {
                type = ta.getInt(R.styleable.DCheckBox_checkboxFontFace, 1);

                typeface = FontUtils.fontName(context, type);


            } finally {
                ta.recycle();
            }


            textView.setTypeface(typeface);
        }

    }
}
