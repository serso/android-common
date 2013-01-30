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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android;

import android.graphics.drawable.Drawable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.Converter;

import java.io.InputStream;

/**
 * User: serso
 * Date: 5/29/12
 * Time: 11:20 PM
 */
public class DrawableFromIsConverter implements Converter<InputStream, Drawable> {

    @NotNull
    private static final DrawableFromIsConverter instance = new DrawableFromIsConverter("instance");

    @NotNull
    private final String name;

    @Nullable
    private final Drawable defaultDrawable;

    public DrawableFromIsConverter(@NotNull String name) {
        this(name, null);
    }

    public DrawableFromIsConverter(@NotNull String name, @Nullable Drawable defaultDrawable) {
        this.name = name;
        this.defaultDrawable = defaultDrawable;
    }

    @NotNull
    public static DrawableFromIsConverter getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public Drawable convert(@NotNull InputStream inputStream) {
        Drawable result = Drawable.createFromStream(inputStream, name);

        if ( result == null ) {
            result = defaultDrawable;
        }

        if ( result == null ) {
            throw new DrawableConversionFailedException();
        }

        return result;
    }

    public static class DrawableConversionFailedException extends RuntimeException {

    }
}
