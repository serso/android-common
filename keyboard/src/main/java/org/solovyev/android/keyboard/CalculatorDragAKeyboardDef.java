package org.solovyev.android.keyboard;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.view.drag.DirectionDragButtonDefImpl;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 5:31 PM
 */
public final class CalculatorDragAKeyboardDef  {

    @NotNull
    public static DragAKeyboardDef.KeyboardDef newKeyboardDef(@NotNull Context context) {
        final int operatorButtonColor = context.getResources().getColor(R.color.metro_button_blue_background);
        final DragAKeyboardDef.KeyboardDef result = new DragAKeyboardDef.KeyboardDef();

        final DragAKeyboardDef.RowDef firstRow = new DragAKeyboardDef.RowDef();
        firstRow.add(DirectionDragButtonDefImpl.newInstance("7", "i", null, "!", "ob:"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("8", "ln", null, "lg", "od:"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("9", "PI", null, "e", "ox:"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("*", "^", null, "^2", null, operatorButtonColor));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("C", CalculatorKeyboardController.CLEAR));
        result.add(firstRow);

        final DragAKeyboardDef.RowDef secondRow = new DragAKeyboardDef.RowDef();
        secondRow.add(DirectionDragButtonDefImpl.newInstance("4", "x", null, "y", "D"));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("5", "t", null, "j", "E"));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("6", null, null, null, "F"));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("/", "%", null, null, null, operatorButtonColor));
        secondRow.add(DirectionDragButtonDefImpl.newDrawableInstance(R.drawable.sym_keyboard_delete, CalculatorKeyboardController.ERASE));
        result.add(secondRow);

        final DragAKeyboardDef.RowDef thirdRow = new DragAKeyboardDef.RowDef();
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("1", "sin", null, "asin", "A"));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("2", "cos", null, "acos", "B"));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("3", "tan", null, "atan", "C"));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("+", null, null, "E", null, operatorButtonColor));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("C", CalculatorKeyboardController.COPY));
        result.add(thirdRow);

        final DragAKeyboardDef.RowDef fourthRow = new DragAKeyboardDef.RowDef();
        fourthRow.add(DirectionDragButtonDefImpl.newInstance("()", "(", null, ")", null));
        fourthRow.add(DirectionDragButtonDefImpl.newInstance("0", "00", null, "000", null));
        fourthRow.add(DirectionDragButtonDefImpl.newInstance(".", ",", null, null, null));
        fourthRow.add(DirectionDragButtonDefImpl.newInstance("-", null, null, null, null, operatorButtonColor));
        fourthRow.add(DirectionDragButtonDefImpl.newInstance("P", CalculatorKeyboardController.PASTE));
        result.add(fourthRow);

        return result;
    }
}
