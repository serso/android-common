package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * User: serso
 * Date: 5/28/12
 * Time: 11:41 PM
 */
public class RuntimeIoException extends RuntimeException {

    public RuntimeIoException(@NotNull IOException e) {
        super(e);
    }
}
