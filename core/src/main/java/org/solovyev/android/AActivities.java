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
import android.content.Intent;
import android.util.Log;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 2/2/13
 * Time: 6:03 PM
 */
public final class AActivities {

    private AActivities() {
        throw new AssertionError();
    }

    /**
	 * Method restarts activity
	 * @param activity to be restarted activity
	 */
	public static void restartActivity(@NotNull Activity activity) {
		final Intent intent = activity.getIntent();

		Log.d(activity.getClass().getName(), "Finishing current activity!");
		activity.finish();

		Log.d(activity.getClass().getName(), "Starting new activity!");
		activity.startActivity(intent);
	}
}
