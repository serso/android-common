package org.solovyev.android.view.sidebar;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class SideBarSlider {

    /*
    **********************************************************************
    *
    *                           CONSTANTS
    *
    **********************************************************************
    */

    private static final int MAX_OFFSET_DIFF = 150;

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

    @Nonnull
    private final Animation.AnimationListener openListener = new SlidingViewOpenListener();

    @Nonnull
    private final Animation.AnimationListener closeListener = new SlidingViewCloseListener();

    /**
     * Offsets:
     * <p/>
     * in case of closing sliding view
     * close     offset    open
     * |<----------|--------|
     * <p/>
     * in case of opening sliding view
     * close  offset    open
     * |---------|------->|
     */

    private int offset;
    private int closedOffset;
    private int openedOffset;
    private boolean opening;

    @Nonnull
    private View sideBarLayout;

    private final SideBarAttributes attributes;

    @Nullable
    private final OnSlideListener onSlideListener;

    SideBarSlider(@Nonnull View sideBarLayout, SideBarAttributes attributes, @Nullable OnSlideListener onSlideListener) {
        this.sideBarLayout = sideBarLayout;
        this.offset = attributes.getSlidingViewLedge();
        this.closedOffset = attributes.getSlidingViewLedge();
        this.openedOffset = attributes.getSlidingViewLedge();
        this.opening = true;
        this.attributes = attributes;
        this.onSlideListener = onSlideListener;
    }

    public void init(int closedOffset, int openedOffset, boolean opening) {
        if (opening) {
            this.offset = closedOffset;
        } else {
            this.offset = openedOffset;
        }
        this.closedOffset = closedOffset;
        this.openedOffset = openedOffset;
        this.opening = opening;
    }

    public int getOffsetOnScreen() {
        final int result;
        switch (attributes.getSlidingViewPosition()) {
            case left:
            case top:
                result = offset;
                break;
            case right:
                result = sideBarLayout.getMeasuredWidth() - offset;
                break;
            case bottom:
                result = sideBarLayout.getMeasuredHeight() - offset;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return result;
    }

    public void completeOpening() {
        offset = openedOffset;
    }

    public void completeClosing() {
        offset = closedOffset;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isOpening() {
        return opening;
    }

    @Nonnull
    public Animation newCloseAnimation() {
        offset = Math.max(offset, closedOffset);
        final Animation animation = new SlideAnimation(offset, closedOffset);
        animation.setAnimationListener(closeListener);
        return animation;
    }

    @Nonnull
    public Animation newOpenAnimation() {
        offset = Math.min(offset, openedOffset);
        final Animation animation = new SlideAnimation(offset, openedOffset);
        animation.setAnimationListener(openListener);
        return animation;
    }

    public void addOffsetDelta(int delta) {
        switch (attributes.getSlidingViewPosition()) {
            case left:
            case top:
                offset += delta;
                break;
            case right:
            case bottom:
                offset -= delta;
                break;
        }

        final boolean canFinishSlide = offset <= closedOffset || offset >= openedOffset;

        offset = Math.min(offset, openedOffset);
        offset = Math.max(offset, closedOffset);

        if (canFinishSlide) {
            finishSlide();
        }
    }

    public void finishSlide() {

        final boolean proceedOpening;
        if (isOpening()) {
            final int third = Math.abs(openedOffset + 2 * closedOffset) / 3;
            proceedOpening = offset > third;
        } else {
            final int twoThirds = Math.abs(2 * openedOffset + closedOffset) / 3;
            proceedOpening = offset > twoThirds;
        }

        sideBarLayout.startAnimation(proceedOpening ? newOpenAnimation() : newCloseAnimation());
    }

    @Override
    public String toString() {
        return "SideBarViewOffsets{" +
                "offset=" + offset +
                ", closedOffset=" + closedOffset +
                ", openedOffset=" + openedOffset +
                ", opening=" + opening +
                '}';
    }

    public boolean canStartSlide(float z) {
        final boolean result;
        switch (attributes.getSlidingViewPosition()) {
            case left:
            case top:
                result = z < getOffsetOnScreen() + MAX_OFFSET_DIFF;
                break;
            case right:
            case bottom:
                result = z > getOffsetOnScreen() - MAX_OFFSET_DIFF;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return result;
    }

    /*
    **********************************************************************
    *
    *                           STATIC/INNER CLASSES
    *
    **********************************************************************
    */

    class SlideAnimation extends Animation {

        private static final float SPEED = 0.6f;

        private float mStart;
        private float mEnd;

        public SlideAnimation(float fromX, float toX) {
            mStart = fromX;
            mEnd = toX;

            setInterpolator(new DecelerateInterpolator());

            float duration = Math.abs(mEnd - mStart) / SPEED;
            setDuration((long) duration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            final float offset = (mEnd - mStart) * interpolatedTime + mStart;
            SideBarSlider.this.offset = (int) offset;

            sideBarLayout.postInvalidate();
        }

    }

    private class SlidingViewOpenListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            completeOpening();
            onSlideListener.onSlideCompleted(true);
        }
    }

    private class SlidingViewCloseListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            completeClosing();
            onSlideListener.onSlideCompleted(false);
        }
    }
}
