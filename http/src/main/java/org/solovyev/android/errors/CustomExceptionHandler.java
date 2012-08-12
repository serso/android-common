package org.solovyev.android.errors;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: serso
 * Date: 2/12/12
 * Time: 1:19 PM
 */

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

	private static final String TAG = "RemoteStackTrace";

	private Thread.UncaughtExceptionHandler defaultUEH;

	@Nullable
	private String localPath;

	@Nullable
	private String url;

	/*
		 * if any of the parameters is null, the respective functionality
		 * will not be used
		 */
	public CustomExceptionHandler(@Nullable String localPath, @Nullable String url) {
		this.localPath = localPath;
		this.url = url;
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
	}

	public void uncaughtException(Thread t, Throwable e) {
		try {

			final Date time = new Date();

			final String stackTrace = getStackTrace(e);

			if (stackTrace != null) {
				if (localPath != null) {
					writeToFile(stackTrace, String.valueOf(time.getTime()) + ".stacktrace");
				}

				if (url != null) {
					sendToServer(stackTrace, time);
				}
			}
		} catch (Throwable anyException) {
			Log.e(TAG, StringUtils.fromStackTrace(anyException.getStackTrace()));
		} finally {
			defaultUEH.uncaughtException(t, e);
		}
	}

	@Nullable
	private String getStackTrace(@NotNull Throwable e) {

		PrintWriter writer = null;

		String result;
		try {
			final StringWriter sw = new StringWriter();
			writer = new PrintWriter(sw);
			e.printStackTrace(writer);

			result = sw.toString();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

		return result;
	}

	private void writeToFile(@NotNull String stackTrace, @NotNull String fileName) {
		try {
			BufferedWriter bos = null;
			try {
				bos = new BufferedWriter(new FileWriter(localPath + "/" + fileName));
				bos.write(stackTrace);
				bos.flush();
			} finally {
				if (bos != null) {
					bos.close();
				}
			}
		} catch (Exception e) {
			// unable to save to the file - strange
			Log.e(TAG, StringUtils.fromStackTrace(e.getStackTrace()));
		}
	}

	private void sendToServer(@NotNull final String stackTrace, @NotNull Date time) {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		final HttpPost httpPost = new HttpPost(url);
		httpPost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

		final List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("timestamp", time.toString()));
		values.add(new BasicNameValuePair("stacktrace", stackTrace));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(values, HTTP.UTF_8));
			httpClient.execute(httpPost);
		} catch (IOException e) {
			// ok, just not connected
		}

	}
}
