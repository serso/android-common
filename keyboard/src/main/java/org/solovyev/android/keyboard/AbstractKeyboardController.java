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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.StringUtils;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 11:33
 */
public abstract class AbstractKeyboardController<KD extends AKeyboardDef> implements AKeyboardController {

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

    protected static final int KEYCODE_CLEAR = -800;
    protected static final int KEYCODE_COPY = -801;
    protected static final int KEYCODE_ENTER = 10;
    protected static final int KEYCODE_PASTE = -802;
    protected static final int KEYCODE_CURSOR_LEFT = -803;
    protected static final int KEYCODE_CURSOR_RIGHT = -804;

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

	@NotNull
	private AKeyboardControllerState<KD> state;

	@NotNull
	private AKeyboardView<KD> keyboardView;

	@NotNull
	private AKeyboardInput keyboardInput;

	@NotNull
	private InputMethodService inputMethodService;

	private long metaState;

	@NotNull
	private InputMethodManager inputMethodManager;

    @NotNull
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
    public final void onCreate(@NotNull Context context) {
        this.inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.configuration = onCreate0(context);
    }

    @NotNull
    protected abstract AKeyboardConfiguration onCreate0(@NotNull Context context);

    @Override
	public final void onInitializeInterface(@NotNull InputMethodService inputMethodService) {
		this.inputMethodService = inputMethodService;
		this.keyboardInput = new DefaultKeyboardInput(inputMethodService);
        this.keyboardView = createKeyboardView0(inputMethodService);
        this.state = onInitializeInterface0(inputMethodService);
    }

    @NotNull
    protected abstract AKeyboardControllerState<KD> onInitializeInterface0(@NotNull InputMethodService inputMethodService);

    @NotNull
    @Override
    public final AKeyboardView createKeyboardView(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
        keyboardView.createAndroidKeyboardView(context, layoutInflater);
        keyboardView.setKeyboard(getCurrentKeyboard().getKeyboard());
        keyboardView.setOnKeyboardActionListener(new DefaultKeyboardActionListener(this));
        return keyboardView;
    }

    @NotNull
    protected abstract AKeyboardView<KD> createKeyboardView0(@NotNull Context context);

    @Override
    public View onCreateCandidatesView() {
        return null;
    }

