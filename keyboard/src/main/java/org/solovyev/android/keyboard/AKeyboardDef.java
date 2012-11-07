package org.solovyev.android.keyboard;

import android.content.res.Resources;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 1:05 PM
 */
public interface AKeyboardDef {

    void setImeOptions(@NotNull Resources resources, int imeOptions);

    void setShifted(boolean shifted);

	@NotNull
	String getKeyboardId();
}
