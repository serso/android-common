/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 */

package org.solovyev.android.view.drag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.AndroidViewUtils;
import org.solovyev.common.math.Point2d;
import org.solovyev.common.text.StringUtils;

public class DragButton extends Button {

	@Nullable
	private Point2d startPoint = null;

	@Nullable
	private org.solovyev.android.view.drag.OnDragListener onDragListener;

	public DragButton(@NotNull Context context, @NotNull AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(new OnTouchListenerImpl());
	}

    public DragButton(@NotNull Context context, @NotNull DragButtonDef dragButtonDef) {
        super(context);
        setOnTouchListener(new OnTouchListenerImpl());

        setText(dragButtonDef.getText());
    }

    public void setOnDragListener(@Nullable org.solovyev.android.view.drag.OnDragListener onDragListener) {
		this.onDragListener = onDragListener;
	}

	@Nullable
	public org.solovyev.android.view.drag.OnDragListener getOnDragListener() {
		return onDragListener;
	}

    public void applyDef(@NotNull DragButtonDef buttonDef) {
        setText(buttonDef.getText());

        final Integer backgroundColor = buttonDef.getBackgroundColor();
        if (backgroundColor != null) {
            setBackgroundColor(backgroundColor);
        }

        final Integer drawableResId = buttonDef.getDrawableResId();
        if ( drawableResId != null ) {
            setPadding(0, 0, 0, 0);

            final Drawable drawable = getContext().getResources().getDrawable(drawableResId);
            setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }

        final String tag = buttonDef.getTag();
        if ( tag != null ) {
            setTag(tag);
        }

    }

    /**
	 * OnTouchListener implementation that fires onDrag()
	 * 
	 * @author serso
	 * 
	 */
	private final class OnTouchListenerImpl implements OnTouchListener {

		@Override
		public boolean onTouch(@NotNull View v, @NotNull MotionEvent event) {
			// processing on touch event

			// in order to avoid possible NPEs
			final Point2d localStartPoint = startPoint;
			final org.solovyev.android.view.drag.OnDragListener localOnDragListener = onDragListener;

			if (localOnDragListener != null) {
				// only if onDrag() listener specified

				Log.d(String.valueOf(getId()), "onTouch() for: " + getId() + " . Motion event: " + event);

				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// start tracking: set start point
						startPoint = new Point2d(event.getX(), event.getY());
						break;

					case MotionEvent.ACTION_UP:
						// stop tracking

						if (localStartPoint != null && localOnDragListener.onDrag(DragButton.this, new DragEvent(localStartPoint, event))) {
							if (localOnDragListener.isSuppressOnClickEvent()) {
								// prevent on click action
								setPressed(false);

								// sometimes setPressed(false); doesn't work so to prevent onClick action button disables
								if (v instanceof Button) {
									final Button button = (Button) v;

									button.setEnabled(false);

									new Handler().postDelayed(new Runnable() {
										public void run() {
											button.setEnabled(true);
										}
									}, 200);
								}
							}
						}

						startPoint = null;
						break;
				}
			}

			return false;
		}
	}

    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence text = getText();
        if ( !StringUtils.isEmpty(text)) {
            super.onDraw(canvas);
        } else {
            AndroidViewUtils.drawDrawables(canvas, this);
        }
    }
}
