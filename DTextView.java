package com.hirani.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hirani.R;
import com.hirani.utils.FontUtils;


/**
 * Created by divyeshshani on 12/09/16.
 */
public class DTextView extends AppCompatTextView {

    public DTextView(Context context) {
        super(context);

    }

    public DTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TextViewHelper.setTypeface(context, this, attrs);
    }

    public static class TextViewHelper {

        private static Typeface typeface = null;
        private static int type;

        public static void setTypeface(Context context, TextView textView, AttributeSet attrs) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DTextView);
            try {
                type = ta.getInt(R.styleable.DTextView_textFontFace, 1);

                typeface = FontUtils.fontName(context, type);

            } finally {
                ta.recycle();
            }


            textView.setTypeface(typeface);
        }

        public static void setTypeface(Context context, TextView textView) {

            if (typeface == null) {

                typeface = FontUtils.fontName(context, 1);
            }

            textView.setTypeface(typeface);
        }

    }
}
