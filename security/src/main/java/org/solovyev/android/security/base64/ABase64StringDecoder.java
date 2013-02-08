package org.solovyev.android.security.base64;

import android.util.Base64;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Converter;

public class ABase64StringDecoder implements Converter<String, byte[]> {

    @NotNull
    private static Converter<String, byte[]> instance = new ABase64StringDecoder();

    private ABase64StringDecoder() {
    }

    @NotNull
    public static Converter<String, byte[]> getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public byte[] convert(@NotNull String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }
}