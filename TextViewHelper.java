package com.app.fixthys.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.app.fixthys.R;
import com.app.fixthys.utils.FontUtils;

/**
 * Created by Vishal Sojitra on 9/16/2017.
 */

class TextViewHelper {

    static void setTypeface(Context context, TextView textView, AttributeSet attrs) {
//        if (!textView.isInEditMode()) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DTextView);
            Drawable leftDrawable;
            Drawable topDrawable;
            Typeface typeface ;
            Drawable rightDrawable;
            Drawable bottomDrawable;
            int color;
            try {
                int type = ta.getInt(R.styleable.DTextView_textFontFace, 1);
                leftDrawable = ta.getDrawable(R.styleable.DTextView_leftDrawable);
                topDrawable = ta.getDrawable(R.styleable.DTextView_topDrawable);
                rightDrawable = ta.getDrawable(R.styleable.DTextView_rightDrawable);
                bottomDrawable = ta.getDrawable(R.styleable.DTextView_bottomDrawable);
                color = ta.getColor(R.styleable.DTextView_tintColor, ContextCompat.getColor(context, R.color.colorAccent));
                typeface = FontUtils.fontName(context, type);
            } finally {
                ta.recycle();
            }
            if (leftDrawable != null) {
                drawableTint(leftDrawable, color);
            }
            if (topDrawable != null) {
                drawableTint(topDrawable, color);
            }
            if (rightDrawable != null) {
                drawableTint(rightDrawable, color);
            }
            if (bottomDrawable != null) {
                drawableTint(bottomDrawable, color);
            }
            if (leftDrawable != null || topDrawable != null || rightDrawable != null || bottomDrawable != null)
                textView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
            textView.setTypeface(typeface);
//        }

    }

    private static void drawableTint(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
    }
}