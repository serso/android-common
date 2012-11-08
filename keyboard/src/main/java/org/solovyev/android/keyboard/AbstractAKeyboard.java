package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractAKeyboard implements AKeyboard {

	@NotNull
	private final String keyboardId;

	protected AbstractAKeyboard(@NotNull String keyboardId) {
		this.keyboardId = keyboardId;
	}

	@Override
	@NotNull
	public String getKeyboardId() {
		return keyboardId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractAKeyboard)) return false;

		AbstractAKeyboard that = (AbstractAKeyboard) o;

		if (!keyboardId.equals(that.keyboardId)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return keyboardId.hashCode();
	}
}
