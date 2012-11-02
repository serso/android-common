package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:30 PM
 */
public interface AKeyboardControllerState {

    boolean isShifted();

    boolean isCapsLock();

    boolean isCompletion();

    boolean isPrediction();

	@NotNull
	AKeyboard getKeyboard();

	@NotNull
	AKeyboardControllerState copyForNewKeyboard(@NotNull AKeyboard keyboard);

	@NotNull
	AKeyboardControllerState copyForNewCapsLock(boolean capsLock);
}
