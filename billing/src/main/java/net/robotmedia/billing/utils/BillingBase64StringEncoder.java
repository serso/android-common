package net.robotmedia.billing.utils;

import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Converter;

/**
 * User: serso
 * Date: 2/9/13
 * Time: 7:22 PM
 */
public class BillingBase64StringEncoder implements Converter<byte[], String> {

    @NotNull
    private static Converter<byte[], String> instance = new BillingBase64StringEncoder();

    private BillingBase64StringEncoder() {
    }

    @NotNull
    public static Converter<byte[], String> getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public String convert(@NotNull byte[] bytes) {
        return Base64.encode(bytes);
    }
}

