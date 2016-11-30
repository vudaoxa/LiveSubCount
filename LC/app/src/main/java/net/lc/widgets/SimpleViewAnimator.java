package net.lc.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import net.lc.R;


/**
 * Created by Dieu on 29/08/2016.
 */
public class SimpleViewAnimator extends LinearLayout {
    private Animation inAnimation;
    private Animation outAnimation;


    public SimpleViewAnimator(Context context) {
        super(context);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view));
    }

    public SimpleViewAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view));
    }

    public SimpleViewAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleViewAnimator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view));
    }


    public void setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            if (visibility == VISIBLE) {
                if (inAnimation != null) startAnimation(inAnimation);
            } else if ((visibility == INVISIBLE) || (visibility == GONE)) {
                if (outAnimation != null) startAnimation(outAnimation);
            }
        }
        super.setVisibility(visibility);
    }
}
