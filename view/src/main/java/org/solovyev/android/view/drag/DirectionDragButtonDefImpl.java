package org.solovyev.android.view.drag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:57 PM
 */
public class DirectionDragButtonDefImpl implements DirectionDragButtonDef {

    @Nullable
    private CharSequence text;

    private Map<DragDirection, CharSequence> directionsTexts = new EnumMap<DragDirection, CharSequence>(DragDirection.class);

    private DirectionDragButtonDefImpl() {
    }

    @NotNull
    public static DirectionDragButtonDef newInstance(@Nullable CharSequence text) {
        return newInstance(text, null, null, null, null);
    }

    public static DirectionDragButtonDef newInstance(@Nullable CharSequence text,
                                                     @Nullable CharSequence up,
                                                     @Nullable CharSequence right,
                                                     @Nullable CharSequence down,
                                                     @Nullable CharSequence left) {
        final DirectionDragButtonDefImpl result = new DirectionDragButtonDefImpl();

        result.text = text;
        result.directionsTexts.put(DragDirection.up, up);
        result.directionsTexts.put(DragDirection.right, right);
        result.directionsTexts.put(DragDirection.down, down);
        result.directionsTexts.put(DragDirection.left, left);

        return result;
    }

    @Nullable
    @Override
    public CharSequence getText(@NotNull DragDirection dragDirection) {
        return directionsTexts.get(dragDirection);
    }

    @Nullable
    @Override
    public CharSequence getText() {
        return text;
    }
}
