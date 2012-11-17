/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 * or visit http://se.solovyev.org
 */

package org.solovyev.android.msg;

import android.app.Application;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.msg.AbstractMessage;
import org.solovyev.common.msg.MessageType;

import java.util.List;
import java.util.Locale;

/**
 * User: serso
 * Date: 10/18/11
 * Time: 11:57 PM
 */
public class AndroidMessage extends AbstractMessage {

    @NotNull
    private final Integer messageCodeId;

    @NotNull
    private final Application application;

    private final boolean javaFormat;

    public AndroidMessage(@NotNull Integer messageCodeId,
                          @NotNull MessageType messageType,
                          @NotNull Application application,
                          @org.jetbrains.annotations.Nullable Object... arguments) {
        super(String.valueOf(messageCodeId), messageType, arguments);
        this.messageCodeId = messageCodeId;
        this.application = application;
        this.javaFormat = false;
    }

    public AndroidMessage(@NotNull Integer messageCodeId,
                          @NotNull MessageType messageType,
                          @NotNull Application application,
                          @NotNull List<?> arguments) {
        this(messageCodeId, messageType, application, arguments, false);
    }

    public AndroidMessage(@NotNull Integer messageCodeId,
                          @NotNull MessageType messageType,
                          @NotNull Application application,
                          @NotNull List<?> arguments,
                          boolean javaFormat) {
        super(String.valueOf(messageCodeId), messageType, arguments);
        this.messageCodeId = messageCodeId;
        this.application = application;
        this.javaFormat = javaFormat;
    }

    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull Locale locale) {
        if (javaFormat) {
            return super.getLocalizedMessage(locale);
        } else {
            final List<Object> parameters = getParameters();
            final Object[] parametersArray = parameters.toArray(new Object[parameters.size()]);
            return application.getResources().getString(messageCodeId, (Object[])parametersArray);
        }
    }

    @Override
    protected String getMessagePattern(@NotNull Locale locale) {
        return application.getResources().getString(messageCodeId);
    }
}
