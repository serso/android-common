package org.solovyev.android.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.AndroidUtils;
import org.solovyev.android.view.AndroidViewUtils;
import org.solovyev.android.view.VibratorContainer;
import org.solovyev.android.view.drag.*;
import org.solovyev.common.math.Point2d;
import org.solovyev.common.text.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:47
 */
public class DragAndroidKeyboardView extends LinearLayout implements AndroidKeyboardView<DragAKeyboard>, SimpleOnDragListener.DragProcessor, View.OnTouchListener, View.OnClickListener {

    @Nullable
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;

    @NotNull
    private AKeyboardButtonPreview preview;

    @NotNull
    private final RepeatHelper repeatHelper = new RepeatHelper();

    @NotNull
    private final Map<View, DragAKeyboardButtonDef> defs = new HashMap<View, DragAKeyboardButtonDef>();

    @NotNull
    private final VibratorContainer vibrator;

    public DragAndroidKeyboardView(Context context) {
        super(context);
        preview = new AKeyboardButtonPreview(this);
        vibrator = new VibratorContainer((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE), PreferenceManager.getDefaultSharedPreferences(context), 1f);
    }

    public DragAndroidKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        preview = new AKeyboardButtonPreview(this);
        vibrator = new VibratorContainer((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE), PreferenceManager.getDefaultSharedPreferences(context), 1f);
    }

    public DragAndroidKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        preview = new AKeyboardButtonPreview(this);
        vibrator = new VibratorContainer((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE), PreferenceManager.getDefaultSharedPreferences(context), 1f);
    }

    @Override
    public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        this.keyboardActionListener = keyboardActionListener;
    }

    @Override
    public void setKeyboard(@NotNull DragAKeyboard keyboard) {
        setKeyboard(keyboard, null);
    }

    @Override
    public void closing() {
		this.preview.hide();
    }

    @Override
    public boolean handleBack() {
        return false;
    }

    @Override
    public boolean isShifted() {
        return false;
    }

    @Override
    public boolean setShifted(boolean shifted) {
        return false;
    }

    private void setKeyboard(@Nullable DragAKeyboard keyboard,
                             @Nullable LayoutInflater layoutInflater) {
        if (keyboard != null) {

            final DragAKeyboard.KeyboardDef keyboardDef = keyboard.getKeyboardDef();

            final Context context = this.getContext();
            int buttonMargin = AndroidUtils.toPixels(context.getResources().getDisplayMetrics(), 0.5f);

            if (layoutInflater == null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            }

            preview.createPreviewView(layoutInflater);

            final SimpleOnDragListener.Preferences defaultPreferences = SimpleOnDragListener.getDefaultPreferences(context);
            this.removeAllViews();

            this.defs.clear();

            for (DragAKeyboard.RowDef rowDef : keyboardDef.getRowDefs()) {
                final LinearLayout rowLayout = new LinearLayout(context);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (DragAKeyboardButtonDef buttonDef : rowDef.getButtonDefs()) {

                    Float weight = buttonDef.getLayoutWeight();
                    if (weight == null) {
                        weight = 1f;
                    }
                    final LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);
                    if (buttonDef.getLayoutMarginLeft() != null) {
                        params.leftMargin = buttonDef.getLayoutMarginLeft();
                    } else {
                        params.leftMargin = buttonMargin;
                    }
                    if (buttonDef.getLayoutMarginRight() != null) {
                        params.rightMargin = buttonDef.getLayoutMarginRight();
                    } else {
                        params.rightMargin = buttonMargin;
                    }

                    params.topMargin = buttonMargin;
                    params.bottomMargin = buttonMargin;

                    final Integer drawableResId = buttonDef.getDrawableResId();
                    if (drawableResId == null) {
                        final DirectionDragButton directionDragButton = (DirectionDragButton) layoutInflater.inflate(R.layout.drag_keyboard_drag_button, null);
                        directionDragButton.applyDef(buttonDef);
                        directionDragButton.setOnDragListener(new SimpleOnDragListener(this, defaultPreferences));
                        // we cannot use on touch listener here (in order to get repeat) as it will conflict with default DragButton logic
                        directionDragButton.setOnClickListener(this);
                        defs.put(directionDragButton, buttonDef);
                        rowLayout.addView(directionDragButton, params);
                    } else {
                        final ImageButton imageButton = (ImageButton) layoutInflater.inflate(R.layout.drag_keyboard_image_button, null);
                        AndroidViewUtils.applyButtonDef(imageButton, buttonDef);
                        imageButton.setOnTouchListener(this);
                        defs.put(imageButton, buttonDef);
                        rowLayout.addView(imageButton, params);
                    }
                }
                final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                this.addView(rowLayout, params);
            }
        }
    }

    @Override
    public boolean processDragEvent(@NotNull DragDirection dragDirection, @NotNull DragButton dragButton, @NotNull Point2d startPoint2d, @NotNull MotionEvent motionEvent) {
        if (dragButton instanceof DirectionDragButton) {
            final DirectionDragButton directionDragButton = (DirectionDragButton) dragButton;

            vibrator.vibrate();

            final Integer keycode = getKeycode(dragDirection, dragButton);

            return handleTextOrCode(dragButton, directionDragButton.getText(dragDirection), keycode, true);
        }
        return false;
    }

    private Integer getKeycode(@Nullable DragDirection dragDirection,
                               @NotNull View view) {
        Integer keycode = null;
        final DragAKeyboardButtonDef buttonDef = this.defs.get(view);
        if ( buttonDef != null ) {
            if (dragDirection != null) {
                keycode = buttonDef.getDirectionKeycode(dragDirection);
            } else {
                keycode = buttonDef.getKeycode();
            }

        }
        return keycode;
    }

    private boolean handleTextOrCode(@NotNull View view, @Nullable CharSequence text, @Nullable Integer keycode, boolean withPreview) {
        // we need to check if there is something in the tag

        if (keycode != null) {
            return handleKeycode(view, keycode, withPreview);
        } else {
            return handleText(view, text, withPreview);
        }
    }

    private boolean handleKeycode(@NotNull View view, @NotNull Integer keycode, boolean withPreview) {
        if (withPreview) {
            showPreview(view, null);
        }

        if (keyboardActionListener != null) {
            keyboardActionListener.onKey(keycode, null);
        }

        return true;
    }

    private boolean handleText(@NotNull View view, @Nullable CharSequence text, boolean withPreview) {
        if (!StringUtils.isEmpty(text)) {

            if (withPreview) {
                showPreview(view, text);
            }

            if (keyboardActionListener != null) {
                keyboardActionListener.onText(text);
            }

            return true;
        }
        return false;
    }

    private void showPreview(@NotNull View view, @Nullable CharSequence text) {
        final DirectionDragButtonDef buttonDef = defs.get(view);
        if (buttonDef != null) {
            preview.showPreview(view, text, buttonDef.getDrawableResId());
        } else {
            preview.showPreview(view, text, null);
        }
    }

    @Override
    public boolean onTouch(@NotNull final View v, @NotNull MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				repeatHelper.keyDown(v, isRepeatAllowed(v) ? new RepeatKeydownRunnable(v) : null);
				return true;

			case MotionEvent.ACTION_UP:
				repeatHelper.keyUp(v);
				return true;
		}

        return false;
    }

    private boolean isRepeatAllowed(View v) {
        boolean allowRepeat = false;

        final DragAKeyboardButtonDef buttonDef = defs.get(v);
        if ( buttonDef != null ) {
            allowRepeat = buttonDef.allowRepeat();
        }

        return allowRepeat;
    }

    @Override
    public void onClick(View v) {
        vibrator.vibrate();

        final Integer keycode = getKeycode(null, v);

        if (v instanceof TextView) {
            handleTextOrCode(v, ((TextView) v).getText(), keycode, true);
        } else {
            handleTextOrCode(v, null, keycode, true);
        }
    }

	private class RepeatKeydownRunnable implements Runnable {

		private final View view;

		public RepeatKeydownRunnable(@NotNull View view) {
			this.view = view;
		}

		@Override
		public void run() {
			final Integer keycode = getKeycode(null, view);

			if (view instanceof TextView) {
				handleTextOrCode(view, ((TextView) view).getText(), keycode, true);
			} else {
				handleTextOrCode(view, null, keycode, true);
			}
		}
	}
}
