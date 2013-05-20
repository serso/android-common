package org.solovyev.android.security.base64;

import android.util.Base64;
import org.solovyev.common.Converter;

import javax.annotation.Nonnull;

public class ABase64StringDecoder implements Converter<String, byte[]> {

	@Nonnull
	private static Converter<String, byte[]> instance = new ABase64StringDecoder();

	private ABase64StringDecoder() {
	}

	@Nonnull
	public static Converter<String, byte[]> getInstance() {
		return instance;
	}

	@Nonnull
	@Override
	public byte[] convert(@Nonnull String s) {
		return Base64.decode(s, Base64.DEFAULT);
	}
}