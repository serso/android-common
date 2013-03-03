package net.robotmedia.billing.utils;

import javax.annotation.Nonnull;
import org.solovyev.common.Converter;

/**
 * User: serso
 * Date: 2/9/13
 * Time: 7:22 PM
 */
public class BillingBase64StringEncoder implements Converter<byte[], String> {

    @Nonnull
    private static Converter<byte[], String> instance = new BillingBase64StringEncoder();

    private BillingBase64StringEncoder() {
    }

    @Nonnull
    public static Converter<byte[], String> getInstance() {
        return instance;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull byte[] bytes) {
        return Base64.encode(bytes);
    }
}

