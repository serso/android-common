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

package org.solovyev.android.ads;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.AdView;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 1/14/12
 * Time: 6:47 PM
 */
public class AdViewPreference extends android.preference.Preference {

	// todo serso: adView must be destroyed
	@Nullable
	private AdView adView;

	public AdViewPreference(Context context) {
		super(context, null);
	}

	public AdViewPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// this will create the linear layout defined in ads_layout.xml
		View view = super.onCreateView(parent);

		if (view instanceof ViewGroup) {
			adView = AdsController.getInstance().inflateAd((Activity) getContext(), ((ViewGroup) view), 0);
		}

		return view;
	}
}