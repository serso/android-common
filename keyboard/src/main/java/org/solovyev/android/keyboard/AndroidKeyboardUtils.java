package org.solovyev.android.keyboard;

import android.content.Context;
import android.os.Build;
import android.text.ClipboardManager;
import android.view.inputmethod.InputConnection;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.text.Strings;

/**
 * User: serso
 * Date: 11/5/12
 * Time: 1:12 PM
 */
public final class AndroidKeyboardUtils {

    private AndroidKeyboardUtils() {
        throw new AssertionError();
    }

    public static void copyWholeTextFromInputConnection(@NotNull InputConnection ic, @NotNull Context context) {
        String text = getTextFromInputConnection(ic);

        if (!Strings.isEmpty(text)) {
            final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        }
    }

    @NotNull
    public static String getTextFromInputConnection(@NotNull InputConnection ic) {
        final CharSequence textAfter = ic.getTextAfterCursor(DefaultKeyboardInput.MAX_INT, 0);
        final CharSequence textBefore = ic.getTextBeforeCursor(DefaultKeyboardInput.MAX_INT, 0);

        String text = "";
        if (textBefore != null) {
            text += textBefore.toString();
        }

        if (textAfter != null) {
            text += textAfter.toString();
        }

        return text;
    }

    static void copyTextFromInputConnection(@NotNull InputConnection ic, @NotNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            CharSequence text = ic.getSelectedText(0);
            if (!Strings.isEmpty(text)) {
                final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(text);
            } else {
                copyWholeTextFromInputConnection(ic, context);
            }
        } else {
            copyWholeTextFromInputConnection(ic, context);
        }

    }
}
