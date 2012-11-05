package org.solovyev.android.keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.inputmethod.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.history.HistoryHelper;
import org.solovyev.common.history.SimpleHistoryHelper;
import org.solovyev.common.text.StringUtils;

/**
 * User: Solovyev_S
 * Date: 02.11.12
 * Time: 12:24
 */
public class DefaultKeyboardInput implements AKeyboardInput {

    public static final int MAX_INT = Integer.MAX_VALUE / 2 - 1;

    @NotNull
    private final HistoryHelper<KeyboardInputHistoryState> history = new SimpleHistoryHelper<KeyboardInputHistoryState>(10);
    {
        history.addState(new KeyboardInputHistoryState("", 0));
    }

    @NotNull
    private final InputMethodService inputMethodService;

    @NotNull
    private final StringBuilder typedText = new StringBuilder(100);

    public DefaultKeyboardInput(@NotNull InputMethodService inputMethodService) {
        this.inputMethodService = inputMethodService;
    }

    @Override
    public void commitTyped() {
        if (typedText.length() > 0) {
            commitText(typedText, typedText.length());
            clearTypedText();
        }
    }

    @Override
    public void onText(@Nullable CharSequence text) {
        final InputConnection ic = getCurrentInputConnection();
        ic.beginBatchEdit();
        commitTyped();
        commitText(ic, text, 0);
        ic.endBatchEdit();
    }

    public void commitText(@Nullable CharSequence text, int position) {
        final InputConnection ic = getCurrentInputConnection();
        commitText(ic, text, position);
    }

    @Override
    public void commitText(@Nullable String text, int position) {
        final InputConnection ic = getCurrentInputConnection();
        commitText(ic, text, position);
    }

    private void commitText(@NotNull InputConnection ic, @Nullable CharSequence text, int position) {
        ic.commitText(text, position);
        if (!StringUtils.isEmpty(text)) {
            history.addState(new KeyboardInputHistoryState(AndroidKeyboardUtils.getTextFromInputConnection(ic), 0));
        }
    }

    @NotNull
    @Override
    public EditorInfo getCurrentInputEditorInfo() {
        return inputMethodService.getCurrentInputEditorInfo();
    }

