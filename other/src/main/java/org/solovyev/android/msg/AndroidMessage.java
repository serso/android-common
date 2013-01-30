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
