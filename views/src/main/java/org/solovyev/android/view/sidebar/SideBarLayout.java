package org.solovyev.android.view.sidebar;

import android.content.Context;
import android.graphics.*;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public final class SideBarLayout extends FrameLayout implements OnSlideListener {
	/*
    **********************************************************************
    *
    *                           CONSTANTS
    *
    **********************************************************************
    */

	// count of frames to be skipped in drawing of main view in case of opening/closing the sliding view
	private static final int DRAW_FRAMES_SKIP_COUNT = 5;
	private static final int MIN_Z_DIFF = 50;

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

	// cached values
	private Bitmap cachedBitmap;
	private Canvas cachedCanvas;
	private Paint cachedPaint;

	/**
	 * Used to avoid heavy drawing of main view in case of opening/closing the sliding view
	 */
	@Nonnull
	private final AtomicInteger drawCounter = new AtomicInteger(0);

	// NOTE: use getter as this field is lazily set and might be null in some cases
	private View mainView;

	// NOTE: use getter as this field is lazily set and might be null in some cases
	private View slidingView;

	@Nonnull
	private SideBarAttributes attributes;

	@Nonnull
	private SlidingViewState slidingViewState = SlidingViewState.Closed;

	@Nonnull
	private SideBarSlider slider;

	private boolean alwaysOpened = false;

	@Nullable
	private OnSlideListener listener;

    /*
    **********************************************************************
    *
    *                           CONSTRUCTORS
    *
    **********************************************************************
    */

	public SideBarLayout(Context context, int mainViewId, int slidingViewId) {
		super(context);
		attributes = SideBarAttributes.newAttributes(mainViewId, slidingViewId, 0, SideBarSlidingViewPosition.left);
		init(context, null);
	}

	public SideBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SideBarLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(@Nonnull Context context, @Nullable AttributeSet attrs) {
		if (attrs != null) {
			attributes = SideBarAttributes.newAttributes(context, attrs);
		}

		slider = new SideBarSlider(this, attributes, this);

		cachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
	}

    /*
    **********************************************************************
    *
    *                           METHODS
    *
    **********************************************************************
    */

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final View slidingView = getSlidingView();

		if (attributes.isSlideMainView()) {
			final int slidingViewLedge = attributes.getSlidingViewLedge();
			if (alwaysOpened || slidingViewLedge > 0) {
				final View mainView = getMainView();

				// margin for main view = width of sliding view
				final LayoutParams lp = (LayoutParams) mainView.getLayoutParams();

				if (alwaysOpened) {
					measureChild(slidingView, widthMeasureSpec, heightMeasureSpec);
					switch (attributes.getSlidingViewPosition()) {
						case left:
							lp.leftMargin = slidingView.getMeasuredWidth();
							break;
						case top:
							lp.topMargin = slidingView.getMeasuredWidth();
							break;
						case right:
							lp.rightMargin = slidingView.getMeasuredWidth();
							break;
						case bottom:
							lp.bottomMargin = slidingView.getMeasuredWidth();
							break;
					}
				} else {
					switch (attributes.getSlidingViewPosition()) {
						case left:
							measureChild(slidingView, slidingViewLedge, heightMeasureSpec);
							lp.leftMargin = slidingViewLedge;
							break;
						case top:
							measureChild(slidingView, widthMeasureSpec, slidingViewLedge);
							lp.topMargin = slidingViewLedge;
							break;
						case right:
							measureChild(slidingView, slidingViewLedge, heightMeasureSpec);
							lp.rightMargin = slidingViewLedge;
							break;
						case bottom:
							measureChild(slidingView, widthMeasureSpec, slidingViewLedge);
							lp.bottomMargin = slidingViewLedge;
							break;
					}
				}
			}
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		final int height = bottom - top;
		final int width = right - left;

		final View slidingView = getSlidingView();
		final View mainView = getMainView();

		final int slidingViewWidth = slidingView.getMeasuredWidth();
		final int slidingViewHeight = slidingView.getMeasuredHeight();

		final int offset;
		if (alwaysOpened || slidingViewState == SlidingViewState.Opened) {
			switch (attributes.getSlidingViewPosition()) {
				case left:
				case right:
					offset = slidingViewWidth;
					break;
				case top:
				case bottom:
					offset = slidingViewHeight;
					break;
				default:
					throw new UnsupportedOperationException();
			}
		} else if (slidingViewState == SlidingViewState.Closed) {
			offset = attributes.getSlidingViewLedge();
		} else {
			offset = slider.getOffset();
			// in transition => offset is already set
		}

		switch (attributes.getSlidingViewStyle()) {
			case hover:
				mainView.layout(0, 0, width, height);
				break;
			case push:
				switch (attributes.getSlidingViewPosition()) {
					case left:
						mainView.layout(offset, 0, width + offset, height);
						break;
					case top:
						mainView.layout(0, offset, width, height + offset);
						break;
					case right:
						mainView.layout(-offset, 0, width - offset, height);
						break;
					case bottom:
						mainView.layout(0, -offset, width, height - offset);
						break;
				}
				break;
		}

		switch (attributes.getSlidingViewPosition()) {
			case left:
				slidingView.layout(-slidingViewWidth + offset, 0, offset, height);
				break;
			case top:
				slidingView.layout(0, offset - slidingViewHeight, width, offset);
				break;
			case right:
				slidingView.layout(width - offset, 0, width - offset + slidingViewWidth, height);
				break;
			case bottom:
				slidingView.layout(0, height - offset, width, height - offset + slidingViewHeight);
				break;
		}

		invalidate();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		try {
			if (slidingViewState.isTransition()) {
				if (drawCounter.getAndIncrement() > DRAW_FRAMES_SKIP_COUNT) {
					updateCachedCanvas();

					// reset counter to start over
					drawCounter.set(0);
				}

				canvas.save();

				switch (attributes.getSlidingViewStyle()) {
					case push:
						switch (attributes.getSlidingViewPosition()) {
							case left:
								canvas.translate(slider.getOffset(), 0);
								break;
							case top:
								canvas.translate(0, slider.getOffset());
								break;
							case right:
								canvas.translate(-slider.getOffset(), 0);
								break;
							case bottom:
								canvas.translate(0, -slider.getOffset());
								break;
						}
						break;
				}

				canvas.drawBitmap(cachedBitmap, 0, 0, cachedPaint);

				canvas.restore();


				/*
                 * Draw only visible part of sliding view
				 */

				final View slidingView = getSlidingView();

				final int scrollX = slidingView.getScrollX();
				final int scrollY = slidingView.getScrollY();

				canvas.save();

				final int width = canvas.getWidth();
				final int height = canvas.getHeight();

				switch (attributes.getSlidingViewPosition()) {
					case left:
						canvas.clipRect(0, 0, slider.getOffsetOnScreen(), height, Region.Op.REPLACE);
						canvas.translate(-scrollX - (slidingView.getMeasuredWidth() - slider.getOffset()), -scrollY);
						break;
					case top:
						canvas.clipRect(0, 0, width, slider.getOffsetOnScreen(), Region.Op.REPLACE);
						canvas.translate(-scrollX, -scrollY - slidingView.getMeasuredHeight() + slider.getOffsetOnScreen());
						break;
					case right:
						canvas.clipRect(slider.getOffsetOnScreen(), 0, width, height, Region.Op.REPLACE);
						canvas.translate(-scrollX + slider.getOffsetOnScreen(), -scrollY);
						break;
					case bottom:
						canvas.clipRect(0, slider.getOffsetOnScreen(), width, height, Region.Op.REPLACE);
						canvas.translate(-scrollX, -scrollY + slider.getOffsetOnScreen());
						break;
					default:
						throw new UnsupportedOperationException();
				}

				slidingView.draw(canvas);

				canvas.restore();

			} else {
				if (!alwaysOpened) {
					if (!attributes.isSlidingViewLedgeExists()) {
						if (slidingViewState == SlidingViewState.Closed) {
							getSlidingView().setVisibility(View.GONE);
						}
					}
				}

				super.dispatchDraw(canvas);
			}
		} catch (IndexOutOfBoundsException e) {
            /*
             * Possibility of crashes on some devices (especially on Samsung).
			 * Usually, when ListView is empty.
			 */
		}
	}

	private void updateCachedCanvas() {
		final View mainView = getMainView();

		// we must clear canvas before drawing
		cachedCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		cachedCanvas.translate(-mainView.getScrollX(), -mainView.getScrollY());
		mainView.draw(cachedCanvas);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		boolean opened;
		if (slidingViewState == SlidingViewState.Opened) {
			opened = true;
		} else if (slidingViewState.isTransition()) {
			opened = slider.isOpening();
		} else {
			opened = false;
		}

		return new ViewState(super.onSaveInstanceState(), opened);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof ViewState) {
			final ViewState viewState = (ViewState) state;
			super.onRestoreInstanceState(viewState.getSuperState());

			if (viewState.mOpened) {
				openImmediately();
			} else {
				closeImmediately();
			}
		} else {
			super.onRestoreInstanceState(state);
		}
	}

	/**
	 * @return child view which is slided, in contrast to main view this view may be not shown initially and may appear only after user actions
	 */
	@Nonnull
	private View getSlidingView() {
		if (slidingView == null) {
			slidingView = findViewById(attributes.getSlidingViewId());
		}
		return slidingView;
	}

	@Nonnull
	private View getMainView() {
		if (mainView == null) {
			mainView = findViewById(attributes.getMainViewId());
		}
		return mainView;
	}

	public void setAlwaysOpened(boolean opened) {
		alwaysOpened = opened;

		requestLayout();
	}

	public void setOnSlideListener(OnSlideListener lis) {
		listener = lis;
	}

	public boolean isOpened() {
		return slidingViewState == SlidingViewState.Opened;
	}

	public void toggle(boolean immediately) {
		if (immediately) {
			toggleImmediately();
		} else {
			toggle();
		}
	}

	public void toggle() {
		if (isOpened()) {
			close();
		} else {
			open();
		}
	}

	public void toggleImmediately() {
		if (isOpened()) {
			closeImmediately();
		} else {
			openImmediately();
		}
	}

	public boolean open() {
		if (isOpened() || alwaysOpened || slidingViewState.isTransition()) {
			return false;
		}

		initSlideMode();

		startAnimation(slider.newOpenAnimation());

		invalidate();

		return true;
	}

	public boolean openImmediately() {
		if (isOpened() || alwaysOpened || slidingViewState.isTransition()) {
			return false;
		}

		getSlidingView().setVisibility(View.VISIBLE);
		slidingViewState = SlidingViewState.Opened;
		requestLayout();

		if (listener != null) {
			listener.onSlideCompleted(true);
		}

		return true;
	}

	public boolean close() {
		if (!isOpened() || alwaysOpened || slidingViewState.isTransition()) {
			return false;
		}

		initSlideMode();

		startAnimation(slider.newCloseAnimation());

		invalidate();

		return true;
	}

	public boolean closeImmediately() {
		if (!isOpened() || alwaysOpened || slidingViewState.isTransition()) {
			return false;
		}

		if (!attributes.isSlidingViewLedgeExists()) {
			getSlidingView().setVisibility(View.GONE);
		}

		slidingViewState = SlidingViewState.Closed;
		requestLayout();

		if (listener != null) {
			listener.onSlideCompleted(false);
		}

		return true;
	}

	private int mHistoricalZ = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		if (alwaysOpened) {
			return super.dispatchTouchEvent(e);
		} else if (!isEnabled() && slidingViewState == SlidingViewState.Closed) {
			return super.dispatchTouchEvent(e);
		}

		if (slidingViewState != SlidingViewState.Opened) {
			onTouchEvent(e);

			if (slidingViewState.isEndState()) {
				super.dispatchTouchEvent(e);
			} else {
				final MotionEvent cancelEvent = MotionEvent.obtain(e);
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
				super.dispatchTouchEvent(cancelEvent);
			}

			return true;
		} else {
			final View slidingView = getSlidingView();
			final View mainView = getMainView();

			final Rect slidingRect = new Rect();
			slidingView.getHitRect(slidingRect);

			if (!slidingRect.contains((int) e.getX(), (int) e.getY())) {
				// set main view coordinates
				e.offsetLocation(-mainView.getLeft(), -mainView.getTop());
				mainView.dispatchTouchEvent(e);

				// revert real view coordinates
				e.offsetLocation(mainView.getLeft(), mainView.getTop());
				onTouchEvent(e);

				return true;
			} else {
				onTouchEvent(e);

				e.offsetLocation(-slidingView.getLeft(), -slidingView.getTop());
				slidingView.dispatchTouchEvent(e);

				return true;
			}
		}
	}

	private boolean handleTouchEvent(@Nonnull MotionEvent e) {
		if (!isEnabled()) {
			return false;
		}

		final float z;
		switch (attributes.getSlidingViewPosition()) {
			case left:
			case right:
				z = e.getX();
				break;
			case top:
			case bottom:
				z = e.getY();
				break;
			default:
				throw new UnsupportedOperationException();
		}

		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mHistoricalZ = (int) z;
				return true;
			case MotionEvent.ACTION_MOVE:
				return handleTouchMove(z);
			case MotionEvent.ACTION_UP:
				if (slidingViewState == SlidingViewState.InTransition) {
					slider.finishSlide();
				}
				return false;
		}

		return slidingViewState.isTransition();
	}

	private boolean handleTouchMove(float z) {
		final float diff = z - mHistoricalZ;

		final float prevHistoricalZ = mHistoricalZ;
		mHistoricalZ = (int) z;

		if (slidingViewState.isTransition()) {
			if (slidingViewState == SlidingViewState.InTransition) {
				// in case of animation we do not need to update offset
				slider.addOffsetDelta((int) diff);
			}

			return true;
		} else {
			final boolean openingAllowed;
			final boolean closingAllowed;

			switch (attributes.getSlidingViewPosition()) {
				case left:
				case top:
					openingAllowed = diff > MIN_Z_DIFF && slidingViewState == SlidingViewState.Closed;
					closingAllowed = diff < -MIN_Z_DIFF && slidingViewState == SlidingViewState.Opened;
					break;
				case right:
				case bottom:
					openingAllowed = diff < -MIN_Z_DIFF && slidingViewState == SlidingViewState.Closed;
					closingAllowed = diff > MIN_Z_DIFF && slidingViewState == SlidingViewState.Opened;
					break;
				default:
					throw new UnsupportedOperationException();
			}

			if (openingAllowed || closingAllowed) {
				if (slider.canStartSlide(prevHistoricalZ)) {
					initSlideMode();
					slider.addOffsetDelta((int) diff);
				}
			}

			return false;
		}
	}

	@Override
	public void startAnimation(Animation animation) {
		slidingViewState = SlidingViewState.InAnimation;
		super.startAnimation(animation);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean handled = handleTouchEvent(ev);

		invalidate();

		return handled;
	}

	private void initSlideMode() {
		final View mainView = getMainView();

		// offsets for closed view state
		final int openedOffset;

		final int width = getWidth();
		final int height = getHeight();

		switch (attributes.getSlidingViewPosition()) {
			case left:
			case right:
				openedOffset = getSlidingView().getMeasuredWidth();
				break;
			case bottom:
			case top:
				openedOffset = getSlidingView().getMeasuredHeight();
				break;
			default:
				throw new UnsupportedOperationException("");
		}

		slider.init(attributes.getSlidingViewLedge(), openedOffset, slidingViewState == SlidingViewState.Closed);

		if (cachedBitmap == null || cachedBitmap.isRecycled() || cachedBitmap.getWidth() != width) {
			cachedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			cachedCanvas = new Canvas(cachedBitmap);
		}

		mainView.setVisibility(View.VISIBLE);

		updateCachedCanvas();

		slidingViewState = SlidingViewState.InTransition;

		getSlidingView().setVisibility(View.VISIBLE);
	}

	@Override
	public void onSlideCompleted(final boolean opened) {
		requestLayout();

		post(new Runnable() {

			@Override
			public void run() {
				if (opened) {
					slidingViewState = SlidingViewState.Opened;
					if (!attributes.isSlidingViewLedgeExists()) {
						getSlidingView().setVisibility(View.VISIBLE);
					}
				} else {
					slidingViewState = SlidingViewState.Closed;
					if (!attributes.isSlidingViewLedgeExists()) {
						getSlidingView().setVisibility(View.GONE);
					}
				}
			}
		});

		if (listener != null) {
			listener.onSlideCompleted(opened);
		}
	}

    /*
    **********************************************************************
    *
    *                           STATIC/INNER CLASSES
    *
    **********************************************************************
    */

	private static enum SlidingViewState {
		Closed(true),
		InTransition(false),
		InAnimation(false),
		Opened(true);

		private final boolean mEndState;

		SlidingViewState(boolean endState) {
			mEndState = endState;
		}

		public boolean isEndState() {
			return mEndState;
		}

		public boolean isTransition() {
			return !isEndState();
		}
	}

	public static class ViewState extends BaseSavedState {

		@Nonnull
		public static final Parcelable.Creator<ViewState> CREATOR = new Parcelable.Creator<ViewState>() {

			public ViewState createFromParcel(Parcel in) {
				return new ViewState(in);
			}

			public ViewState[] newArray(int size) {
				return new ViewState[size];
			}
		};

		private final boolean mOpened;

		public ViewState(Parcel in) {
			super(in);
			mOpened = in.readInt() == 1;

		}

		public ViewState(Parcelable state, boolean opened) {
			super(state);
			mOpened = opened;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(this.mOpened ? 1 : 0);
		}
	}

}

