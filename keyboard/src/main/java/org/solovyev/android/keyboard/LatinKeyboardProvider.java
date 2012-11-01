package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
* User: serso
* Date: 11/1/12
* Time: 9:22 PM
*/
final class LatinKeyboardProvider extends MapKeyboardProvider {

    @NotNull
    private AKeyboard qwertyKeyboard;

    @NotNull
    private AKeyboard symbolsKeyboard;

    @NotNull
    private AKeyboard symbolsShiftedKeyboard;

    @NotNull
    @Override
    protected List<AKeyboard> createKeyboard(@NotNull Context context) {
        final List<AKeyboard> result = new ArrayList<AKeyboard>();

        qwertyKeyboard = AKeyboardImpl.fromResource(context, R.xml.qwerty);
        symbolsKeyboard = AKeyboardImpl.fromResource(context, R.xml.symbols);
        symbolsShiftedKeyboard = AKeyboardImpl.fromResource(context, R.xml.symbols_shift);

        result.add(qwertyKeyboard);
        result.add(symbolsKeyboard);
        result.add(symbolsShiftedKeyboard);

        return result;
    }

    @NotNull
    @Override
    protected String getDefaultKeyboardId() {
        return qwertyKeyboard.getKeyboardId();
    }

    @NotNull
    @Override
    public InputMethodServiceState onStartInput(@NotNull EditorInfo attribute, boolean restarting) {
        final InputMethodServiceInterface imsi = getInputMethodServiceInterface();

        final InputMethodServiceState result;

        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                setCurrentKeyboard(symbolsKeyboard.getKeyboardId());
                result = InputMethodServiceStateImpl.newDefaultState();
                break;

            case InputType.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                setCurrentKeyboard(symbolsKeyboard.getKeyboardId());
                result = InputMethodServiceStateImpl.newDefaultState();
                break;

            case InputType.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                setCurrentKeyboard(qwertyKeyboard.getKeyboardId());
                boolean prediction = true;
                boolean completion = false;

                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                    prediction = false;
                }

                if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == InputType.TYPE_TEXT_VARIATION_URI
                        || variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    prediction = false;
                }

                if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own.  We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    prediction = false;
                    completion = getInputMethodService().isFullscreenMode();
                }

                result = InputMethodServiceStateImpl.newInstance(prediction, completion);

                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
                imsi.updateShiftKeyState(attribute);
                break;

            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
                setCurrentKeyboard(qwertyKeyboard.getKeyboardId());
                imsi.updateShiftKeyState(attribute);
                result = InputMethodServiceStateImpl.newDefaultState();
        }

        return result;
    }

    @Override
    public void onFinishInput() {
        setCurrentKeyboard(qwertyKeyboard.getKeyboardId());
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        final InputMethodServiceInterface imsi = getInputMethodServiceInterface();
        final KeyboardView keyboardView = imsi.getKeyboardView();

        if (imsi.isWordSeparator(primaryCode)) {
            // Handle separator
            if (imsi.getText().length() > 0) {
                imsi.commitTyped(getInputMethodService().getCurrentInputConnection());
            }
            imsi.sendKey(primaryCode);
            imsi.updateShiftKeyState(getInputMethodService().getCurrentInputEditorInfo());
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            imsi.handleBackspace();
        } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            imsi.handleShift();
        } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
            imsi.handleClose();
        } else if (primaryCode == AKeyboardView.KEYCODE_OPTIONS) {
            // Show a menu or somethin'
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
            Keyboard current = keyboardView.getKeyboard();
            if (current == symbolsKeyboard || current == symbolsShiftedKeyboard) {
                current = qwertyKeyboard.getKeyboard();
            } else {
                current = symbolsKeyboard.getKeyboard();
            }
            keyboardView.setKeyboard(current);
            if (current == symbolsKeyboard) {
                current.setShifted(false);
            }
        } else {
            imsi.handleCharacter(primaryCode, keyCodes);
        }
    }

    @Override
    public void onCreate(@NotNull Context context) {
    }
}
