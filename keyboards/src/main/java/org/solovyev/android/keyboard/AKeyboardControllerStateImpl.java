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

import javax.annotation.Nonnull;

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

	@Nonnull
	private K keyboard;

    private AKeyboardControllerStateImpl() {
    }

    @Nonnull
    public static <K extends AKeyboard> AKeyboardControllerState<K> newDefaultState(@Nonnull K keyboard) {
        final AKeyboardControllerStateImpl<K> result = new AKeyboardControllerStateImpl<K>();

        result.shifted = false;
        result.capsLock = false;
        result.completion = false;
        result.prediction = false;
        result.keyboard = keyboard;

        return result;
    }

    @Nonnull
    public static <K extends AKeyboard> AKeyboardControllerState<K> newInstance(boolean prediction, boolean completion, @Nonnull K keyboard) {
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
	@Nonnull
	public K getKeyboard() {
		return keyboard;
	}

	@Nonnull
	@Override
	public AKeyboardControllerState<K> copyForNewKeyboard(@Nonnull K keyboard) {
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

    @Nonnull
    @Override
    public AKeyboardControllerState<K> copyForNewCapsLock(boolean capsLock) {
        final AKeyboardControllerStateImpl<K> result = copy();
        result.capsLock = capsLock;
        return result;
    }

    @Nonnull
    @Override
    public AKeyboardControllerState<K> copyForNewShift(boolean shifted) {
        final AKeyboardControllerStateImpl<K> result = copy();
        result.shifted = shifted;
        return result;
    }
}
