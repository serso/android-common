package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.StringUtils;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 12:24
 */
public class DefaultKeyboardInput implements AKeyboardInput {

	@NotNull
	private final InputMethodService inputMethodService;

	@NotNull
	private final StringBuilder text = new StringBuilder(1000);

	public DefaultKeyboardInput(@NotNull InputMethodService inputMethodService) {
		this.inputMethodService = inputMethodService;
	}

	@Override
	public void commitTyped() {
		final InputConnection inputConnection = inputMethodService.getCurrentInputConnection();
		if (text.length() > 0) {
			if (inputConnection != null) {
				inputConnection.commitText(text, text.length());
			}
			text.setLength(0);
		}
	}

	@Override
	public void onText(CharSequence text) {
		final InputConnection inputConnection = inputMethodService.getCurrentInputConnection();
		if (inputConnection != null) {
			inputConnection.beginBatchEdit();
			if (this.text.length() > 0) {
				commitTyped();
			}
			inputConnection.commitText(text, 0);
			inputConnection.endBatchEdit();
		}
	}

	@NotNull
	@Override
	public EditorInfo getCurrentInputEditorInfo() {
		return inputMethodService.getCurrentInputEditorInfo();
	}

	@Nullable
	@Override
	public InputConnection getCurrentInputConnection() {
		return inputMethodService.getCurrentInputConnection();
	}

	@Override
	public CharSequence getText() {
		return text;
	}

	@Override
	public void clear() {
		this.text.setLength(0);
	}

	@Override
	public boolean handleBackspace() {
		boolean changed = false;

		int length = text.length();
		final InputConnection inputConnection = getCurrentInputConnection();
		if (inputConnection != null) {
			if (length > 1) {
				text.delete(length - 1, length);
				inputConnection.setComposingText(text, 1);
				changed = true;
			} else if (length > 0) {
				text.setLength(0);
				inputConnection.commitText("", 0);
				changed = true;
			}
		}

		return changed;
	}

	@Override
	public void sendKeyEvent(@NotNull KeyEvent keyEvent) {
		final InputConnection inputConnection = getCurrentInputConnection();
		if (inputConnection != null) {
			inputConnection.sendKeyEvent(keyEvent);
		}
	}

	@Override
	public int translateKeyDown(int unicodeChar) {
		if (!StringUtils.isEmpty(text)) {
			char accent = text.charAt(text.length() - 1);
			int composed = KeyEvent.getDeadChar(accent, unicodeChar);

			if (composed != 0) {
				unicodeChar = composed;
				text.setLength(text.length() - 1);
			}
		}

		return unicodeChar;
	}

	@Override
	public void commitCompletion(@NotNull CompletionInfo completionInfo) {
		final InputConnection inputConnection = getCurrentInputConnection();
		if (inputConnection != null) {
			inputConnection.commitCompletion(completionInfo);
		}
	}

	@Override
	public void append(char primaryCode) {
		text.append(primaryCode);
		final InputConnection inputConnection = getCurrentInputConnection();
		if (inputConnection != null) {
			inputConnection.setComposingText(text, 1);
		}
	}

	@Override
	public void commitText(@Nullable String text, int i) {
		final InputConnection inputConnection = getCurrentInputConnection();
		if (inputConnection != null) {
			inputConnection.commitText(text, i);
		}
	}
}
