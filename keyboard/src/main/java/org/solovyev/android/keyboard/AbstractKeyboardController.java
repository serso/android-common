package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.text.InputType;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 11:33
 */
public abstract class AbstractKeyboardController implements AKeyboardController {

	/**
	 * This boolean indicates the optional example code for performing
	 * processing of hard keys in addition to regular text generation
	 * from on-screen interaction.  It would be used for input methods that
	 * perform language translations (such as converting text entered on
	 * a QWERTY keyboard to Chinese), but may not be used for input methods
	 * that are primarily intended to be used for on-screen text entry.
	 */
	private static final boolean PROCESS_HARD_KEYS = true;

	@NotNull
	private AKeyboardControllerState state = AKeyboardControllerStateImpl.newDefaultState();

	@NotNull
	private AKeyboardView keyboardView = new DummyAKeyboardView();

	@NotNull
	private AKeyboardInput keyboardInput;

	@NotNull
	private InputMethodService inputMethodService;

	private long metaState;

	@NotNull
	private InputMethodManager inputMethodManager;

	@Override
	public void onInitializeInterface(@NotNull InputMethodService inputMethodService) {
		this.inputMethodService = inputMethodService;
		this.keyboardInput = new DefaultKeyboardInput(inputMethodService);
	}

	@NotNull
	protected InputMethodService getInputMethodService() {
		return inputMethodService;
	}

	@NotNull
	@Override
	public AKeyboard getCurrentKeyboard() {
		return state.getKeyboard();
	}

	protected void setCurrentKeyboard(@NotNull AKeyboard keyboard) {
		this.state = this.state.copyForNewKeyboard(keyboard);
		this.keyboardView.setKeyboard(state.getKeyboard().getKeyboard());
	}

	@Override
	@NotNull
	public AKeyboardControllerState getState() {
		return state;
	}

	protected void setState(@NotNull AKeyboardControllerState state) {
		this.state = state;
	}

	@Override
	@NotNull
	public AKeyboardView getKeyboardView() {
		return keyboardView;
	}

	@Override
	@NotNull
	public AKeyboardInput getKeyboardInput() {
		return keyboardInput;
	}

	@Override
	public void handleClose() {
		keyboardInput.commitTyped();

		updateCandidates();

		inputMethodService.requestHideSelf(0);
		keyboardView.closing();
	}

	@Override
	public void onCreate(@NotNull Context context) {
		inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public void onStartInput(@NotNull EditorInfo attribute, boolean restarting) {
		if (!restarting) {
			// Clear shift states.
			metaState = 0;
		}

		this.state = onStartInput0(attribute, restarting);

		keyboardInput.clear();

		updateCandidates();
		keyboardView.setCompletions(Collections.<CompletionInfo>emptyList());

		// Update the label on the enter key, depending on what the application
		// says it will do.
		getCurrentKeyboard().setImeOptions(inputMethodService.getResources(), attribute.imeOptions);
	}

	@Override
	public void onStartInputView(EditorInfo attribute, boolean restarting) {
		// Apply the selected keyboard to the input view.
		keyboardView.setKeyboard(state.getKeyboard().getKeyboard());
		keyboardView.closing();
		final InputMethodSubtype subtype = inputMethodManager.getCurrentInputMethodSubtype();
		keyboardView.setSubtypeOnSpaceKey(subtype);
	}

	@NotNull
	@Override
	public final AKeyboardView createKeyboardView(@NotNull Context context, @NotNull LayoutInflater layoutInflater) {
		keyboardView = createKeyboardView0(context, layoutInflater);
		keyboardView.setKeyboard(state.getKeyboard().getKeyboard());
		keyboardView.setOnKeyboardActionListener(new DefaultKeyboardActionListener(this));
		return keyboardView;
	}

	@NotNull
	protected abstract AKeyboardView createKeyboardView0(@NotNull Context context, @NotNull LayoutInflater layoutInflater);

	@NotNull
	public abstract AKeyboardControllerState onStartInput0(@NotNull EditorInfo attribute, boolean restarting);

	@Override
	public void onText(@Nullable CharSequence text) {
		keyboardInput.onText(text);

		updateShiftKeyState(keyboardInput.getCurrentInputEditorInfo());
	}

	@Override
	public void onDisplayCompletions(@Nullable CompletionInfo[] completions) {
		if (state.isCompletion()) {

			if (completions == null) {
				setSuggestions(Collections.<String>emptyList(), false, false);
			} else {
				final List<String> suggestions = new ArrayList<String>();
				for (CompletionInfo completion : Arrays.asList(completions)) {
					if (completion != null) {
						suggestions.add(completion.getText().toString());
					}
				}

				setSuggestions(suggestions, true, true);
			}
		}
	}

	/**
	 * Update the list of available candidates from the current composing
	 * text.  This will need to be filled in by however you are determining
	 * candidates.
	 */
	public void updateCandidates() {
		if (!state.isCompletion()) {
			final CharSequence text = keyboardInput.getText();
			if (!StringUtils.isEmpty(text)) {
				final List<String> list = new ArrayList<String>();
				list.add(text.toString());
				setSuggestions(list, true, true);
			} else {
				setSuggestions(Collections.<String>emptyList(), false, false);
			}
		}
	}

	public void setSuggestions(@NotNull List<String> suggestions,
							   boolean completions,
							   boolean typedWordValid) {
		if (suggestions.size() > 0) {
			keyboardView.setCandidatesViewShown(true);
		} else if (keyboardView.isExtractViewShown()) {
			keyboardView.setCandidatesViewShown(true);
		}

		keyboardView.setSuggestions(suggestions, completions, typedWordValid);
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
	public void onFinishInput() {
		keyboardInput.clear();
		updateCandidates();
		keyboardView.closing();
	}

	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
		// If the current selection in the text view changes, we should
		// clear whatever candidate text we have.
		final CharSequence text = keyboardInput.getText();
		if (!StringUtils.isEmpty(text) && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
			keyboardInput.clear();
			updateCandidates();
			final InputConnection ic = keyboardInput.getCurrentInputConnection();
			if (ic != null) {
				ic.finishComposingText();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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
				final CharSequence text = keyboardInput.getText();
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
					if (keyCode == KeyEvent.KEYCODE_SPACE
							&& (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
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

	public void handleBackspace() {
		if ( keyboardInput.handleBackspace() ) {
			updateCandidates();
		} else {
			keyDownUp(KeyEvent.KEYCODE_DEL);
		}

		updateShiftKeyState(keyboardInput.getCurrentInputEditorInfo());
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
	private boolean translateKeyDown(int keyCode, KeyEvent event) {
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
		pickSuggestionManually(0);
	}

	public void pickSuggestionManually(int index) {
		final List<CompletionInfo> completions = keyboardView.getCompletions();

		final CharSequence text = keyboardInput.getText();

		if (state.isCompletion() && index >= 0 && index < completions.size()) {
			final CompletionInfo ci = completions.get(index);
			keyboardInput.commitCompletion(ci);
			keyboardView.clearCandidateView();
			updateShiftKeyState(keyboardInput.getCurrentInputEditorInfo());
		} else if (!StringUtils.isEmpty(text)) {
			// If we were generating candidate suggestions for the current
			// text, we would commit one of them here.  But for this sample,
			// we will just commit the current text.
			keyboardInput.commitTyped();
		}
	}

	public void handleCharacter(int primaryCode, int[] keyCodes) {
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

	@Override
	public View onCreateCandidatesView() {
		return this.keyboardView.onCreateCandidatesView();
	}
}
