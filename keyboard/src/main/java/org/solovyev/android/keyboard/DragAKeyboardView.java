package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.LinearLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.drag.DirectionDragButton;
import org.solovyev.android.view.drag.DirectionDragButtonDef;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:47
 */
public class DragAKeyboardView implements AKeyboardView<DragAKeyboardDef> {

    @Nullable
    private LinearLayout rootLayout;

    @NotNull
    private final AKeyboardController keyboardController;

    @NotNull
    private final InputMethodService inputMethodService;

    public DragAKeyboardView(@NotNull AKeyboardController keyboardController,
                             @NotNull InputMethodService inputMethodService) {
        this.keyboardController = keyboardController;
        this.inputMethodService = inputMethodService;
    }

    @NotNull
    @Override
    public View getAndroidKeyboardView() {
        assert rootLayout != null;
        return rootLayout;
    }

    @Override
    public void setKeyboard(@NotNull DragAKeyboardDef keyboard) {
        final DragAKeyboardDef.KeyboardDef keyboardDef = keyboard.getKeyboardDef();

        if (rootLayout != null) {
            rootLayout.removeAllViews();

            for (DragAKeyboardDef.RowDef rowDef : keyboardDef.getRowDefs()) {
                final LinearLayout rowLayout = new LinearLayout(rootLayout.getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (DirectionDragButtonDef buttonDef : rowDef.getButtonDefs()) {
                    final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    rowLayout.addView(new DirectionDragButton(rootLayout.getContext(), buttonDef), params);
                }
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                rootLayout.addView(rowLayout, params);
            }
        }
    }

    @Override
    public void setOnKeyboardActionListener(@NotNull KeyboardView.OnKeyboardActionListener keyboardActionListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void closing() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSubtypeOnSpaceKey(@NotNull InputMethodSubtype subtype) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean handleBack() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isShifted() {
        return false;
    }

    @Override
    public void setShifted(boolean shifted) {
    }

    @Override
    public boolean isExtractViewShown() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createAndroidKeyboardView(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
        this.rootLayout = new LinearLayout(context);
        this.rootLayout.setOrientation(LinearLayout.VERTICAL);
    }
}
