package org.solovyev.android.security.base64;

import android.util.Base64;
import org.solovyev.common.Converter;

import javax.annotation.Nonnull;

public class ABase64StringEncoder implements Converter<byte[], String> {

	@Nonnull
	private static Converter<byte[], String> instance = new ABase64StringEncoder();

	private ABase64StringEncoder() {
	}

	@Nonnull
	public static Converter<byte[], String> getInstance() {
		return instance;
	}

	@Nonnull
	@Override
	public String convert(@Nonnull byte[] bytes) {
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}
}
