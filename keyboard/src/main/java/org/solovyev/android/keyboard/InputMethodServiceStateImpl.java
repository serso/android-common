package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:32 PM
 */
public class InputMethodServiceStateImpl implements InputMethodServiceState {

    private boolean shifted;
    private boolean capsLock;
    private boolean completion;
    private boolean prediction;

    private InputMethodServiceStateImpl() {
    }

    @NotNull
    public static InputMethodServiceState newDefaultState() {
        final InputMethodServiceStateImpl result = new InputMethodServiceStateImpl();

        result.shifted = false;
        result.capsLock = false;
        result.completion = false;
        result.prediction = false;

        return result;
    }

    @NotNull
    public static InputMethodServiceState newInstance(boolean prediction, boolean completion) {
        final InputMethodServiceStateImpl result = new InputMethodServiceStateImpl();

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

    public void setShifted(boolean shifted) {
        this.shifted = shifted;
    }

    public void setCapsLock(boolean capsLock) {
        this.capsLock = capsLock;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    public void setPrediction(boolean prediction) {
        this.prediction = prediction;
    }
}
