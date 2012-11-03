package org.solovyev.android.keyboard;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 11/3/12
 * Time: 9:39 PM
 */
public class AKeyboardConfigurationImpl implements AKeyboardConfiguration {

    @NotNull
    private final String wordSeparators;

    public AKeyboardConfigurationImpl(@NotNull String wordSeparators) {
        this.wordSeparators = wordSeparators;
    }

    @NotNull
    @Override
    public String getWordSeparators() {
        return wordSeparators;
    }
}
