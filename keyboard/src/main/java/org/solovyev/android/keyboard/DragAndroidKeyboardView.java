/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

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
import org.solovyev.android.AViews;
import org.solovyev.android.view.AndroidViewUtils;
import org.solovyev.android.view.VibratorContainer;
import org.solovyev.android.view.drag.*;
import org.solovyev.common.math.Point2d;
import org.solovyev.common.text.Strings;

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

    @Nullable
    private DragAKeyboard keyboard;

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
    public void close() {
		this.preview.hide();
    }

    @Override
    public void dismiss() {
        this.preview.hide();
    }

    @Override
    public void reload() {
        if (keyboard != null) {
            reloadView(keyboard, null);
        }
    }

    private void setKeyboard(@Nullable DragAKeyboard keyboard,
                             @Nullable LayoutInflater layoutInflater) {
        if (keyboard != null) {
            this.keyboard = keyboard;
            reloadView(keyboard, layoutInflater);
        }
    }

    private void reloadView(@NotNull DragAKeyboard keyboard, @Nullable LayoutInflater layoutInflater) {
        dismiss();

        final DragAKeyboard.KeyboardDef keyboardDef = keyboard.getKeyboardDef();

        final Context context = this.getContext();
        int buttonMargin = AViews.toPixels(context.getResources().getDisplayMetrics(), 0.5f);

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

                Integer layoutMarginLeft = buttonDef.getLayoutMarginLeft();
                if (layoutMarginLeft != null) {
                    params.leftMargin = layoutMarginLeft;
                } else {
                    params.leftMargin = buttonMargin;
                }

                Integer layoutMarginRight = buttonDef.getLayoutMarginRight();
                if (layoutMarginRight != null) {
                    params.rightMargin = layoutMarginRight;
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

    @Override
    public boolean processDragEvent(@NotNull DragDirection dragDirection, @NotNull DragButton dragButton, @NotNull Point2d startPoint2d, @NotNull MotionEvent motionEvent) {
        if (dragButton instanceof DirectionDragButton) {
            final DirectionDragButton directionDragButton = (DirectionDragButton) dragButton;

            vibrator.vibrate();

            final Integer keycode = getKeycode(dragDirection, dragButton);

            return handleTextOrCode(dragButton, directionDragButton.getText(dragDirection), keycode, true, dragDirection);
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

    private boolean handleTextOrCode(@NotNull View view,
                                     @Nullable CharSequence text,
                                     @Nullable Integer keycode,
                                     boolean withPreview,
                                     @Nullable DragDirection dragDirection) {
        // we need to check if there is something in the tag

        if (keycode != null) {
            return handleKeycode(view, text, keycode, withPreview, dragDirection);
        } else {
            return handleText(view, text, withPreview, dragDirection);
        }
    }

    private boolean handleKeycode(@NotNull View view,
                                  @Nullable CharSequence text,
                                  @NotNull Integer keycode,
                                  boolean withPreview,
                                  @Nullable DragDirection dragDirection) {
        if (withPreview) {
            showPreview(view, text, dragDirection);
        }

        if (keyboardActionListener != null) {
            keyboardActionListener.onKey(keycode, null);
        }

        return true;
    }

    private boolean handleText(@NotNull View view, @Nullable CharSequence text, boolean withPreview, @Nullable DragDirection dragDirection) {
        if (!Strings.isEmpty(text)) {

            if (withPreview) {
                showPreview(view, text, dragDirection);
            }

            if (keyboardActionListener != null) {
                keyboardActionListener.onText(text);
            }

            return true;
        }
        return false;
    }

    private void showPreview(@NotNull View view,
                             @Nullable CharSequence text,
                             @Nullable DragDirection direction) {

        final DragAKeyboardButtonDef buttonDef = defs.get(view);

        if (buttonDef != null) {

            CharSequence previewText;
            if ( direction != null ) {
                previewText = buttonDef.getPreviewDirectionText(direction);
            } else {
                previewText = buttonDef.getPreviewText();
            }

			Integer previewDrawableResId = null;

            if (direction == null) {
                previewDrawableResId = buttonDef.getPreviewDrawableResId();
            }

            if (previewDrawableResId == null) {
				previewDrawableResId = buttonDef.getDrawableResId();
			}

            if ( previewText == null && previewDrawableResId == null ) {
                previewText = text;
            }

			preview.showPreview(view, previewText, previewDrawableResId);
        } else {
            preview.showPreview(view, text, null);
        }
    }

    @Override
    public boolean onTouch(@NotNull final View v, @NotNull MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
                if ( isRepeatAllowed(v) ) {
                    repeatHelper.keyDown(v, new RepeatKeydownRunnable(v));
                } else {
                    repeatHelper.keyDown(v, null);
                    doKeydown(v);
                }
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
            handleTextOrCode(v, ((TextView) v).getText(), keycode, true, null);
        } else {
            handleTextOrCode(v, null, keycode, true, null);
        }
    }

	private class RepeatKeydownRunnable implements Runnable {

		private final View view;

		public RepeatKeydownRunnable(@NotNull View view) {
			this.view = view;
		}

        @Override
        public void run() {
            doKeydown(view);
        }
    }

    private void doKeydown(@NotNull View view) {
        final Integer keycode = getKeycode(null, view);

        if (view instanceof TextView) {
            handleTextOrCode(view, ((TextView) view).getText(), keycode, true, null);
        } else {
            handleTextOrCode(view, null, keycode, true, null);
        }
    }
}
