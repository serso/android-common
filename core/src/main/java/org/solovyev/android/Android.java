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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class contains static methods for working with some android classes
 */
@SuppressWarnings("UnusedDeclaration")
public final class Android {

	// not intended for instantiation
	private Android() {
		throw new AssertionError();
	}

    /*
	**********************************************************************
    *
    *                           CONSTANTS
    *
    **********************************************************************
    */

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static final String TAG = "ACL";

    /*
    **********************************************************************
    *
    *                           STATIC FIELDS
    *
    **********************************************************************
    */

	@Nullable
	private static Boolean debug = null;

    /*
    **********************************************************************
    *
    *                           STATIC METHODS
    *
    **********************************************************************
    */

	@Nonnull
	public static String newTag(@Nonnull String tag) {
		return newSubTag(TAG, tag);
	}

	@Nonnull
	public static String newSubTag(@Nonnull String tag, @Nonnull String subTag) {
		return tag + "/" + subTag;
	}

	/**
	 * Method returns version of application identified by it's package name.
	 *
	 * @param context        context
	 * @param appPackageName full name of the package of an app, 'com.example.app' for example.
	 * @return version number of application
	 * @throws PackageManager.NameNotFoundException
	 *          if application is not found
	 */
	public static int getAppVersionCode(@Nonnull Context context, @Nonnull String appPackageName) throws PackageManager.NameNotFoundException {
		return context.getPackageManager().getPackageInfo(appPackageName, 0).versionCode;
	}

	/**
	 * @param context context
	 * @return version number of current application
	 */
	public static int getAppVersionCode(@Nonnull Context context) {
		try {
			return getAppVersionCode(context, context.getPackageName());
		} catch (PackageManager.NameNotFoundException e) {
			throw new AssertionError(e);
		}
	}

	public static boolean isPhoneModel(@Nonnull DeviceModel phoneModel) {
		final String model = Build.MODEL;
		return model != null && phoneModel.getModels().contains(model);
	}

	public static boolean isDebuggable(@Nonnull Context context) {
		if (debug == null) {
			debug = 0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
		}
		return debug;
	}

	@Nonnull
	public static Parcelable.Creator<String> getStringParcelableCreator() {
		return StringParcelableCreator.getInstance();
	}

	// use org.solovyev.android.Activities.addIntentFlags
	@Deprecated
	public static void addIntentFlags(@Nonnull Intent intent, boolean detached, @Nonnull Context context) {
		Activities.addIntentFlags(intent, detached, context);
	}

	// use enableComponent() instead
	@Deprecated
	public static void toggleComponent(@Nonnull Context context,
									   @Nonnull Class<?> componentClass,
									   boolean enable) {
		enableComponent(context, componentClass, enable);
	}

	public static void enableComponent(@Nonnull Context context,
									   @Nonnull Class<?> componentClass,
									   boolean enable) {
		final PackageManager pm = context.getPackageManager();

		final int componentState;
		if (enable) {
			componentState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
		} else {
			componentState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
		}

		pm.setComponentEnabledSetting(new ComponentName(context, componentClass), componentState, PackageManager.DONT_KILL_APP);
	}

	public static boolean isComponentEnabled(@Nonnull Context context,
											 @Nonnull Class<? extends Context> componentClass) {
		final PackageManager pm = context.getPackageManager();

		int componentEnabledSetting = pm.getComponentEnabledSetting(new ComponentName(context, componentClass));
		return componentEnabledSetting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED || componentEnabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
	}

	public static String saveBitmap(@Nonnull Bitmap bitmap,
									@Nonnull String path,
									@Nonnull String fileName) {
		final File filePath = new File(path);
		filePath.mkdirs();

		final File file = new File(path, fileName);
		if (!file.exists()) {
			final String name = file.getAbsolutePath();

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(name);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
			} catch (FileNotFoundException e) {
				Log.e(TAG, e.getMessage(), e);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
			}

			return name;
		}

		return null;
	}

}

