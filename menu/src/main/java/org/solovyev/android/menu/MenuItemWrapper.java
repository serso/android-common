package org.solovyev.android.menu;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
* User: serso
* Date: 8/13/12
* Time: 4:24 PM
*/
class MenuItemWrapper<MI> {

    @Nullable
    private final LabeledMenuItem<MI> labeledMenuItem;

    @Nullable
    private Integer menuItemId;

    @Nullable
    private final IdentifiableMenuItem<MI> identifiableMenuItem;

    MenuItemWrapper(@NotNull LabeledMenuItem<MI> labeledMenuItem) {
        this.labeledMenuItem = labeledMenuItem;
        this.identifiableMenuItem = null;
    }

    MenuItemWrapper(@NotNull IdentifiableMenuItem<MI> identifiableMenuItem) {
        this.identifiableMenuItem = identifiableMenuItem;
        this.labeledMenuItem = null;
    }

    @NotNull
    public AMenuItem<MI> getMenuItem() {
        return labeledMenuItem != null ? labeledMenuItem : identifiableMenuItem;
    }

    @Nullable
    public Integer getMenuItemId() {
        return identifiableMenuItem == null ? menuItemId : identifiableMenuItem.getItemId();
    }

    public void setMenuItemId(@Nullable Integer menuItemId) {
        assert labeledMenuItem != null;
        this.menuItemId = menuItemId;
    }

    @NotNull
    public String getCaption(@NotNull Activity activity) {
        assert labeledMenuItem != null;
        return labeledMenuItem.getCaption(activity);
    }
}
