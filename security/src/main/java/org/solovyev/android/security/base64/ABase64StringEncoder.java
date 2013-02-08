package org.solovyev.android.security.base64;

import android.util.Base64;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Converter;

public class ABase64StringEncoder implements Converter<byte[], String> {

    @NotNull
    private static Converter<byte[], String> instance = new ABase64StringEncoder();

    private ABase64StringEncoder() {
    }

    @NotNull
    public static Converter<byte[], String> getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public String convert(@NotNull byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
