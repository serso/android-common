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
	private final StringBuilder typedText = new StringBuilder(100);

	public DefaultKeyboardInput(@NotNull InputMethodService inputMethodService) {
		this.inputMethodService = inputMethodService;
	}

	@Override
	public void commitTyped() {
		final InputConnection inputConnection = inputMethodService.getCurrentInputConnection();
		if (typedText.length() > 0) {
			if (inputConnection != null) {
				inputConnection.commitText(typedText, typedText.length());
			}
			typedText.setLength(0);
		}
	}

	@Override
	public void onText(@Nullable CharSequence text) {
		final InputConnection inputConnection = inputMethodService.getCurrentInputConnection();
		if (inputConnection != null) {
			inputConnection.beginBatchEdit();
			if (this.typedText.length() > 0) {
				commitTyped();
			}
			inputConnection.commitText(text, 0);
			inputConnection.endBatchEdit();
		}
	}

    @Override
    public void commitText(@Nullable String text, int position) {
        final InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            inputConnection.commitText(text, position);
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
	public CharSequence getTypedText() {
		return typedText;
	}

	@Override
	public void clearTypedText() {
		this.typedText.setLength(0);
	}

	@Override
	public boolean handleBackspace() {
		boolean changed = false;

		int length = typedText.length();
		final InputConnection inputConnection = getCurrentInputConnection();
		if (inputConnection != null) {
			if (length > 1) {
				typedText.delete(length - 1, length);
				inputConnection.setComposingText(typedText, 1);
				changed = true;
			} else if (length > 0) {
				typedText.setLength(0);
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
		if (!StringUtils.isEmpty(typedText)) {
			char accent = typedText.charAt(typedText.length() - 1);
			int composed = KeyEvent.getDeadChar(accent, unicodeChar);

			if (composed != 0) {
				unicodeChar = composed;
				typedText.setLength(typedText.length() - 1);
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
		typedText.append(primaryCode);
		final InputConnection inputConnection = getCurrentInputConnection();
		if (inputConnection != null) {
			inputConnection.setComposingText(typedText, 1);
		}
	}
}
