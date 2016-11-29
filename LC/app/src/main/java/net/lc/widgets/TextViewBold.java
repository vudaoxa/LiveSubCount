package net.lc.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import net.lc.utils.FontCache;


/**
 * Created by chienchieu on 20/03/2016.
 */
public class TextViewBold extends TextView {

    public TextViewBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/RobotoCondensed-Bold.ttf", context);
        setTypeface(customFont);
    }
}