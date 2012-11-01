/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.solovyev.android.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of writing an input method for a soft keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public abstract class AbstractInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener, InputMethodServiceInterface {

    /*
    **********************************************************************
    *
    *                           CONSTANTS
    *
    **********************************************************************
    */
    private static final boolean DEBUG = false;

    /**
     * This boolean indicates the optional example code for performing
     * processing of hard keys in addition to regular text generation
     * from on-screen interaction.  It would be used for input methods that
     * perform language translations (such as converting text entered on
     * a QWERTY keyboard to Chinese), but may not be used for input methods
     * that are primarily intended to be used for on-screen text entry.
     */
    private static final boolean PROCESS_HARD_KEYS = true;

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

    @NotNull
    private InputMethodManager inputMethodManager;

    @NotNull
    private AKeyboardView keyboardView;

    @NotNull
    private CandidateView candidateView;

    @Nullable
    private List<CompletionInfo> completions;

    @NotNull
    private final StringBuilder text = new StringBuilder();

    @NotNull
    private InputMethodServiceState state = InputMethodServiceStateImpl.newDefaultState();

    private int lastDisplayWidth;

    @NotNull
    private String wordSeparators;

    private long lastShiftTime;
    private long metaState;

    @NotNull
    private final AKeyboardProvider keyboardProvider;

    protected AbstractInputMethodService(@NotNull AKeyboardProvider keyboardProvider) {
        this.keyboardProvider = keyboardProvider;
    }

    /**
     * Main initialization of the input method component.  Be sure to call
     * to super class.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        wordSeparators = this.getResources().getString(R.string.word_separators);

        keyboardProvider.onCreate(this);
    }

    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override
    public void onInitializeInterface() {
        /*if (qwertyKeyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == lastDisplayWidth) return;
            lastDisplayWidth = displayWidth;
        }*/

        keyboardProvider.onInitializeInterface(this);
    }

    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    @Override
    public View onCreateInputView() {
        keyboardView = keyboardProvider.createKeyboardView(this,  getLayoutInflater());
        keyboardView.setOnKeyboardActionListener(this);
        keyboardView.setKeyboard(keyboardProvider.getCurrentKeyboard().getKeyboard());
        return keyboardView.getKeyboardView();
    }

    /**
     * Called by the framework when your view for showing candidates needs to
     * be generated, like {@link #onCreateInputView}.
     */
    @Override
    public View onCreateCandidatesView() {
        candidateView = new CandidateView(this);
        candidateView.setService(this);
        return candidateView;
    }

    /**
     * This is the main point where we do our initialization of the input method
     * to begin operating on an application.  At this point we have been
     * bound to the client, and are now receiving all of the detailed information
     * about the target of our edits.
     */
    @Override
    public void onStartInput(@NotNull EditorInfo attribute,
                             boolean restarting) {
        super.onStartInput(attribute, restarting);

        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        text.setLength(0);
        updateCandidates();

        if (!restarting) {
            // Clear shift states.
            metaState = 0;
        }

        this.state = InputMethodServiceStateImpl.newDefaultState();
        completions = null;

        this.state = this.keyboardProvider.onStartInput(attribute, restarting);

        // Update the label on the enter key, depending on what the application
        // says it will do.
        this.keyboardProvider.getCurrentKeyboard().setImeOptions(getResources(), attribute.imeOptions);
    }

    /**
     * This is called when the user is done editing a field.  We can use
     * this to reset our state.
     */
    @Override
    public void onFinishInput() {
        super.onFinishInput();

        // Clear current composing text and candidates.
        text.setLength(0);
        updateCandidates();

        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

        keyboardProvider.onFinishInput();

        keyboardView.closing();
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input view.
        keyboardView.setKeyboard(keyboardProvider.getCurrentKeyboard().getKeyboard());
        keyboardView.closing();
        final InputMethodSubtype subtype = inputMethodManager.getCurrentInputMethodSubtype();
        keyboardView.setSubtypeOnSpaceKey(subtype);
    }

    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {
        keyboardView.setSubtypeOnSpaceKey(subtype);
    }

    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd,
                                  int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (text.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            text.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * This tells us about completions that the editor has determined based
     * on the current text in it.  We want to use this in fullscreen mode
     * to show the completions ourself, since the editor can not be seen
     * in that situation.
     */
    @Override
    public void onDisplayCompletions(@Nullable CompletionInfo[] completions) {
        if (state.isCompletion()) {

            if (completions == null) {
                this.completions = null;
                setSuggestions(null, false, false);
            } else {
                this.completions = Arrays.asList(completions);

                final List<String> suggestions = new ArrayList<String>();
                for (CompletionInfo completion : completions) {
                    if (completion != null) {
                        suggestions.add(completion.getText().toString());
                    }
                }

                setSuggestions(suggestions, true, true);
            }
        }
    }

    /**
     * This translates incoming hard key events in to edit operations on an
     * InputConnection.  It is only needed when using the
     * PROCESS_HARD_KEYS option.
     */
    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        metaState = MetaKeyKeyListener.handleKeyDown(metaState,
                keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(metaState));
        metaState = MetaKeyKeyListener.adjustMetaAfterKeypress(metaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }

        if (text.length() > 0) {
            char accent = text.charAt(text.length() - 1);
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                text.setLength(text.length() - 1);
            }
        }

        onKey(c, null);

        return true;
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
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
                if (text.length() > 0) {
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
                        InputConnection ic = getCurrentInputConnection();
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

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with a hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (state.isPrediction()) {
                metaState = MetaKeyKeyListener.handleKeyUp(metaState,
                        keyCode, event);
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * Helper function to commit any text being composed in to the editor.
     */
    @Override
    public void commitTyped(@NotNull InputConnection inputConnection) {
        if (text.length() > 0) {
            inputConnection.commitText(text, text.length());
            text.setLength(0);
            updateCandidates();
        }
    }


    /**
     * Helper to determine if a given character code is alphabetic.
     */
    private boolean isAlphabet(int code) {
        return Character.isLetter(code);
    }

    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    /**
     * Helper to send a character to the editor as raw key events.
     */
    @Override
    public void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    @Override
    @NotNull
    public StringBuilder getText() {
        return text;
    }

    // Implementation of KeyboardViewListener

    public void onKey(int primaryCode, @Nullable int[] keyCodes) {
        keyboardProvider.onKey(primaryCode, keyCodes);
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        if (this.text.length() > 0) {
            commitTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void updateCandidates() {
        if (!state.isCompletion()) {
            if (text.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(text.toString());
                setSuggestions(list, true, true);
            } else {
                setSuggestions(null, false, false);
            }
        }
    }

    public void setSuggestions(@Nullable List<String> suggestions,
                               boolean completions,
                               boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        candidateView.setSuggestions(suggestions, completions, typedWordValid);
    }

    public void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        keyboardView.closing();
    }

/*    private void checkToggleCapsLock() {
        long now = System.currentTimeMillis();
        if (lastShiftTime + 800 > now) {
            capsLock = !capsLock;
            lastShiftTime = 0;
        } else {
            lastShiftTime = now;
        }
    }*/

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    public void pickSuggestionManually(int index) {
        if (state.isCompletion() && completions != null && index >= 0 && index < completions.size()) {
            CompletionInfo ci = completions.get(index);
            getCurrentInputConnection().commitCompletion(ci);
            candidateView.clear();
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (text.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            commitTyped(getCurrentInputConnection());
        }
    }

    public void swipeRight() {
        if (state.isCompletion()) {
            pickDefaultCandidate();
        }
    }

    public void swipeLeft() {
        handleBackspace();
    }

    @Override
    public void handleBackspace() {
        final int length = text.length();
        if (length > 1) {
            text.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(text, 1);
            updateCandidates();
        } else if (length > 0) {
            text.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }

        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {
    }

    public void onRelease(int primaryCode) {
    }

    @NotNull
    @Override
    public AKeyboardView getKeyboardView() {
        return keyboardView;
    }

    public void handleShift() {

        /*Keyboard currentKeyboard = keyboardView.getKeyboard();
        if (qwertyKeyboard == currentKeyboard) {
            // Alphabet keyboard
            checkToggleCapsLock();
            keyboardView.setShifted(capsLock || !keyboardView.isShifted());
        } else if (currentKeyboard == symbolsKeyboard) {
            symbolsKeyboard.setShifted(true);
            keyboardView.setKeyboard(symbolsShiftedKeyboard.getKeyboard());
            symbolsShiftedKeyboard.setShifted(true);
        } else if (currentKeyboard == symbolsShiftedKeyboard) {
            symbolsShiftedKeyboard.setShifted(false);
            keyboardView.setKeyboard(symbolsKeyboard.getKeyboard());
            symbolsKeyboard.setShifted(false);
        }*/
    }

    @Override
    public void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isInputViewShown()) {
            if (keyboardView.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
            }
        }
        if (isAlphabet(primaryCode) && state.isPrediction()) {
            text.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(text, 1);
            updateShiftKeyState(getCurrentInputEditorInfo());
            updateCandidates();
        } else {
            getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
        }
    }

    @NotNull
    private String getWordSeparators() {
        return wordSeparators;
    }

    public boolean isWordSeparator(int code) {
        final String separators = getWordSeparators();
        return separators.contains(String.valueOf((char) code));
    }

    @Override
    @NotNull
    public InputMethodServiceState getState() {
        return state;
    }

    /**
     * Helper to update the shift state of our keyboard based on the initial
     * editor state.
     */
    @Override
    public void updateShiftKeyState(@Nullable EditorInfo attr) {
        if (attr != null) {
            int caps = 0;
            EditorInfo ei = this.getCurrentInputEditorInfo();
            if (ei != null && ei.inputType != InputType.TYPE_NULL) {
                caps = this.getCurrentInputConnection().getCursorCapsMode(attr.inputType);
            }
            keyboardView.setShifted(state.isCapsLock() || caps != 0);
        }
    }
}
