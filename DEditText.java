package com.hirani.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hirani.R;
import com.hirani.utils.FontUtils;


/**
 * Created by divyeshshani on 12/09/16.
 */
public class DEditText extends AppCompatEditText {

    public DEditText(Context context) {
        super(context);

        TextViewHelper.setTypeface(context, this);
    }

    public DEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TextViewHelper.setTypeface(context, this, attrs);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public long getLong() {

        return this != null && length() > 0 ? Long.parseLong(getText().toString()) : 0;
    }

    public int getInt() {

        return this != null && length() > 0 ? Integer.parseInt(getText().toString().trim()) : 0;
    }

    public String getString() {

        return this != null && length() > 0 ? getText().toString().trim() : "";
    }

    public void setText(long data) {
        if (data != 0) {
            setText("" + data);
        } else {
            setText("0");
        }
    }

    public void setText(long data, long def) {
        if (data != 0) {
            setText("" + data);
        } else {
            setText("" + def);
        }
    }

    public void setText(String data) {
        if (data != null && !data.equalsIgnoreCase("")) {
            super.setText(data);
        } else {
            super.setText("");
        }
    }

    public void setText(String data, String def) {
        if (data != null && !data.equalsIgnoreCase("")) {
            setText(data);
        } else {
            setText(def);
        }
    }

    public static class TextViewHelper {

        private static Typeface typeface = null;
        private static int type = 1;

        public static void setTypeface(Context context, TextView textView, AttributeSet attrs) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DEditText);
            try {
                type = ta.getInt(R.styleable.DEditText_editTextFontFace, 1);

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
