package org.solovyev.android.keyboard;

/**
 * User: serso
 * Date: 11/1/12
 * Time: 9:30 PM
 */
public interface InputMethodServiceState {

    boolean isShifted();

    boolean isCapsLock();

    boolean isCompletion();

    boolean isPrediction();
}
