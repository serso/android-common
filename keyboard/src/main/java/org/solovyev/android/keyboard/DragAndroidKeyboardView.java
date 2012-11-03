package org.solovyev.android.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.AndroidUtils;
import org.solovyev.android.view.drag.*;
import org.solovyev.common.math.Point2d;
import org.solovyev.common.text.StringUtils;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:47
 */
public class DragAndroidKeyboardView extends LinearLayout implements AndroidKeyboardView<DragAKeyboardDef>, View.OnClickListener, SimpleOnDragListener.DragProcessor {

    @Nullable
    private KeyboardView.OnKeyboardActionListener keyboardActionListener;

    public DragAndroidKeyboardView(Context context) {
        super(context);
    }

    public DragAndroidKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragAndroidKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        this.keyboardActionListener = keyboardActionListener;
    }

    @Override
    public void setKeyboard(@NotNull DragAKeyboardDef keyboard) {
        setKeyboard(keyboard, null);
    }

    @Override
    public void closing() {
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

    private void setKeyboard(@Nullable DragAKeyboardDef keyboard,
                             @Nullable LayoutInflater layoutInflater) {
        if (keyboard != null) {
            final DragAKeyboardDef.KeyboardDef keyboardDef = keyboard.getKeyboardDef();

            final Context context = this.getContext();
            int buttonMargin = AndroidUtils.toPixels(context.getResources().getDisplayMetrics(), 1);

            if ( layoutInflater == null ) {
                layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            }

            final SimpleOnDragListener.Preferences defaultPreferences = SimpleOnDragListener.getDefaultPreferences(context);
            this.removeAllViews();

            for (DragAKeyboardDef.RowDef rowDef : keyboardDef.getRowDefs()) {
                final LinearLayout rowLayout = new LinearLayout(context);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (DirectionDragButtonDef buttonDef : rowDef.getButtonDefs()) {

                    final LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    params.leftMargin = buttonMargin;
                    params.bottomMargin = buttonMargin;

                    final DirectionDragButton directionDragButton = (DirectionDragButton) layoutInflater.inflate(R.layout.drag_keyboard_button, null);
                    directionDragButton.applyDef(buttonDef);
                    directionDragButton.setOnDragListener(new SimpleOnDragListener(this, defaultPreferences));
                    directionDragButton.setOnClickListener(this);
                    rowLayout.addView(directionDragButton, params);
                }
                final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                this.addView(rowLayout, params);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if ( v instanceof DragButton ) {
             handleText(((DragButton) v).getText(), v.getTag());
        }
    }

    @Override
    public boolean processDragEvent(@NotNull DragDirection dragDirection, @NotNull DragButton dragButton, @NotNull Point2d startPoint2d, @NotNull MotionEvent motionEvent) {
        if ( dragButton instanceof DirectionDragButton) {
            final DirectionDragButton directionDragButton = (DirectionDragButton) dragButton;

            return handleText(directionDragButton.getText(dragDirection), dragButton.getTag());
        }
        return false;
    }

    private boolean handleText(@Nullable CharSequence text, @Nullable Object tagObject) {
        // we need to check if there is something in the tag
        if ( tagObject instanceof String && ((String) tagObject).startsWith(DragKeyboardController.ACTION)) {
            if (keyboardActionListener != null) {
                final String tag = ((String) tagObject);
                final String code = tag.substring(DragKeyboardController.ACTION.length());
                try {
                    keyboardActionListener.onKey(Integer.valueOf(code), null);
                } catch (NumberFormatException e) {
                    Log.e(DragAndroidKeyboardView.class.getSimpleName(), e.getMessage(), e);
                }
            }
            return true;
        } else {
            if (!StringUtils.isEmpty(text)) {
                if ( keyboardActionListener != null ) {
                    keyboardActionListener.onText(text);
                }

                return true;
            }
        }


        return false;
    }
}
