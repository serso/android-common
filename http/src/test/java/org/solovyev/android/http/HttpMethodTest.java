package org.solovyev.android.http;

import java.util.Collections;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.solovyev.android.http.HttpMethod.ParamsLocation.in_uri;

@Config(manifest= Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class HttpMethodTest {

	@Test
	public void testShouldReturnUrlWithParams() throws Exception {
		assertEquals("test", in_uri.prepareUri("test", Collections.<BasicNameValuePair>emptyList(), null));
	}

	@Test
	public void testShouldAddParamToUrl() throws Exception {
		assertEquals("test?param=value", in_uri.prepareUri("test", asList(new BasicNameValuePair("param", "value")), null));
	}

	@Test
	public void testShouldEncodeParamsInUrl() throws Exception {
		assertEquals("test?param=%2C%2C%60%28%2F", in_uri.prepareUri("test", asList(new BasicNameValuePair("param", ",,`(/")), null));
	}
}
