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

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.text.InputType;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.solovyev.common.text.Strings;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 11:33
 */
public abstract class AbstractKeyboardController<K extends AKeyboard> implements AKeyboardController {

    /*
    **********************************************************************
    *
    *                           STATIC
    *
    **********************************************************************
    */
	/**
	 * This boolean indicates the optional example code for performing
	 * processing of hard keys in addition to regular text generation
	 * from on-screen interaction.  It would be used for input methods that
	 * perform language translations (such as converting text entered on
	 * a QWERTY keyboard to Chinese), but may not be used for input methods
	 * that are primarily intended to be used for on-screen text entry.
	 */
	private static final boolean PROCESS_HARD_KEYS = false;

    public static final int KEYCODE_CLEAR = -800;
    public static final int KEYCODE_COPY = -801;
    public static final int KEYCODE_ENTER = 10;
    public static final int KEYCODE_PASTE = -802;
    public static final int KEYCODE_CURSOR_LEFT = -803;
    public static final int KEYCODE_CURSOR_RIGHT = -804;
    public static final int KEYCODE_PREV_KEYBOARD = -805;
    public static final int KEYCODE_NEXT_KEYBOARD = -806;
    public static final int KEYCODE_UNDO = -807;
    public static final int KEYCODE_REDO = -808;

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

	@Nonnull
	private AKeyboardControllerState<K> state;

	@Nonnull
	private AKeyboardView<K> keyboardView;

	@Nonnull
	private AKeyboardInput keyboardInput;

	@Nonnull
	private InputMethodService inputMethodService;

	private long metaState;

	@Nonnull
	private InputMethodManager inputMethodManager;

    @Nonnull
	private AKeyboardConfiguration configuration;

    /*
    **********************************************************************
    *
    *                           CONSTRUCTORS
    *
    **********************************************************************
    */

    protected AbstractKeyboardController() {
    }

    /*
    **********************************************************************
    *
    *                           LIFECYCLE
    *
    **********************************************************************
    */

    @Override
    public final void onCreate(@Nonnull Context context) {
        this.inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.configuration = onCreate0(context);
    }

    @Nonnull
    protected abstract AKeyboardConfiguration onCreate0(@Nonnull Context context);

    @Override
	public final void onInitializeInterface(@Nonnull InputMethodService inputMethodService) {
		this.inputMethodService = inputMethodService;
		this.state = onInitializeInterface0(inputMethodService);
		this.keyboardInput = createKeyboardInput0(inputMethodService);
        this.keyboardView = createKeyboardView0(inputMethodService);
    }

	@Nonnull
	protected abstract AKeyboardControllerState<K> onInitializeInterface0(@Nonnull InputMethodService inputMethodService);

	@Nonnull
	protected DefaultKeyboardInput createKeyboardInput0(@Nonnull InputMethodService inputMethodService) {
		return new DefaultKeyboardInput(inputMethodService);
	}

	@Nonnull
	protected abstract AKeyboardView<K> createKeyboardView0(@Nonnull Context context);

    @Nonnull
    @Override
    public final AKeyboardView createKeyboardView(@Nonnull Context context, @Nonnull LayoutInflater layoutInflater) {
        keyboardView.createAndroidKeyboardView(context, layoutInflater);
        keyboardView.setKeyboard(getCurrentKeyboard());
        keyboardView.setOnKeyboardActionListener(new DefaultKeyboardActionListener(this));
        return keyboardView;
    }

    @Override
    public View onCreateCandidatesView() {
        return null;
    }

    @Override
    public void onStartInput(@Nonnull EditorInfo attribute, boolean restarting) {
        if (!restarting) {
            // Clear shift states.
            metaState = 0;
        }

        this.state = onStartInput0(attribute, restarting);

        keyboardInput.clearTypedText();

        // Update the label on the enter key, depending on what the application
        // says it will do.
        getCurrentKeyboard().setImeOptions(inputMethodService.getResources(), attribute.imeOptions);
    }


    @Override
    public void onFinishInput() {
        keyboardInput.clearTypedText();
        keyboardView.close();
    }

