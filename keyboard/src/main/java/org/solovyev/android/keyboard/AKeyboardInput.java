package org.solovyev.android.keyboard;

import android.view.KeyEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 12:17
 */
public interface AKeyboardInput {

	void commitTyped();

	void onText(@Nullable CharSequence text);

	@NotNull
	EditorInfo getCurrentInputEditorInfo();

	@Nullable
	InputConnection getCurrentInputConnection();

	@Nullable
	CharSequence getTypedText();

	void clearTypedText();

	boolean handleBackspace();

	void sendKeyEvent(@NotNull KeyEvent keyEvent);

	int translateKeyDown(int unicodeChar);

	void commitCompletion(@NotNull CompletionInfo completionInfo);

	void append(char primaryCode);

	void commitText(@Nullable String text, int i);

    void handleCursorRight();

    void handleCursorLeft();

    void handleClear();

    void handlePaste();

    void handleCopy();
}
