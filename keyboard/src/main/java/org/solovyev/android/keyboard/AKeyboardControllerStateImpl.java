package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:32 PM
 */
public class AKeyboardControllerStateImpl<K extends AKeyboard> implements AKeyboardControllerState<K> {

    private boolean shifted;

	private boolean capsLock;

	private boolean completion;

    private boolean prediction;

	@NotNull
	private K keyboard;

    private AKeyboardControllerStateImpl() {
    }

    @NotNull
    public static <K extends AKeyboard> AKeyboardControllerState<K> newDefaultState(@NotNull K keyboard) {
        final AKeyboardControllerStateImpl<K> result = new AKeyboardControllerStateImpl<K>();

        result.shifted = false;
        result.capsLock = false;
        result.completion = false;
        result.prediction = false;
        result.keyboard = keyboard;

        return result;
    }

    @NotNull
    public static <K extends AKeyboard> AKeyboardControllerState<K> newInstance(boolean prediction, boolean completion, @NotNull K keyboard) {
        final AKeyboardControllerStateImpl<K> result = new AKeyboardControllerStateImpl<K>();

        result.shifted = false;
        result.capsLock = false;
        result.completion = completion;
        result.prediction = prediction;
        result.keyboard = keyboard;

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
	public K getKeyboard() {
		return keyboard;
	}

	@NotNull
	@Override
	public AKeyboardControllerState<K> copyForNewKeyboard(@NotNull K keyboard) {
        final AKeyboardControllerStateImpl<K> result = copy();
        result.keyboard = keyboard;
        return result;
	}

    private AKeyboardControllerStateImpl<K> copy() {
        final AKeyboardControllerStateImpl<K> result = new AKeyboardControllerStateImpl<K>();
        result.capsLock = this.capsLock;
        result.prediction = this.prediction;
        result.shifted = this.shifted;
        result.completion = this.completion;
        result.keyboard = this.keyboard;
        return result;
    }

    @NotNull
    @Override
    public AKeyboardControllerState<K> copyForNewCapsLock(boolean capsLock) {
        final AKeyboardControllerStateImpl<K> result = copy();
        result.capsLock = capsLock;
        return result;
    }

    @NotNull
    @Override
    public AKeyboardControllerState<K> copyForNewShift(boolean shifted) {
        final AKeyboardControllerStateImpl<K> result = copy();
        result.shifted = shifted;
        return result;
    }
}
