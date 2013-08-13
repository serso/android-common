package org.solovyev.android.samples;

import net.robotmedia.billing.BillingController;

final class SamplesBillingConfiguration implements BillingController.IConfiguration {
	@Override
	public byte[] getObfuscationSalt() {
		return new byte[]{111, 114, 111, -29, -76, -128, 87, -61, -117, 26, -46, -57, 109, -59, -42, -59, -21, -43, -100, -96};
	}

	@Override
	public String getPublicKey() {
		final StringBuilder result = new StringBuilder();

		result.append("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A");
		result.append("MIIBCgKCAQEAquP2a7dEhTaJEQeXtSyreH5dCmTDOd");
		result.append("dElCfg0ijOeB8JTxBiJTXLWnLA0kMaT/sRXswUaYI61YCQOoik82");
		result.append("qrFH7W4+OFtiLb8WGX+YPEpQQ/IBZu9qm3xzS9Nolu79EBff0/CLa1FuT9RtjO");
		result.append("iTW8Q0VP9meQdJEkfqJEyVCgHain+MGoQaRXI45EzkYmkz8TBx6X6aJF5NBAXnAWeyD0wPX1");
		result.append("uedHH7+LgLcjnPVw82YjyJSzYnaaD2GX0Y7PGoFe6J5K4yJGGX5mih45pe2HWcG5lAkQhu1uX2hCcCBdF3");
		result.append("W7paRq9mJvCsbn+BNTh9gq8QKui0ltmiWpa5U+/9L+FQIDAQAB");

		return result.toString();
	}
}
