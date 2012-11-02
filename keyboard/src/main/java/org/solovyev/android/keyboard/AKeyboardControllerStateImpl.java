package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:32 PM
 */
public class AKeyboardControllerStateImpl implements AKeyboardControllerState {

    private boolean shifted;

	private boolean capsLock;

	private boolean completion;

    private boolean prediction;

	@NotNull
	private AKeyboard keyboard;

    private AKeyboardControllerStateImpl() {
    }

    @NotNull
    public static AKeyboardControllerState newDefaultState() {
        final AKeyboardControllerStateImpl result = new AKeyboardControllerStateImpl();

        result.shifted = false;
        result.capsLock = false;
        result.completion = false;
        result.prediction = false;

        return result;
    }

    @NotNull
    public static AKeyboardControllerState newInstance(boolean prediction, boolean completion) {
        final AKeyboardControllerStateImpl result = new AKeyboardControllerStateImpl();

        result.shifted = false;
        result.capsLock = false;
        result.completion = completion;
        result.prediction = prediction;

        return result;
    }

    @Override
    public boolean isShifted() {
        return shifted;
    }

    @Override
    public boolean isCapsLock() {
        return capsLock;
    }

    @Override
    public boolean isCompletion() {
        return completion;
    }

    @Override
    public boolean isPrediction() {
        return prediction;
    }

	@Override
	@NotNull
	public AKeyboard getKeyboard() {
		return keyboard;
	}

	@NotNull
	@Override
	public AKeyboardControllerState copyForNewKeyboard(@NotNull AKeyboard keyboard) {
		final AKeyboardControllerStateImpl result = new AKeyboardControllerStateImpl();
		result.capsLock = this.capsLock;
		result.prediction = this.prediction;
		result.shifted = this.shifted;
		result.completion = this.completion;
		result.keyboard = keyboard;
		return result;
	}

	@NotNull
	@Override
	public AKeyboardControllerState copyForNewCapsLock(boolean capsLock) {
		final AKeyboardControllerStateImpl result = new AKeyboardControllerStateImpl();
		result.capsLock = capsLock;
		result.prediction = this.prediction;
		result.shifted = this.shifted;
		result.completion = this.completion;
		result.keyboard = this.keyboard;
		return result;
	}

	@NotNull
	public static AKeyboardControllerState newDefaultState(@NotNull AKeyboard keyboard) {
		final AKeyboardControllerStateImpl keyboardControllerState = (AKeyboardControllerStateImpl) newDefaultState();
		keyboardControllerState.keyboard = keyboard;
		return keyboardControllerState;
	}
}