    /*
    **********************************************************************
    *
    *                           SYSTEM CALLS
    *
    **********************************************************************
    */

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        // Apply the selected keyboard to the input view.
        keyboardView.setKeyboard(getCurrentKeyboard());
        keyboardView.close();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final InputMethodSubtype subtype = inputMethodManager.getCurrentInputMethodSubtype();
            keyboardView.setSubtypeOnSpaceKey(subtype);
        }
    }

	@Nonnull
	protected InputMethodService getInputMethodService() {
		return inputMethodService;
	}

    @Override
    public final boolean onKey(int primaryCode, @Nullable int[] keyCodes) {
        boolean consumed = handleSpecialKey(primaryCode);

        if ( !consumed ) {

            if (isWordSeparator(primaryCode)) {
                // Handle separator
                if (!Strings.isEmpty(getKeyboardInput().getTypedText())) {
                    getKeyboardInput().commitTyped();
                }
                sendKey(primaryCode);
                updateShiftKeyState(getInputMethodService().getCurrentInputEditorInfo());
                consumed = true;
            } else {
                handleCharacter(primaryCode, keyCodes);
            }
        }

        return consumed;
    }

    protected boolean handleSpecialKey(int primaryCode) {
        boolean consumed = false;

        switch (primaryCode) {
            case Keyboard.KEYCODE_MODE_CHANGE:
                handleModeChange();
                consumed = true;
                break;
            case Keyboard.KEYCODE_DELETE:
                handleBackspace();
                consumed = true;
                break;
            case Keyboard.KEYCODE_CANCEL:
                handleClose();
                consumed = true;
                break;
            case Keyboard.KEYCODE_SHIFT:
                handleShift();
                consumed = true;
                break;
            case KEYCODE_COPY:
                getKeyboardInput().handleCopy();
                consumed = true;
                break;
            case KEYCODE_PASTE:
                getKeyboardInput().handlePaste();
                consumed = true;
                break;
            case KEYCODE_CLEAR:
                getKeyboardInput().handleClear();
                consumed = true;
                break;
            case KEYCODE_CURSOR_LEFT:
                getKeyboardInput().handleCursorLeft();
                consumed = true;
                break;
            case KEYCODE_CURSOR_RIGHT:
                getKeyboardInput().handleCursorRight();
                consumed = true;
                break;
            case KEYCODE_PREV_KEYBOARD:
                handlePrevKeyboard();
                consumed = true;
                break;
            case KEYCODE_NEXT_KEYBOARD:
                handleNextKeyboard();
                consumed = true;
                break;
            case KEYCODE_UNDO:
                getKeyboardInput().undo();
                consumed = true;
                break;
            case KEYCODE_REDO:
                getKeyboardInput().redo();
                consumed = true;
                break;
        }

        return consumed;
    }

    protected void handleModeChange() {
    }

    protected void handleNextKeyboard() {
    }

    protected void handlePrevKeyboard() {
    }

    private void handleShift() {
        boolean newState = !this.state.isShifted();

        setShifted(newState);
    }

    protected final void setShifted(boolean shifted) {
        if ( shifted != this.state.isShifted() ) {
            this.state = this.state.copyForNewShift(shifted);
            this.state.getKeyboard().setShifted(shifted);
            this.keyboardView.reloadAndroidKeyboardView();
            this.setShifted0(shifted);
        }
    }

    protected void setShifted0(boolean shifted) {
    }

    @Nonnull
	protected K getCurrentKeyboard() {
		return state.getKeyboard();
	}

	protected void setCurrentKeyboard(@Nonnull K keyboard) {
		this.state = this.state.copyForNewKeyboard(keyboard);
		this.keyboardView.setKeyboard(keyboard);
	}

	@Nonnull
	protected AKeyboardControllerState<K> getState() {
		return state;
	}

	protected void setState(@Nonnull AKeyboardControllerState<K> state) {
		this.state = state;
	}

	@Nonnull
    protected AKeyboardView<K> getKeyboardView() {
		return keyboardView;
	}

	@Nonnull
	protected AKeyboardInput getKeyboardInput() {
		return keyboardInput;
	}

	@Override
	public void handleClose() {
		keyboardInput.commitTyped();
		inputMethodService.requestHideSelf(0);
		keyboardView.close();
	}

	@Nonnull
	public abstract AKeyboardControllerState<K> onStartInput0(@Nonnull EditorInfo attribute, boolean restarting);

	@Override
	public void onText(@Nullable CharSequence text) {
		keyboardInput.onText(text);

		updateShiftKeyState(keyboardInput.getCurrentInputEditorInfo());
	}

	@Override
	public void onDisplayCompletions(@Nullable CompletionInfo[] completions) {
	}

	/**
	 * Helper to update the shift state of our keyboard based on the initial
	 * editor state.
	 */
	public void updateShiftKeyState(@Nullable EditorInfo attr) {
		if (attr != null) {
			final EditorInfo editorInfo = keyboardInput.getCurrentInputEditorInfo();

			int caps = 0;
			if (editorInfo.inputType != InputType.TYPE_NULL) {
                caps = keyboardInput.getCursorCapsMode(attr.inputType);
			}

            boolean shifted = state.isCapsLock() || caps != 0;
            setShifted(shifted);
		}
	}

	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
		// If the current selection in the text view changes, we should
		// clear whatever candidate text we have.
		final CharSequence text = keyboardInput.getTypedText();
		if (!Strings.isEmpty(text) && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
			keyboardInput.clearTypedText();
			updateCandidates();
            keyboardInput.finishComposingText();
		}
	}

    protected void updateCandidates() {
    }

    @Override
	public boolean onKeyDown(int keyCode, @Nonnull KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				// The InputMethodService already takes care of the back
				// key for us, to dismiss the input method if it is shown.
				// However, our keyboard could be showing a pop-up window
				// that back should dismiss, so we first allow it to do that.
				if (event.getRepeatCount() == 0) {
					keyboardView.dismiss();
					return true;
				}
				break;

			case KeyEvent.KEYCODE_DEL:
				// Special handling of the delete key: if we currently are
				// composing text for the user, we want to modify that instead
				// of let the application to the delete itself.
				final CharSequence text = keyboardInput.getTypedText();
				if (!Strings.isEmpty(text)) {
					onKey(Keyboard.KEYCODE_DELETE, null);
					return true;
				}
				break;

			case KeyEvent.KEYCODE_ENTER:
				// Let the underlying text editor always handle these.
				return false;

			default:
				// For all other keys, if we want to do transformations on
				// text being entered with a hard keyboard, we need to process
				// it and do the appropriate action.
				if (PROCESS_HARD_KEYS) {
					if (keyCode == KeyEvent.KEYCODE_SPACE && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
						// A silly example: in our input method, Alt+Space
						// is a shortcut for 'android' in lower case.

                        // First, tell the editor that it is no longer in the
                        // shift state, since we are consuming this.
                        keyboardInput.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                        keyboardInput.keyDownUp(KeyEvent.KEYCODE_A);
                        keyboardInput.keyDownUp(KeyEvent.KEYCODE_N);
                        keyboardInput.keyDownUp(KeyEvent.KEYCODE_D);
                        keyboardInput.keyDownUp(KeyEvent.KEYCODE_R);
                        keyboardInput.keyDownUp(KeyEvent.KEYCODE_O);
                        keyboardInput.keyDownUp(KeyEvent.KEYCODE_I);
                        keyboardInput.keyDownUp(KeyEvent.KEYCODE_D);
                        // And we consume this event.
                        return true;
                    }
					if (state.isPrediction() && translateKeyDown(keyCode, event)) {
						return true;
					}
				}
		}

		return false;
	}

	public boolean handleBackspace() {
        boolean changed = keyboardInput.handleBackspace();
        if (!changed) {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }

        updateShiftKeyState(keyboardInput.getCurrentInputEditorInfo());

        return changed;
	}

	/**
	 * Helper to send a key down / key up pair to the current editor.
	 */
	public void keyDownUp(int keyEventCode) {
        keyboardInput.keyDownUp(keyEventCode);
	}

	/**
	 * This translates incoming hard key events in to edit operations on an
	 * InputConnection.  It is only needed when using the
	 * PROCESS_HARD_KEYS option.
	 */
	private boolean translateKeyDown(int keyCode, @Nonnull KeyEvent event) {
		metaState = MetaKeyKeyListener.handleKeyDown(metaState, keyCode, event);
		int unicodeChar = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(metaState));

		metaState = MetaKeyKeyListener.adjustMetaAfterKeypress(metaState);
		if (unicodeChar == 0 || !keyboardInput.isInputConnected()) {
			return false;
		}

		if ((unicodeChar & KeyCharacterMap.COMBINING_ACCENT) != 0) {
			unicodeChar = unicodeChar & KeyCharacterMap.COMBINING_ACCENT_MASK;
		}

		unicodeChar = keyboardInput.translateKeyDown(unicodeChar);

		onKey(unicodeChar, null);

		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// If we want to do transformations on text being entered with a hard
		// keyboard, we need to process the up events to update the meta key
		// state we are tracking.
		if (PROCESS_HARD_KEYS) {
			if (state.isPrediction()) {
				metaState = MetaKeyKeyListener.handleKeyUp(metaState, keyCode, event);
			}
		}

        return false;
	}

	/**
	 * Helper to send a character to the editor as raw key events.
	 */
	public void sendKey(int keyCode) {
		switch (keyCode) {
			case '\n':
				keyDownUp(KeyEvent.KEYCODE_ENTER);
				break;
			default:
				if (keyCode >= '0' && keyCode <= '9') {
					keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
				} else {
                    keyboardInput.commitText(String.valueOf((char) keyCode), 1);
				}
				break;
		}
	}

	public void pickDefaultCandidate() {
        if (state.isCompletion()) {
            pickSuggestionManually(0);
        }
	}

	public void pickSuggestionManually(int index) {
	}

	protected void handleCharacter(int primaryCode, int[] keyCodes) {
		if (inputMethodService.isInputViewShown()) {
			if (state.isShifted()) {
				primaryCode = Character.toUpperCase(primaryCode);
			}
		}

		if (isAlphabet(primaryCode) && state.isPrediction()) {
			keyboardInput.append((char)primaryCode);
			updateShiftKeyState(keyboardInput.getCurrentInputEditorInfo());
			updateCandidates();
		} else {
			keyboardInput.commitText(String.valueOf((char) primaryCode), 1);
		}
	}

	/**
	 * Helper to determine if a given character code is alphabetic.
	 */
	private boolean isAlphabet(int code) {
		return Character.isLetter(code);
	}

	@Override
	public void onCurrentInputMethodSubtypeChanged(@Nonnull InputMethodSubtype subtype) {
		keyboardView.setSubtypeOnSpaceKey(subtype);
	}

    public boolean isWordSeparator(int code) {
        final String separators = configuration.getWordSeparators();
        return separators.contains(String.valueOf((char) code));
    }
}
