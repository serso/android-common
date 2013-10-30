/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.samples.http;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.solovyev.android.Threads;
import org.solovyev.android.async.CommonAsyncTask;
import org.solovyev.android.http.*;
import org.solovyev.android.list.ListItemAdapter;
import org.solovyev.android.samples.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 2:26 PM
 */
public class SamplesHttpActivity extends ListActivity {

	@Nonnull
	private ImageLoader imageLoader;

	@Nonnull
	private static final String fetchDataUri = "http://se.solovyev.org/other/acl/data.txt";

	@Nonnull
	private static final String uriPrefix = "http://se.solovyev.org/other/acl/icons/";

	private static final List<String> imageNames = new ArrayList<String>();

	static {
		imageNames.add("Android-Action-complete.png");
		imageNames.add("Android-Adobe-Photoshop.png");
		imageNames.add("Android-Amazon-MP3.png");
		imageNames.add("Android-Barcode-Reader.png");
		imageNames.add("Android-Bluetooth.png");
		imageNames.add("Android-Browser.png");
		imageNames.add("Android-Buzz.png");
		imageNames.add("Android-Calculator.png");
		imageNames.add("Android-Calender.png");
		imageNames.add("Android-Camera.png");
		imageNames.add("Android-Car-Home.png");
		imageNames.add("Android-Clock.png");
		imageNames.add("Android-Contacts.png");
		imageNames.add("Android-Email.png");
		imageNames.add("Android-Evernote.png");
		imageNames.add("Android-Facebook.png");
		imageNames.add("Android-Flickr.png");
		imageNames.add("Android-Gallery.png");
		imageNames.add("Android-Gmail.png");
		imageNames.add("Android-Goggles.png");
		imageNames.add("Android-Gtalk.png");
		imageNames.add("Android-Layar.png");
		imageNames.add("Android-Maps.png");
		imageNames.add("Android-Market.png");
		imageNames.add("Android-Messages.png");
		imageNames.add("Android-mPhotoshop-CS4.png");
		imageNames.add("Android-Music.png");
		imageNames.add("Android-News.png");
		imageNames.add("Android-NewsWeather.png");
		imageNames.add("Android-Opera-Mini.png");
		imageNames.add("Android-Opera-Mini-Android.png");
		imageNames.add("Android-Phone.png");
		imageNames.add("Android-QR-Code.png");
		imageNames.add("Android-Recorder.png");
		imageNames.add("Android-Recorder-detailed.png");
		imageNames.add("Android-RSS.png");
		imageNames.add("Android-Settings.png");
		imageNames.add("Android-Shazam.png");
		imageNames.add("Android-Skype.png");
		imageNames.add("Android-Tag-reader.png");
		imageNames.add("Android-Twitter.png");
		imageNames.add("Android-Twitter-2.png");
		imageNames.add("Android-Voice.png");
		imageNames.add("Android-Voice-Dailer.png");
		imageNames.add("Android-Voice-Search.png");
		imageNames.add("Android-Weather.png");
		imageNames.add("Android-Wi-Fi.png");
		imageNames.add("Android-Wikitude.png");
		imageNames.add("Android-Youtube.png");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acl_http_layout);

		// should be one instance in application if several threads are working with it
		this.imageLoader = new CachingImageLoader(this, "acl-samples", new Handler());

		final List<HttpListItem> listItems = new ArrayList<HttpListItem>();
		for (String imageName : imageNames) {
			listItems.add(new HttpListItem(uriPrefix + imageName, this.imageLoader));
		}
		ListItemAdapter.createAndAttach(this, listItems);


		final Button fetchDataButton = (Button) findViewById(R.id.fetch_data_button);
		fetchDataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new CommonAsyncTask<String, Void, String>(SamplesHttpActivity.this) {

					@Override
					protected String doWork(@Nonnull List<String> strings) {
						assert strings.size() == 1;
						try {
							return HttpTransactions.execute(new FetchHttpData(strings.get(0)));
						} catch (IOException e) {
							throwException(e);
						}

						return "";
					}

					@Override
					protected void onSuccessPostExecute(@Nullable final String result) {
						final Activity activity = (Activity) getContext();
						Threads.tryRunOnUiThread(activity, new Runnable() {
							@Override
							public void run() {
								Toast.makeText(activity, getString(R.string.acl_http_response) + " " + result, Toast.LENGTH_SHORT).show();
							}
						});
					}

					@Override
					protected void onFailurePostExecute(@Nonnull final Exception e) {
						final Activity activity = (Activity) getContext();
						Threads.tryRunOnUiThread(activity, new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					}
				}.execute(fetchDataUri);
			}
		});
	}


	private static class FetchHttpData extends AbstractHttpTransaction<String> {

		private FetchHttpData(@Nonnull String uri) {
			super(uri, HttpMethod.GET);
		}

		@Override
		public String getResponse(@Nonnull HttpResponse response) {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				throw new HttpRuntimeIoException(e);
			}
		}

		@Nonnull
		@Override
		public List<NameValuePair> getRequestParameters() {
			return Collections.emptyList();
		}
	}
}
