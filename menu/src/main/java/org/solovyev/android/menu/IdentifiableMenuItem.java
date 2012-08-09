package org.solovyev.android.menu;

import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;

/**
* User: serso
* Date: 4/30/12
* Time: 11:06 AM
*/
public interface IdentifiableMenuItem extends AMenuItem<MenuItem> {

    @NotNull
    Integer getItemId();
}
