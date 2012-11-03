package org.solovyev.android.view.drag;

import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:51 PM
 */
public interface DragButtonDef {

    @Nullable
    Integer getDrawableResId();

    @Nullable
    String getTag();

    @Nullable
    Integer getBackgroundColor();

    @Nullable
    CharSequence getText();
}