    @Override
    public void onStartInput(@NotNull EditorInfo attribute, boolean restarting) {
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
        keyboardView.closing();
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
        keyboardView.setKeyboard(getCurrentKeyboard().getKeyboard());
        keyboardView.closing();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final InputMethodSubtype subtype = inputMethodManager.getCurrentInputMethodSubtype();
            keyboardView.setSubtypeOnSpaceKey(subtype);
        }
    }

	@NotNull
	protected InputMethodService getInputMethodService() {
		return inputMethodService;
	}

    @Override
    public final boolean onKey(int primaryCode, int[] keyCodes) {
        boolean consumed = handleSpecialKey(primaryCode);

        if ( !consumed ) {

            if (isWordSeparator(primaryCode)) {
                // Handle separator
                if (!StringUtils.isEmpty(getKeyboardInput().getTypedText())) {
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
        }

        return consumed;
    }

    protected abstract void handleShift();


    @NotNull
	protected AKeyboard<? extends KD> getCurrentKeyboard() {
		return state.getKeyboard();
	}

	protected void setCurrentKeyboard(@NotNull AKeyboard<? extends KD> keyboard) {
		this.state = this.state.copyForNewKeyboard(keyboard);
		this.keyboardView.setKeyboard(keyboard.getKeyboard());
	}

	@NotNull
	protected AKeyboardControllerState<KD> getState() {
		return state;
	}

	protected void setState(@NotNull AKeyboardControllerState<KD> state) {
		this.state = state;
	}

	@NotNull
    protected AKeyboardView<KD> getKeyboardView() {
		return keyboardView;
	}

	@NotNull
	protected AKeyboardInput getKeyboardInput() {
		return keyboardInput;
	}

	@Override
	public void handleClose() {
		keyboardInput.commitTyped();
		inputMethodService.requestHideSelf(0);
		keyboardView.closing();
	}

	@NotNull
	public abstract AKeyboardControllerState<KD> onStartInput0(@NotNull EditorInfo attribute, boolean restarting);

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
				final InputConnection currentInputConnection = keyboardInput.getCurrentInputConnection();
				if (currentInputConnection != null) {
					caps = currentInputConnection.getCursorCapsMode(attr.inputType);
				}
			}

			keyboardView.setShifted(state.isCapsLock() || caps != 0);
		}
	}

	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
		// If the current selection in the text view changes, we should
		// clear whatever candidate text we have.
		final CharSequence text = keyboardInput.getTypedText();
		if (!StringUtils.isEmpty(text) && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
			keyboardInput.clearTypedText();
			updateCandidates();
			final InputConnection ic = keyboardInput.getCurrentInputConnection();
			if (ic != null) {
				ic.finishComposingText();
			}
		}
	}

    protected void updateCandidates() {
    }

    @Override
	public boolean onKeyDown(int keyCode, @NotNull KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				// The InputMethodService already takes care of the back
				// key for us, to dismiss the input method if it is shown.
				// However, our keyboard could be showing a pop-up window
				// that back should dismiss, so we first allow it to do that.
				if (event.getRepeatCount() == 0) {
					if (keyboardView.handleBack()) {
						return true;
					}
				}
				break;

			case KeyEvent.KEYCODE_DEL:
				// Special handling of the delete key: if we currently are
				// composing text for the user, we want to modify that instead
				// of let the application to the delete itself.
				final CharSequence text = keyboardInput.getTypedText();
				if (!StringUtils.isEmpty(text)) {
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
						InputConnection ic = keyboardInput.getCurrentInputConnection();
						if (ic != null) {
							// First, tell the editor that it is no longer in the
							// shift state, since we are consuming this.
							ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
							keyDownUp(KeyEvent.KEYCODE_A);
							keyDownUp(KeyEvent.KEYCODE_N);
							keyDownUp(KeyEvent.KEYCODE_D);
							keyDownUp(KeyEvent.KEYCODE_R);
							keyDownUp(KeyEvent.KEYCODE_O);
							keyDownUp(KeyEvent.KEYCODE_I);
							keyDownUp(KeyEvent.KEYCODE_D);
							// And we consume this event.
							return true;
						}
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
		keyboardInput.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
		keyboardInput.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
	}

	/**
	 * This translates incoming hard key events in to edit operations on an
	 * InputConnection.  It is only needed when using the
	 * PROCESS_HARD_KEYS option.
	 */
	private boolean translateKeyDown(int keyCode, @NotNull KeyEvent event) {
		metaState = MetaKeyKeyListener.handleKeyDown(metaState, keyCode, event);
		int unicodeChar = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(metaState));

		metaState = MetaKeyKeyListener.adjustMetaAfterKeypress(metaState);
		InputConnection ic = keyboardInput.getCurrentInputConnection();
		if (unicodeChar == 0 || ic == null) {
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
	public void onKeyUp(int keyCode, KeyEvent event) {
		// If we want to do transformations on text being entered with a hard
		// keyboard, we need to process the up events to update the meta key
		// state we are tracking.
		if (PROCESS_HARD_KEYS) {
			if (state.isPrediction()) {
				metaState = MetaKeyKeyListener.handleKeyUp(metaState, keyCode, event);
			}
		}
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
					final InputConnection inputConnection = keyboardInput.getCurrentInputConnection();
					if (inputConnection != null) {
						inputConnection.commitText(String.valueOf((char) keyCode), 1);
					}
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
			if (keyboardView.isShifted()) {
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
	public void onCurrentInputMethodSubtypeChanged(@NotNull InputMethodSubtype subtype) {
		keyboardView.setSubtypeOnSpaceKey(subtype);
	}

    public boolean isWordSeparator(int code) {
        final String separators = configuration.getWordSeparators();
        return separators.contains(String.valueOf((char) code));
    }
}
