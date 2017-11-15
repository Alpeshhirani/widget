package com.hirani.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Vishal Sojitra on 4/22/2016.
 */
public class FontUtils {

    public static Typeface createTypeface(Context context, boolean isBold) {
        if (isBold) return Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf");
        else return Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf");
    }

    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText
                    || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    public static void setFont(View v, Typeface font) {
        if (v instanceof TextView || v instanceof EditText
                || v instanceof Button) {
            ((TextView) v).setTypeface(font);
        }
    }

    public static Typeface fontName(Context context, int type) {
        Typeface typeface;
        switch (type) {


            case 1:
                typeface = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Montserrat-Light.ttf");
                break;
            case 2:
                typeface = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Montserrat-Regular.ttf");
                break;
            case 3:
                typeface = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Montserrat-LightItalic.ttf");
                break;

            default:
                typeface = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Montserrat-Light.ttf");
                break;

        }
        return typeface;
    }
}

