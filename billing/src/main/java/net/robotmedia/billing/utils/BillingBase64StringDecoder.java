package net.robotmedia.billing.utils;

import javax.annotation.Nonnull;
import org.solovyev.common.Converter;

/**
 * User: serso
 * Date: 2/9/13
 * Time: 7:23 PM
 */
public class BillingBase64StringDecoder implements Converter<String, byte[]> {

    @Nonnull
    private static Converter<String, byte[]> instance = new BillingBase64StringDecoder();

    private BillingBase64StringDecoder() {
    }

    @Nonnull
    public static Converter<String, byte[]> getInstance() {
        return instance;
    }

    @Nonnull
    @Override
    public byte[] convert(@Nonnull String s) {
        try {
            return Base64.decode(s);
        } catch (Base64DecoderException e) {
            throw new RuntimeException(e);
        }
    }
}
