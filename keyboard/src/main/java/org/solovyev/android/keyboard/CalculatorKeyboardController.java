package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 5:57 PM
 */
public class CalculatorKeyboardController extends DragKeyboardController {

    @Override
    protected DragAKeyboardDef.KeyboardDef createKeyboardDef(@NotNull Context context) {
        final int operatorButtonColor = R.drawable.metro_dark_button;
        final DragAKeyboardDef.KeyboardDef result = new DragAKeyboardDef.KeyboardDef();

        final DragAKeyboardDef.RowDef firstRow = new DragAKeyboardDef.RowDef();
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("7", "i", null, "!", "ob:"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("8", "ln", null, "lg", "od:"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("9", "PI", null, "e", "ox:"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("*", "^", null, "^2", null, operatorButtonColor));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("C", CalculatorKeyboardController.KEYCODE_CLEAR));
        result.add(firstRow);

        final DragAKeyboardDef.RowDef secondRow = new DragAKeyboardDef.RowDef();
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("4", "x", null, "y", "D"));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("5", "t", null, "j", "E"));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("6", null, null, null, "F"));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("/", "%", null, null, null, operatorButtonColor));
        secondRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_delete, Keyboard.KEYCODE_DELETE));
        result.add(secondRow);

        final DragAKeyboardDef.RowDef thirdRow = new DragAKeyboardDef.RowDef();
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("1", "sin", null, "asin", "A"));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("2", "cos", null, "acos", "B"));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("3", "tan", null, "atan", "C"));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("+", null, null, "E", null, operatorButtonColor));
        thirdRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_copy, CalculatorKeyboardController.KEYCODE_COPY));
        result.add(thirdRow);

        final DragAKeyboardDef.RowDef fourthRow = new DragAKeyboardDef.RowDef();
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("()", "(", null, ")", null));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("0", "00", null, "000", null));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance(".", ",", null, null, null));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("-", null, null, null, null, operatorButtonColor));
        fourthRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_paste, CalculatorKeyboardController.KEYCODE_PASTE));
        result.add(fourthRow);

        return result;
    }
}
