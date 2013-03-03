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
import android.os.Build;
import android.text.ClipboardManager;
import android.view.inputmethod.InputConnection;
import javax.annotation.Nonnull;
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

    public static void copyWholeTextFromInputConnection(@Nonnull InputConnection ic, @Nonnull Context context) {
        String text = getTextFromInputConnection(ic);

        if (!Strings.isEmpty(text)) {
            final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        }
    }

    @Nonnull
    public static String getTextFromInputConnection(@Nonnull InputConnection ic) {
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

    static void copyTextFromInputConnection(@Nonnull InputConnection ic, @Nonnull Context context) {
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
