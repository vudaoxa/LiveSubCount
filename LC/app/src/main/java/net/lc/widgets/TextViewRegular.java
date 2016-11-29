package net.lc.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.icomhealthtap.icom.icomhealthtap.utils.FontCache;

/**
 * Created by chienchieu on 20/03/2016.
 */
public class TextViewRegular extends TextView {

    public TextViewRegular(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/Roboto-Regular.ttf", context);
        setTypeface(customFont);
    }
}