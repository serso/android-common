package org.solovyev.android.keyboard;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AndroidUtils;
import org.solovyev.android.view.drag.DirectionDragButtonDefImpl;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 11:58 PM
 */
public class LatinDragKeyboardController extends DragKeyboardController {

    @Override
    protected DragAKeyboardDef.KeyboardDef createKeyboardDef(@NotNull Context context) {
        final int notLetterBackgroundResId = R.drawable.metro_dark_button_gray;

        final DragAKeyboardDef.KeyboardDef result = new DragAKeyboardDef.KeyboardDef();

        final DragAKeyboardDef.RowDef firstRow = new DragAKeyboardDef.RowDef();
        firstRow.add(DirectionDragButtonDefImpl.newInstance("q", "Q", null, "1", "!"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("w", "W", null, "2", "@"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("e", "E", null, "3", "#"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("r", "R", null, "4", "$"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("t", "T", null, "5", "%"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("y", "Y", null, "6", "^"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("u", "U", null, "7", "&"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("i", "I", null, "8", "*"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("o", "O", null, "9", "("));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("p", "P", null, "0", ")"));
        result.add(firstRow);

        final DragAKeyboardDef.RowDef secondRow = new DragAKeyboardDef.RowDef();
        secondRow.add(DirectionDragButtonDefImpl.newInstance("a", "A", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("s", "S", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("d", "D", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("f", "F", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("g", "G", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("h", "H", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("j", "H", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("k", "K", null, null, null));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("l", "L", null, null, null));

        secondRow.add(DirectionDragButtonDefImpl.newDrawableInstance(R.drawable.kb_delete, CalculatorKeyboardController.DELETE, notLetterBackgroundResId));

        result.add(secondRow);

        final DragAKeyboardDef.RowDef thirdRow = new DragAKeyboardDef.RowDef();
        thirdRow.add(DirectionDragButtonDefImpl.newDrawableInstance(R.drawable.kb_copy, CalculatorKeyboardController.COPY, notLetterBackgroundResId));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("z", "Z", null, null, null));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("x", "X", null, null, null));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("c", "C", null, null, null));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("v", "V", null, null, null));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("b", "B", null, null, null));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("n", "N", null, null, null));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("m", "M", null, null, null));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance(",", ".", null, "!", "?", notLetterBackgroundResId));
        thirdRow.add(DirectionDragButtonDefImpl.newDrawableInstance(R.drawable.kb_enter, CalculatorKeyboardController.ENTER, notLetterBackgroundResId));
        result.add(thirdRow);

        final DragAKeyboardDef.RowDef fourthRow = new DragAKeyboardDef.RowDef();
        fourthRow.add(DirectionDragButtonDefImpl.newDrawableInstance(R.drawable.kb_paste, CalculatorKeyboardController.PASTE, notLetterBackgroundResId));

        // weight "eats" some margins => need to add them
        // 6 buttons with 1 dp margin needed for both sides = > / 2f
        int spaceMargin = AndroidUtils.toPixels(context.getResources().getDisplayMetrics(), (6 - 1) * 2f/ 2f);

        fourthRow.add(DirectionDragButtonDefImpl.newInstance("-", null, null, null, null, notLetterBackgroundResId));
        fourthRow.add(DirectionDragButtonDefImpl.newInstance("-", null, null, null, null, notLetterBackgroundResId));

        final DirectionDragButtonDefImpl spaceButtonDef = DirectionDragButtonDefImpl.newInstance(" ", null, null, null, null, notLetterBackgroundResId);
        spaceButtonDef.setWeight(4f);

        fourthRow.add(spaceButtonDef);

        fourthRow.add(DirectionDragButtonDefImpl.newInstance(".", ",", null, null, null, notLetterBackgroundResId));
        fourthRow.add(DirectionDragButtonDefImpl.newInstance(".", ",", null, null, null, notLetterBackgroundResId));
        fourthRow.add(DirectionDragButtonDefImpl.newInstance("0", "(", null, ")", null, notLetterBackgroundResId));
        result.add(fourthRow);

        return result;
    }
}
