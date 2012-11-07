package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractAKeyboardDef implements AKeyboardDef {

	@NotNull
	private final String keyboardId;

	protected AbstractAKeyboardDef(@NotNull String keyboardId) {
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
		if (!(o instanceof AbstractAKeyboardDef)) return false;

		AbstractAKeyboardDef that = (AbstractAKeyboardDef) o;

		if (!keyboardId.equals(that.keyboardId)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return keyboardId.hashCode();
	}
}
