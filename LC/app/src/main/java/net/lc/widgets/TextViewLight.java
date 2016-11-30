package net.lc.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import net.lc.utils.FontCache;


/**
 * Created by chienchieu on 20/03/2016.
 */
public class TextViewLight extends TextView {

    public TextViewLight(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/Roboto-Light.ttf", context);
        setTypeface(customFont);
    }
}