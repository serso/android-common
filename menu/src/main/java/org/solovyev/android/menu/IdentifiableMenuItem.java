package org.solovyev.android.menu;

import org.jetbrains.annotations.NotNull;

/**
* User: serso
* Date: 4/30/12
* Time: 11:06 AM
*/
public interface IdentifiableMenuItem<MI> extends AMenuItem<MI> {

    @NotNull
    Integer getItemId();
}
