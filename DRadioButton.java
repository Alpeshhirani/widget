package com.hirani.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hirani.R;
import com.hirani.utils.FontUtils;


public class DRadioButton extends AppCompatRadioButton {

    public DRadioButton(Context context) {
        super(context);
    }

    public DRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextViewHelper.setTypeface(context, this, attrs);
    }
}
