package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:30 PM
 */
public interface AKeyboardControllerState<K extends AKeyboardDef> {

    boolean isShifted();

    boolean isCapsLock();

    boolean isCompletion();

    boolean isPrediction();

	@NotNull
	AKeyboard<? extends K> getKeyboard();

	@NotNull
	AKeyboardControllerState<K> copyForNewKeyboard(@NotNull AKeyboard<? extends K> keyboard);

	@NotNull
	AKeyboardControllerState<K> copyForNewCapsLock(boolean capsLock);
}