    @NotNull
    private InputConnection getCurrentInputConnection() {
        InputConnection result = this.inputMethodService.getCurrentInputConnection();
        if (result == null ) {
            result = NoInputConnection.getInstance();
        }
        return result;
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
    public void undo() {
        if (this.history.isUndoAvailable()) {
            final KeyboardInputHistoryState state = this.history.undo(null);
            restoreFromHistory(state);
        }
    }

    private void restoreFromHistory(@Nullable KeyboardInputHistoryState state) {
        if (state != null) {
            final InputConnection ic = getCurrentInputConnection();
            ic.deleteSurroundingText(MAX_INT, MAX_INT);
            ic.commitText(state.getCharSequence(), 1);
        }
    }

    @Override
    public void redo() {
        if (this.history.isRedoAvailable()) {
            final KeyboardInputHistoryState state = this.history.redo(null);
            restoreFromHistory(state);
        }
    }

    @Override
    public boolean handleBackspace() {
        boolean changed = false;

        int length = typedText.length();

        final InputConnection ic = getCurrentInputConnection();
        if (length > 1) {
            typedText.delete(length - 1, length);
            ic.setComposingText(typedText, 1);
            changed = true;
        } else if (length > 0) {
            clearTypedText();
            commitText(ic, "", 0);
            changed = true;
        }

        return changed;
    }

    @Override
    public void sendKeyEvent(@NotNull KeyEvent keyEvent) {
        getCurrentInputConnection().sendKeyEvent(keyEvent);
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
        getCurrentInputConnection().commitCompletion(completionInfo);
    }

    @Override
    public void append(char primaryCode) {
        typedText.append(primaryCode);
        getCurrentInputConnection().setComposingText(typedText, 1);
    }

    @Override
    public void handleCursorRight() {
        final InputConnection ic = getCurrentInputConnection();
        int selectionStart = getSelectionStart(ic);
        int selectionEnd = getSelectionEnd(ic, selectionStart);
        if (selectionStart > 0) {
            selectionStart = selectionStart - 1;
            ic.setSelection(selectionStart, selectionEnd);
        }
    }

    private int getSelectionEnd(@NotNull InputConnection ic, int selectionStart) {
        final CharSequence selectedText = ic.getSelectedText(0);
        return selectionStart + (selectedText == null ? 0 : selectedText.length());
    }

    private int getSelectionStart(@NotNull InputConnection ic) {
        return ic.getTextBeforeCursor(MAX_INT, 0).length();
    }

    @Override
    public void handleCursorLeft() {
        final InputConnection ic = getCurrentInputConnection();
        int selectionStart = getSelectionStart(ic);
        int selectionEnd = getSelectionEnd(ic, selectionStart);
        if (selectionStart < 0) {
            selectionStart = selectionStart - 1;
            ic.setSelection(selectionStart, selectionEnd);
        }
    }

    @Override
    public void handleClear() {
        typedText.setLength(0);
        final InputConnection ic = getCurrentInputConnection();
        ic.setSelection(0, 0);
        ic.deleteSurroundingText(MAX_INT, MAX_INT);

    }

    @Override
    public void handlePaste() {
        final ClipboardManager clipboardManager = (ClipboardManager) inputMethodService.getSystemService(Context.CLIPBOARD_SERVICE);
        final CharSequence text = clipboardManager.getText();
        if (!StringUtils.isEmpty(text)) {
            commitText(text, 1);
        }
    }

    @Override
    public void handleCopy() {
        final InputConnection ic = getCurrentInputConnection();
        AndroidKeyboardUtils.copyTextFromInputConnection(ic, inputMethodService);
    }

    @Override
    public void clearMetaKeyStates(int flags) {
        getCurrentInputConnection().clearMetaKeyStates(flags);
    }

    @Override
    public void keyDownUp(int keyEventCode) {
        sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    @Override
    public void finishComposingText() {
        getCurrentInputConnection().finishComposingText();
    }

    @Override
    public boolean isInputConnected() {
        return this.inputMethodService.getCurrentInputConnection() != null;
    }

    @Override
    public int getCursorCapsMode(int inputType) {
        return getCurrentInputConnection().getCursorCapsMode(inputType);
    }
    
    /*
    **********************************************************************
    *
    *                           STATIC
    *
    **********************************************************************
    */
    
    private static final class NoInputConnection implements InputConnection {

        @NotNull
        private static final InputConnection instance = new NoInputConnection();

        @NotNull
        public static InputConnection getInstance() {
            return instance;
        }

        private NoInputConnection() {
        }

        @Override
        public CharSequence getTextBeforeCursor(int n, int flags) {
            return "";
        }

        @Override
        public CharSequence getTextAfterCursor(int n, int flags) {
            return "";
        }

        @Override
        public CharSequence getSelectedText(int flags) {
            return "";
        }

        @Override
        public int getCursorCapsMode(int reqModes) {
            return 0;
        }

        @Override
        public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
            return new ExtractedText();
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            return false;
        }

        @Override
        public boolean setComposingText(CharSequence text, int newCursorPosition) {
            return false;
        }

        @Override
        public boolean setComposingRegion(int start, int end) {
            return false;
        }

        @Override
        public boolean finishComposingText() {
            return false;
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            return false;
        }

        @Override
        public boolean commitCompletion(CompletionInfo text) {
            return false;
        }

        @Override
        public boolean commitCorrection(CorrectionInfo correctionInfo) {
            return false;
        }

        @Override
        public boolean setSelection(int start, int end) {
            return false;
        }

        @Override
        public boolean performEditorAction(int editorAction) {
            return false;
        }

        @Override
        public boolean performContextMenuAction(int id) {
            return false;
        }

        @Override
        public boolean beginBatchEdit() {
            return false;
        }

        @Override
        public boolean endBatchEdit() {
            return false;
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return false;
        }

        @Override
        public boolean clearMetaKeyStates(int states) {
            return false;
        }

        @Override
        public boolean reportFullscreenMode(boolean enabled) {
            return false;
        }

        @Override
        public boolean performPrivateCommand(String action, Bundle data) {
            return false;
        }
    }
}
