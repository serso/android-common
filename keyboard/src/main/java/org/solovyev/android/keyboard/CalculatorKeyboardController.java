package org.solovyev.android.keyboard;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 5:57 PM
 */
public class CalculatorKeyboardController extends DragKeyboardController {

    @Override
    protected DragAKeyboardDef.KeyboardDef createKeyboardDef(@NotNull Context context) {
        return CalculatorDragAKeyboardDef.newKeyboardDef(context);
    }
}
