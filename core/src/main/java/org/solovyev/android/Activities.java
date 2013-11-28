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

package org.solovyev.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.annotation.Nonnull;

import static org.solovyev.android.Android.newTag;

public final class Activities {

	private static final String TAG = newTag("Activities");

	private Activities() {
		throw new AssertionError();
	}

	/**
	 * Method restarts activity
	 *
	 * @param activity to be restarted activity
	 */
	public static void restartActivity(@Nonnull Activity activity) {
		final Intent intent = activity.getIntent();

		Log.d(TAG, "Restarting activity: " + activity.getClass().getSimpleName());
		activity.finish();

		activity.startActivity(intent);
	}

	public static void startActivity(@Nonnull Intent intent, @Nonnull Context context) {
		addIntentFlags(intent, false, context);
		context.startActivity(intent);
	}

	public static void addIntentFlags(@Nonnull Intent intent, boolean detached, @Nonnull Context context) {
		int flags = 0;

		if (!(context instanceof Activity)) {
			flags = flags | Intent.FLAG_ACTIVITY_NEW_TASK;
		}

		if (detached) {
			flags = flags | Intent.FLAG_ACTIVITY_NO_HISTORY;
		}

		intent.setFlags(flags);

	}
}
