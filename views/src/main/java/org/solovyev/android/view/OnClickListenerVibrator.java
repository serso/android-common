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

package org.solovyev.android.view;

import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.View;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 10/26/11
 * Time: 11:25 PM
 */
public class OnClickListenerVibrator implements View.OnClickListener {

	private static final float VIBRATION_TIME_SCALE = 1.0f;

	@Nonnull
	private VibratorContainer vibrator;

	public OnClickListenerVibrator(@Nullable Vibrator vibrator,
								   @Nonnull SharedPreferences preferences) {
		this.vibrator = new VibratorContainer(vibrator, preferences, VIBRATION_TIME_SCALE);
	}

	@Override
	public void onClick(View v) {
		vibrator.vibrate();
	}
}
