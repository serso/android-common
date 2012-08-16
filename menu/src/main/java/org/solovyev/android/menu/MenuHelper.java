package org.solovyev.android.menu;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/4/12
 * Time: 2:49 PM
 */
public interface MenuHelper<M, MI> {

    int size(@NotNull M menu);

    @NotNull
    MI add(@NotNull M menu, int groupId, int itemId, int orderId, @NotNull String caption);

    public void setOnMenuItemClickListener(@NotNull MI menuItem, @NotNull AMenuItem<MI> onMenuItemClick, @NotNull Activity activity);

    void removeItem(@NotNull M menu, @NotNull Integer menuItemId);

    void inflateMenu(@NotNull Activity activity, int layoutId, @NotNull M menu);

    @NotNull
    Integer getItemId(@NotNull MI item);
}
