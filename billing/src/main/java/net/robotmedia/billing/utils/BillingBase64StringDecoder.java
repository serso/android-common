package net.robotmedia.billing.utils;

import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Converter;

/**
 * User: serso
 * Date: 2/9/13
 * Time: 7:23 PM
 */
public class BillingBase64StringDecoder implements Converter<String, byte[]> {

    @NotNull
    private static Converter<String, byte[]> instance = new BillingBase64StringDecoder();

    private BillingBase64StringDecoder() {
    }

    @NotNull
    public static Converter<String, byte[]> getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public byte[] convert(@NotNull String s) {
        try {
            return Base64.decode(s);
        } catch (Base64DecoderException e) {
            throw new RuntimeException(e);
        }
    }
}
