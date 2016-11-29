package net.lc.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.icomhealthtap.icom.icomhealthtap.utils.FontCache;

/**
 * Created by Dieu on 27/07/2016.
 */
public class BoldTextView extends TextView {
    public BoldTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Roboto-Medium.ttf", context);
        setTypeface(customFont);
    }

}
