package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.LinearLayout;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.view.drag.DirectionDragButton;
import org.solovyev.android.view.drag.DirectionDragButtonDef;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 14:47
 */
public class DragAKeyboardView implements AKeyboardView<DragAKeyboardDef> {

    @NotNull
    private final LinearLayout rootLayout;

    @NotNull
    private final AKeyboardController<DragAKeyboardDef> keyboardController;

    @NotNull
    private final InputMethodService inputMethodService;

    public DragAKeyboardView(@NotNull LinearLayout rootLayout,
                             @NotNull AKeyboardController<DragAKeyboardDef> keyboardController,
                             @NotNull InputMethodService inputMethodService) {
        this.rootLayout = rootLayout;
        this.keyboardController = keyboardController;
        this.inputMethodService = inputMethodService;
    }

    @NotNull
    @Override
    public View getKeyboardView() {
        return rootLayout;
    }

    @Override
    public void setKeyboard(@NotNull DragAKeyboardDef keyboard) {
        final DragAKeyboardDef.KeyboardDef keyboardDef = keyboard.getKeyboardDef();

        rootLayout.removeAllViews();
        rootLayout.setOrientation(LinearLayout.VERTICAL);

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
}
