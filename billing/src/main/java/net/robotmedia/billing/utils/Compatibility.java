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

package net.robotmedia.billing.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Compatibility {

    @Nullable
	private static Method activityMethod;

    @Nullable
    private static Method contextMethod;

	public static int START_NOT_STICKY;

	@SuppressWarnings("rawtypes")
	private static final Class[] START_INTENT_SENDER_SIGNATURE = new Class[]{IntentSender.class, Intent.class, int.class, int.class, int.class};
    private static final String TAG = "BillingCompatibility";

    static {
		initCompatibility();
	}

	private static void initCompatibility() {
		try {
			final Field field = Service.class.getField("START_NOT_STICKY");
			START_NOT_STICKY = field.getInt(null);
		} catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
			START_NOT_STICKY = 2;
		}

        activityMethod = initMethod(Activity.class);
        contextMethod = initMethod(Context.class);
	}

    @Nullable
    private static Method initMethod(@Nonnull Class<? extends Context> clazz) {
        Method result;

        try {
            result = clazz.getMethod("startIntentSender", START_INTENT_SENDER_SIGNATURE);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage(), e);
            result = null;
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage(), e);
            result = null;
        }

        return result;
    }

    public static void startIntentSender(@Nonnull Context context,
										 @Nonnull IntentSender intentSender,
										 @Nullable Intent intent) {
        if (context instanceof Activity) {
            startIntentSender0(context, intentSender, intent, activityMethod);
        } else {
            startIntentSender0(context, intentSender, intent, contextMethod);
        }
    }

    private static void startIntentSender0(@Nonnull Context context,
                                           @Nonnull IntentSender intentSender,
                                           @Nullable Intent intent,
                                           @Nullable Method method) {
        if (method != null) {

            final Object[] args = new Object[5];
            args[0] = intentSender;
            args[1] = intent;
            args[2] = 0;
            args[3] = 0;
            args[4] = 0;

            try {
                method.invoke(context, args);
            } catch (Exception e) {
                Log.e(TAG, "startIntentSender", e);
            }
        }
    }

    public static boolean isStartIntentSenderSupported(@Nonnull Context context) {
        if (context instanceof Activity) {
            return activityMethod != null;
        } else {
            return contextMethod != null;
        }
    }
}
