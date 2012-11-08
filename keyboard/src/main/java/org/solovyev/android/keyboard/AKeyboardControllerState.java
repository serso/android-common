package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:30 PM
 */
public interface AKeyboardControllerState<K extends AKeyboard> {

    boolean isShifted();

    boolean isCapsLock();

    boolean isCompletion();

    boolean isPrediction();

	@NotNull
	K getKeyboard();

	@NotNull
	AKeyboardControllerState<K> copyForNewKeyboard(@NotNull K keyboard);

	@NotNull
	AKeyboardControllerState<K> copyForNewCapsLock(boolean capsLock);
}
