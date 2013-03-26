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

import android.content.Context;
import android.inputmethodservice.Keyboard;
import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 5:57 PM
 */
public class CalculatorKeyboardController extends DragKeyboardController {

    @Override
    protected DragAKeyboard createKeyboardDef(@Nonnull Context context) {
        final int operatorButtonColor = R.drawable.metro_dark_button;
        final DragAKeyboard.KeyboardDef result = new DragAKeyboard.KeyboardDef();

        final DragAKeyboard.RowDef firstRow = new DragAKeyboard.RowDef();
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("7", "i", null, "!", "ob:"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("8", "ln", null, "lg", "od:"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("9", "PI", null, "e", "ox:"));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("*", "^", null, "^2", null, operatorButtonColor));
        firstRow.add(DragAKeyboardButtonDefImpl.newInstance("C", CalculatorKeyboardController.KEYCODE_CLEAR));
        result.add(firstRow);

        final DragAKeyboard.RowDef secondRow = new DragAKeyboard.RowDef();
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("4", "x", null, "y", "D"));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("5", "t", null, "j", "E"));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("6", null, null, null, "F"));
        secondRow.add(DragAKeyboardButtonDefImpl.newInstance("/", "%", null, null, null, operatorButtonColor));
        secondRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_delete, Keyboard.KEYCODE_DELETE));
        result.add(secondRow);

        final DragAKeyboard.RowDef thirdRow = new DragAKeyboard.RowDef();
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("1", "sin", null, "asin", "A"));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("2", "cos", null, "acos", "B"));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("3", "tan", null, "atan", "C"));
        thirdRow.add(DragAKeyboardButtonDefImpl.newInstance("+", null, null, "E", null, operatorButtonColor));
        thirdRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_copy, CalculatorKeyboardController.KEYCODE_COPY));
        result.add(thirdRow);

        final DragAKeyboard.RowDef fourthRow = new DragAKeyboard.RowDef();
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("()", "(", null, ")", null));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("0", "00", null, "000", null));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance(".", ",", null, null, null));
        fourthRow.add(DragAKeyboardButtonDefImpl.newInstance("-", null, null, null, null, operatorButtonColor));
        fourthRow.add(DragAKeyboardButtonDefImpl.newDrawableInstance(R.drawable.kb_paste, CalculatorKeyboardController.KEYCODE_PASTE));
        result.add(fourthRow);

        return new DragAKeyboard("calculator", result);
    }
}
