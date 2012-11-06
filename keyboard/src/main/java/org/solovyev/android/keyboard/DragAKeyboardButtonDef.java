package org.solovyev.android.keyboard;

import android.content.res.Resources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.drag.DirectionDragButtonDef;
import org.solovyev.android.view.drag.DragDirection;

public interface DragAKeyboardButtonDef extends DirectionDragButtonDef {

	boolean allowRepeat();

	@Nullable
	Integer getKeycode();

	@Nullable
	Integer getDirectionKeycode(@NotNull DragDirection dragDirection);

	void setImeOptions(@NotNull Resources resources, int imeOptions);

	void setShifted(boolean shifted);
}
