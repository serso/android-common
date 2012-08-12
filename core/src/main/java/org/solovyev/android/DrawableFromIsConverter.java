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
