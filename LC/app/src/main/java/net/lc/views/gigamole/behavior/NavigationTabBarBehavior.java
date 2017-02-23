package net.lc.views.gigamole.behavior;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import net.lc.views.gigamole.NavigationTabBar;
import net.live.sub.R;


public class NavigationTabBarBehavior extends VerticalScrollingBehavior<NavigationTabBar> {

    private final static Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private final static int ANIMATION_DURATION = 300;

    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private ObjectAnimator mTranslationObjectAnimator;
    private Snackbar.SnackbarLayout mSnackbarLayout;
    private FloatingActionButton mFloatingActionButton;

    private int mSnackbarHeight = -1;
    private float
            mTargetOffset = 0,
            mFabTargetOffset = 0,
            mFabDefaultBottomMargin = 0;

    private boolean mHidden;
    private boolean mFabBottomMarginInitialized;
    private boolean mBehaviorTranslationEnabled = true;

    public NavigationTabBarBehavior(final boolean behaviorTranslationEnabled) {
        super();
        this.mBehaviorTranslationEnabled = behaviorTranslationEnabled;
    }

    public NavigationTabBarBehavior(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.NavigationTabBar);
        typedArray.recycle();
    }

    private static ObjectAnimator objectAnimatorOfTranslationY(View target, int offset) {
        final ObjectAnimator res;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            res = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, offset);
        else {
            res = new ObjectAnimator();
            res.setTarget(target);
            res.setPropertyName("translationY");
            res.setFloatValues(offset);
        }
        return res;
    }

    public static NavigationTabBarBehavior from(NavigationTabBar view) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams))
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");

        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof NavigationTabBarBehavior))
            throw new IllegalArgumentException(
                    "The view is not associated with NavigationTabBarBehavior");

        return (NavigationTabBarBehavior) behavior;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, NavigationTabBar child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, NavigationTabBar child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, NavigationTabBar child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, NavigationTabBar child, View dependency) {
        updateSnackbar(child, dependency);
        updateFloatingActionButton(child, dependency);
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, NavigationTabBar child, @ScrollDirection int direction, int currentOverScroll, int totalOverScroll) {
    }

    @Override
    public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, NavigationTabBar child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection) {
    }

    @Override
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, NavigationTabBar child, View target, float velocityX, float velocityY, @ScrollDirection int scrollDirection) {
        return false;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, NavigationTabBar child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed < 0) handleDirection(child, ScrollDirection.SCROLL_DIRECTION_DOWN);
        else if (dyConsumed > 0) handleDirection(child, ScrollDirection.SCROLL_DIRECTION_UP);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, NavigationTabBar child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    // Handle scroll direction
    private void handleDirection(NavigationTabBar child, int scrollDirection) {
        if (!mBehaviorTranslationEnabled) return;
        if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && mHidden) {
            mHidden = false;
            animateOffset(child, 0, false, true);
        } else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !mHidden) {
            mHidden = true;
            animateOffset(child, child.getHeight(), false, true);
        }
    }

    // Animate offset
    private void animateOffset(final NavigationTabBar child, final int offset, boolean forceAnimation, boolean withAnimation) {
        if (!mBehaviorTranslationEnabled && !forceAnimation) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            ensureOrCancelObjectAnimation(child, offset, withAnimation);
            mTranslationObjectAnimator.start();
        } else {
            ensureOrCancelAnimator(child, withAnimation);
            mTranslationAnimator.translationY(offset).start();
        }
    }

    // Manage animation for Android >= KITKAT
    private void ensureOrCancelAnimator(final NavigationTabBar child, boolean withAnimation) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(child);
            mTranslationAnimator.setDuration(withAnimation ? ANIMATION_DURATION : 0);
            mTranslationAnimator.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(View view) {
                    // Animate snackbar
                    if (mSnackbarLayout != null && mSnackbarLayout.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                        mTargetOffset = child.getBarHeight() - view.getTranslationY();

                        final ViewGroup.MarginLayoutParams p =
                                (ViewGroup.MarginLayoutParams) mSnackbarLayout.getLayoutParams();

                        p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, (int) mTargetOffset);
                        mSnackbarLayout.requestLayout();
                    }
                    // Animate Floating Action Button
                    if (mFloatingActionButton != null && mFloatingActionButton.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                        final ViewGroup.MarginLayoutParams p =
                                (ViewGroup.MarginLayoutParams) mFloatingActionButton.getLayoutParams();

                        mFabTargetOffset = mFabDefaultBottomMargin - view.getTranslationY();
                        p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, (int) mFabTargetOffset);
                        mFloatingActionButton.requestLayout();
                    }
                }
            });
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.setDuration(withAnimation ? ANIMATION_DURATION : 0);
            mTranslationAnimator.cancel();
        }
    }

    // Manage animation for Android < KITKAT
    private void ensureOrCancelObjectAnimation(final NavigationTabBar child, final int offset, boolean withAnimation) {
        if (mTranslationObjectAnimator != null) mTranslationObjectAnimator.cancel();

        mTranslationObjectAnimator = objectAnimatorOfTranslationY(child, offset);
        mTranslationObjectAnimator.setDuration(withAnimation ? ANIMATION_DURATION : 0);
        mTranslationObjectAnimator.setInterpolator(INTERPOLATOR);
        mTranslationObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mSnackbarLayout != null && mSnackbarLayout.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    mTargetOffset = child.getBarHeight() - child.getTranslationY();

                    final ViewGroup.MarginLayoutParams p =
                            (ViewGroup.MarginLayoutParams) mSnackbarLayout.getLayoutParams();
                    p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, (int) mTargetOffset);
                    mSnackbarLayout.requestLayout();
                }
                // Animate Floating Action Button
                if (mFloatingActionButton != null && mFloatingActionButton.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    mFabTargetOffset = mFabDefaultBottomMargin - child.getTranslationY();

                    final ViewGroup.MarginLayoutParams p =
                            (ViewGroup.MarginLayoutParams) mFloatingActionButton.getLayoutParams();
                    p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, (int) mFabTargetOffset);
                    mFloatingActionButton.requestLayout();
                }
            }
        });
    }

    // Enable or not the behavior translation
    public void setBehaviorTranslationEnabled(boolean behaviorTranslationEnabled) {
        this.mBehaviorTranslationEnabled = behaviorTranslationEnabled;
    }

    // Hide NTB with animation
    public void hideView(NavigationTabBar view, int offset, boolean withAnimation) {
        if (!mHidden) {
            mHidden = true;
            animateOffset(view, offset, true, withAnimation);
        }
    }

    // Reset NTB position with animation
    public void resetOffset(NavigationTabBar view, boolean withAnimation) {
        if (mHidden) {
            mHidden = false;
            animateOffset(view, 0, true, withAnimation);
        }
    }

    // Update Snackbar bottom margin
    public void updateSnackbar(final NavigationTabBar child, View dependency) {
        if (dependency != null && dependency instanceof Snackbar.SnackbarLayout) {
            mSnackbarLayout = (Snackbar.SnackbarLayout) dependency;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mSnackbarLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (mFloatingActionButton != null &&
                                mFloatingActionButton.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                            mFabTargetOffset = mFabDefaultBottomMargin - child.getTranslationY();

                            final ViewGroup.MarginLayoutParams p =
                                    (ViewGroup.MarginLayoutParams) mFloatingActionButton.getLayoutParams();
                            p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, (int) mFabTargetOffset);
                            mFloatingActionButton.requestLayout();
                        }
                    }
                });
            }

            if (mSnackbarHeight == -1) mSnackbarHeight = dependency.getHeight();
            final int targetMargin = (int) (child.getBarHeight() - child.getTranslationY());

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) child.bringToFront();
            if (dependency.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                final ViewGroup.MarginLayoutParams p =
                        (ViewGroup.MarginLayoutParams) dependency.getLayoutParams();

                p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, targetMargin);
                dependency.requestLayout();
            }
        }
    }

    // Update floating action button bottom margin
    public void updateFloatingActionButton(final NavigationTabBar child, final View dependency) {
        if (dependency != null && dependency instanceof FloatingActionButton) {
            mFloatingActionButton = (FloatingActionButton) dependency;

            if (!mFabBottomMarginInitialized &&
                    dependency.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                mFabBottomMarginInitialized = true;

                final ViewGroup.MarginLayoutParams p =
                        (ViewGroup.MarginLayoutParams) dependency.getLayoutParams();
                mFabDefaultBottomMargin = p.bottomMargin;
            }
        }
    }
}