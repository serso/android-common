package org.solovyev.android.keyboard;

import android.content.res.Resources;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.view.drag.DirectionDragButtonDef;

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
    private final KeyboardDef keyboardDef;

    public DragAKeyboardDef(@NotNull KeyboardDef keyboardDef) {
        this.keyboardDef = keyboardDef;
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

        protected boolean add(RowDef object) {
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

        protected boolean add(@NotNull DirectionDragButtonDef dragButtonDef) {
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
