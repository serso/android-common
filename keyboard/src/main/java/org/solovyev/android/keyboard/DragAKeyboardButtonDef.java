/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

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
	Integer getPreviewDrawableResId();

	@Nullable
	Integer getDirectionKeycode(@NotNull DragDirection dragDirection);

	void setImeOptions(@NotNull Resources resources, int imeOptions);

	void setShifted(boolean shifted);

    @Nullable
    CharSequence getPreviewText();

    @Nullable
    CharSequence getPreviewDirectionText(@NotNull DragDirection direction);
}
