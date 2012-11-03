package org.solovyev.android.keyboard;

import android.content.res.Resources;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.view.drag.DirectionDragButtonDef;
import org.solovyev.android.view.drag.DirectionDragButtonDefImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:45 PM
 */
public class DragAKeyboardDef implements AKeyboardDef {

    @NotNull
    private final KeyboardDef keyboardDef = new KeyboardDef();

    public DragAKeyboardDef() {
        final RowDef firstRow = new RowDef();
        firstRow.add(DirectionDragButtonDefImpl.newInstance("7"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("8"));
        firstRow.add(DirectionDragButtonDefImpl.newInstance("9"));
        this.keyboardDef.add(firstRow);

        final RowDef secondRow = new RowDef();
        secondRow.add(DirectionDragButtonDefImpl.newInstance("4"));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("5"));
        secondRow.add(DirectionDragButtonDefImpl.newInstance("6"));
        this.keyboardDef.add(secondRow);

        final RowDef thirdRow = new RowDef();
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("1"));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("2"));
        thirdRow.add(DirectionDragButtonDefImpl.newInstance("3"));
        this.keyboardDef.add(thirdRow);
    }

    @Override
    public void setImeOptions(@NotNull Resources resources, int imeOptions) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setShifted(boolean shifted) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public KeyboardDef getKeyboardDef() {
        return keyboardDef;
    }

    public static final class KeyboardDef {

        @NotNull
        private final List<RowDef> rowDefs = new ArrayList<RowDef>();

        public KeyboardDef() {
        }

        private boolean add(RowDef object) {
            return rowDefs.add(object);
        }

        private void clear() {
            rowDefs.clear();
        }

        @NotNull
        public List<RowDef> getRowDefs() {
            return Collections.unmodifiableList(rowDefs);
        }
    }

    public static final class RowDef {

        @NotNull
        private final List<DirectionDragButtonDef> buttonDefs = new ArrayList<DirectionDragButtonDef>();

        public RowDef() {
        }

        private boolean add(@NotNull DirectionDragButtonDef dragButtonDef) {
            return buttonDefs.add(dragButtonDef);
        }

        private void clear() {
            buttonDefs.clear();
        }

        @NotNull
        public List<DirectionDragButtonDef> getButtonDefs() {
            return Collections.unmodifiableList(buttonDefs);
        }
    }
}
