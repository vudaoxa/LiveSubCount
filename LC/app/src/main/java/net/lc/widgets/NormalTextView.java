package net.lc.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import net.lc.utils.FontCache;


/**
 * Created by Dieu on 27/07/2016.
 */
public class NormalTextView extends TextView {
    public NormalTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public NormalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public NormalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Roboto-Light.ttf", context);
        setTypeface(customFont);
    }

}
